/*
 * Copyright (c) bdew, 2013 - 2015
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.neiaddons.utils;

import codechicken.nei.LayoutManager;
import codechicken.nei.NEIClientUtils;
import codechicken.nei.OffsetPositioner;
import codechicken.nei.PositionedStack;
import codechicken.nei.api.DefaultOverlayRenderer;
import codechicken.nei.api.IOverlayHandler;
import codechicken.nei.api.IStackPositioner;
import codechicken.nei.guihook.GuiContainerManager;
import codechicken.nei.recipe.IRecipeHandler;
import net.bdew.neiaddons.NEIAddons;
import net.bdew.neiaddons.network.ClientHandler;
import net.bdew.neiaddons.network.PacketHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.List;

import static codechicken.nei.NEIServerUtils.areStacksSameType;

public class CustomOverlayHandler implements IOverlayHandler {
    private boolean invert;
    private String command;
    private int xOffs, yOffs;
    private Class<? extends Slot> craftingSlot;

    public CustomOverlayHandler(String command, int xOffs, int yOffs, boolean invert, Class<? extends Slot> craftingSlot) {
        super();
        this.command = command;
        this.xOffs = xOffs;
        this.yOffs = yOffs;
        this.craftingSlot = craftingSlot;
        this.invert = invert;
    }

    private Slot findMatchingSlot(GuiContainer cont, PositionedStack pstack) {
        for (Object slotob : cont.inventorySlots.inventorySlots) {
            Slot slot = (Slot) slotob;
            if ((slot.xDisplayPosition == pstack.relx + xOffs) && (slot.yDisplayPosition == pstack.rely + yOffs)) {
                return slot;
            }
        }
        NEIAddons.logWarning("Failed to find matching slot - (%d,%d) in %s", pstack.relx + xOffs, pstack.rely + yOffs, cont.toString());
        return null;
    }

    private Boolean isValidSlot(Slot slot) {
        // Don't try to take items from special slots
        return (slot.inventory == Minecraft.getMinecraft().thePlayer.inventory) || (slot.getClass() == Slot.class);
    }

    private Slot findItem(GuiContainer cont, PositionedStack p) {
        for (ItemStack teststack : p.items) {
            for (Object slotob : cont.inventorySlots.inventorySlots) {
                Slot slot = (Slot) slotob;
                if (isValidSlot(slot)) {
                    ItemStack stack = slot.getStack();
                    if (stack != null && areStacksSameType(stack, teststack)) {
                        return slot;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void overlayRecipe(GuiContainer cont, IRecipeHandler recipe, int recipeIndex, boolean shift) {
        List<PositionedStack> ingr = recipe.getIngredientStacks(recipeIndex);

        if (invert) {
            shift = !shift;
        }

        if (!shift) {
            IStackPositioner positioner = new OffsetPositioner(xOffs, yOffs);
            LayoutManager.overlayRenderer = new DefaultOverlayRenderer(ingr, positioner);
        } else if (ClientHandler.enabledCommands.contains(command)) {
            NBTTagList stacksnbt = new NBTTagList();

            for (PositionedStack pstack : ingr) {
                if (pstack != null) {
                    // This is back-asswards but i don't see a better way :(
                    int x = (pstack.relx - 25) / 18;
                    int y = (pstack.rely - 6) / 18;

                    ItemStack stack = pstack.item;
                    NBTTagCompound stacknbt = stack.writeToNBT(new NBTTagCompound());
                    stacknbt.setInteger("slot", y * 3 + x);
                    stacksnbt.appendTag(stacknbt);
                }

                ItemStack stack = recipe.getResultStack(recipeIndex).items[0];
                NBTTagCompound stacknbt = stack.writeToNBT(new NBTTagCompound());
                stacknbt.setInteger("slot", 9);
                stacksnbt.appendTag(stacknbt);
            }

            NBTTagCompound data = new NBTTagCompound();
            data.setTag("stacks", stacksnbt);

            PacketHelper.sendToServer(command, data);
        } else {
            if (NEIClientUtils.getHeldItem() != null) {
                return;
            }
            NEIAddons.logInfo("Don't have server support, moving recipe manually");
            GuiContainerManager manager = GuiContainerManager.getManager(cont);
            //noinspection ConstantConditions
            if (manager != null) {
                for (Object slotob : cont.inventorySlots.inventorySlots) {
                    if (craftingSlot.isInstance(slotob)) {
                        Slot slot = (Slot) slotob;
                        // Left click once to clear
                        manager.handleSlotClick(slot.slotNumber, 0, 0);
                    }
                }
                for (PositionedStack pstack : ingr) {
                    if (pstack != null) {

                        Slot slotTo = findMatchingSlot(cont, pstack);
                        if (slotTo == null)
                            continue;

                        Slot slotFrom = findItem(cont, pstack);
                        if (slotFrom == null)
                            continue;

                        NEIAddons.logInfo("Moving from slot %s[%d] to %s[%d]", slotFrom.toString(), slotFrom.slotNumber, slotTo.toString(), slotTo.slotNumber);

                        // pick up item
                        manager.handleSlotClick(slotFrom.slotNumber, 0, 0);
                        // right click to add 1
                        manager.handleSlotClick(slotTo.slotNumber, 1, 0);
                        // put item back
                        manager.handleSlotClick(slotFrom.slotNumber, 0, 0);
                    }
                }
            }
        }
    }
}
