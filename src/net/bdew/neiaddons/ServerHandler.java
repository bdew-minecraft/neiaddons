/**
 * Copyright (c) bdew, 2013
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/neiaddons/master/MMPL-1.0.txt
 */

package net.bdew.neiaddons;

import java.util.HashMap;
import java.util.Map;

import net.bdew.neiaddons.api.SubPacketHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class ServerHandler implements IPacketHandler {
    private static Map<String,SubPacketHandler> handlers = new HashMap<String,SubPacketHandler>();
    
    public static void registerHandler(String command, SubPacketHandler handler) {
        if (handlers.containsKey(command)) {
            throw new RuntimeException(String.format("Tried to register handler for command %s that's already registered for %s", command, handler.toString()));
        }
        handlers.put(command, handler);
    }
    
    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
        EntityPlayerMP p = (EntityPlayerMP) player;

        try {

            NBTTagCompound data = CompressedStreamTools.decompress(packet.data);
            String cmd = data.getString("cmd");

            if (handlers.containsKey(cmd)) {
                handlers.get(cmd).handle(data.getCompoundTag("data"), p);
            } else {
                NEIAddons.logWarning("Uknown packet from client '%s': %s", p.username, cmd);
            }
        } catch (Throwable e) {
            NEIAddons.logWarning("Error handling packet from client '%s'", p.username);
            e.printStackTrace();
        }
    }
}
