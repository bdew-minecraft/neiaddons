/*
 * Copyright (c) bdew, 2013 - 2015
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.neiaddons.appeng;

import codechicken.nei.api.INEIGuiAdapter;
import net.bdew.neiaddons.network.ClientHandler;
import net.bdew.neiaddons.network.PacketHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

public class AppEngGuiHandler extends INEIGuiAdapter {
    @Override
    public boolean handleDragNDrop(GuiContainer gui, int mousex, int mousey, ItemStack draggedStack, int button) {
        if (AddonAppeng.clsBaseGui.isInstance(gui)) {
            int slotNum = -1;
            Slot slot = null;
            for (int k = 0; k < gui.inventorySlots.inventorySlots.size(); k++) {
                slot = (Slot) gui.inventorySlots.inventorySlots.get(k);
                if (isMouseOverSlot(gui, slot, mousex, mousey)) {
                    slotNum = k;
                    break;
                }
            }
            if ((slotNum > 0) && (AddonAppeng.clsSlotFake.isInstance(slot)) && SlotHelper.isSlotEnabled(slot)) {
                if (ClientHandler.enabledCommands.contains(AddonAppeng.setWorkbenchCommand)) {
                    NBTTagCompound data = new NBTTagCompound();
                    data.setInteger("slot", slotNum);
                    NBTTagCompound item = new NBTTagCompound();
                    draggedStack.writeToNBT(item);
                    data.setTag("item", item);
                    PacketHelper.sendToServer(AddonAppeng.setWorkbenchCommand, data);
                    return true;
                } else {
                    Minecraft.getMinecraft().thePlayer.addChatMessage(
                            new ChatComponentTranslation("bdew.neiaddons.noserver").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED))
                    );
                }
            }
        }

        return super.handleDragNDrop(gui, mousex, mousey, draggedStack, button);
    }

    private boolean isMouseOverSlot(GuiContainer gui, Slot slot, int mouseX, int mouseY) {
        int slotX = slot.xDisplayPosition;
        int slotY = slot.yDisplayPosition;
        int slotW = 16;
        int slotH = 16;
        mouseX -= gui.guiLeft;
        mouseY -= gui.guiTop;
        return mouseX >= slotX - 1 && mouseX < slotX + slotW + 1 && mouseY >= slotY - 1 && mouseY < slotY + slotH + 1;
    }
}
