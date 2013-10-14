/**
 * Copyright (c) bdew, 2013
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/neiaddons/master/MMPL-1.0.txt
 */

package net.bdew.neiaddons.forestry.crafting;

import java.util.HashMap;

import net.bdew.neiaddons.api.SubPacketHandler;
import net.bdew.neiaddons.forestry.AddonForestry;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class SetWorktableRecipe implements SubPacketHandler {
    static public final String command = "SetWorktableRecipe";

    @Override
    public void handle(NBTTagCompound data, EntityPlayerMP player) {
        NBTTagList stacks = data.getTagList("stacks");
        Container cont = player.openContainer;
        AddonForestry.instance.logInfo("Cont: %s, %d slots",cont.toString(),cont.inventorySlots.size());
        if (CraftingOverlayHelper.ContainerWorktable.isInstance(cont)) {
            HashMap<Integer, ItemStack> stmap = new HashMap<Integer, ItemStack>();
            for (int i = 0; i < stacks.tagCount(); i++) {
                NBTBase tag = stacks.tagAt(i);
                if (tag instanceof NBTTagCompound) {
                    NBTTagCompound itemdata = (NBTTagCompound) tag;
                    stmap.put(itemdata.getInteger("slot"), ItemStack.loadItemStackFromNBT(itemdata));
                }
            }
            for (Object slotobj : cont.inventorySlots) {
                // only operate on SlotCraftMatrix to prevent potential exploits
                AddonForestry.instance.logInfo("Found slot %d (%s) - %s", ((Slot) slotobj).slotNumber, slotobj.getClass().toString(), slotobj.toString());
                if (CraftingOverlayHelper.SlotCraftMatrix.isInstance(slotobj)) {
                    Slot slot = (Slot) slotobj;
                    if (stmap.containsKey(slot.getSlotIndex())) {
                        slot.putStack(stmap.get(slot.getSlotIndex()));
                    } else {
                        slot.putStack(null);
                    }
                }
            }
        }
    }
}
