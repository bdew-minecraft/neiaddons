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

import net.bdew.neiaddons.NEIAddons;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class ServerHandler implements IPacketHandler {
    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
        EntityPlayerMP p = (EntityPlayerMP) player;

        try {

            NBTTagCompound data = CompressedStreamTools.decompress(packet.data);
            String op = data.getString("op");

            if (op.equals("LoadRecipe")) {
                handle_LoadRecipe(p, data.getTagList("stacks"));
            } else {
                NEIAddons.log.severe(String.format("Uknown packet from client '%s': %s", p.username, op));
            }

        } catch (Throwable e) {
            NEIAddons.log.severe(String.format("Error handling packet from client '%s'", p.username));
            e.printStackTrace();
        }
    }

    private void handle_LoadRecipe(EntityPlayerMP p, NBTTagList stacks) {
        Container cont = p.openContainer;
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
