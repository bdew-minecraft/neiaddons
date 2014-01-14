/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/neiaddons/master/MMPL-1.0.txt
 */

package net.bdew.neiaddons;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.PacketDispatcher;

public class PacketHelper {
    private static Packet250CustomPayload makePacket(String cmd, NBTTagCompound data) throws IOException {
        NBTTagCompound nbt = new NBTTagCompound();

        nbt.setString("cmd", cmd);
        nbt.setTag("data", data);

        return new Packet250CustomPayload(NEIAddons.channel, CompressedStreamTools.compress(nbt));
    }
    
    public static void sendToServer(String cmd, NBTTagCompound data) {
        try {
            PacketDispatcher.sendPacketToServer(makePacket(cmd, data));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendToClient(String cmd, NBTTagCompound data, EntityPlayerMP player) {
        try {
            player.playerNetServerHandler.sendPacketToPlayer(makePacket(cmd, data));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
