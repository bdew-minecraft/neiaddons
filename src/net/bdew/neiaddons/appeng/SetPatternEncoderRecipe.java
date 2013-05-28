/**
 * Copyright (c) bdew, 2013
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/neiaddons/master/MMPL-1.0.txt
 */

package net.bdew.neiaddons.appeng;

import java.util.HashMap;

import net.bdew.neiaddons.api.SubPacketHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class SetPatternEncoderRecipe implements SubPacketHandler {
    static public final String command="SetPatternEncoderRecipe"; 
    
    @Override
    public void handle(NBTTagCompound data, EntityPlayerMP player) {
        NBTTagList stacks = data.getTagList("stacks");
        Container cont = player.openContainer;
        if (AddonAE.ContainerPatternEncoder.isInstance(cont)) {
            HashMap<Integer, ItemStack> stmap = new HashMap<Integer, ItemStack>();
            for (Object tag : stacks.tagList) {
                if (tag instanceof NBTTagCompound) {
                    NBTTagCompound itemdata = (NBTTagCompound) tag;
                    stmap.put(itemdata.getInteger("slot"), ItemStack.loadItemStackFromNBT(itemdata));
                }
            }
            for (Object slotobj : cont.inventorySlots) {
                // only operate on SlotFake to prevent potential exploits
                if (AddonAE.SlotFake.isInstance(slotobj)) {
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
