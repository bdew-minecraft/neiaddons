/**
 * Copyright (c) bdew, 2013
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/neiaddons/master/MMPL-1.0.txt
 */

package net.bdew.neiaddons;

import java.io.IOException;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.PacketDispatcher;

public class PacketHelper {

    public static void send(String cmd, NBTTagCompound data) {
        NBTTagCompound nbt = new NBTTagCompound();

        nbt.setString("cmd", cmd);
        nbt.setTag("data", data);

        try {
            Packet250CustomPayload pkt = new Packet250CustomPayload(NEIAddons.channel, CompressedStreamTools.compress(nbt));
            PacketDispatcher.sendPacketToServer(pkt);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
