/*
 * Copyright (c) bdew, 2013 - 2015
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.neiaddons.network;

import net.bdew.neiaddons.NEIAddons;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

public class PacketHelper {
    private static NBTTagCompound makePacket(String cmd, NBTTagCompound data) {
        NBTTagCompound nbt = new NBTTagCompound();

        nbt.setString("cmd", cmd);
        nbt.setTag("data", data);

        return nbt;
    }

    public static void sendToServer(String cmd, NBTTagCompound data) {

        NEIAddons.channel.sendToServer(makePacket(cmd, data));
    }

    public static void sendToClient(String cmd, NBTTagCompound data, EntityPlayerMP player) {
        NEIAddons.channel.sendTo(makePacket(cmd, data), player);
    }
}
