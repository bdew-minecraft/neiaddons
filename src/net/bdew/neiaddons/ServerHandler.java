/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/neiaddons/master/MMPL-1.0.txt
 */

package net.bdew.neiaddons;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.bdew.neiaddons.api.SubPacketHandler;
import net.bdew.neiaddons.network.PacketHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class ServerHandler {
    private static Map<String, SubPacketHandler> handlers = new HashMap<String, SubPacketHandler>();

    public ServerHandler() {
        FMLCommonHandler.instance().bus().register(this);
    }

    public static void registerHandler(String command, SubPacketHandler handler) {
        if (handlers.containsKey(command)) {
            throw new RuntimeException(String.format("Tried to register handler for command %s that's already registered for %s", command, handler.toString()));
        }
        handlers.put(command, handler);
    }

    public void processCommand(String cmd, NBTTagCompound data, EntityPlayerMP from) {
        if (handlers.containsKey(cmd)) {
            NEIAddons.logInfo("Handling %s from %s -> %s", cmd, from.getDisplayName(), handlers.get(cmd).toString());
            handlers.get(cmd).handle(data.getCompoundTag("data"), from);
        } else {
            NEIAddons.logWarning("Uknown packet from client '%s': %s", from.getDisplayName(), cmd);
        }
    }

    private void sendPlayerHello(EntityPlayer player) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("commands", StringUtils.join(handlers.keySet(), ';'));
        nbt.setInteger("version", NEIAddons.netVersion);
        PacketHelper.sendToClient("hello", nbt, (EntityPlayerMP) player);
    }

    @SubscribeEvent
    public void handlePlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent ev) {
        sendPlayerHello(ev.player);
    }

    @SubscribeEvent
    public void handlePlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent ev) {
        sendPlayerHello(ev.player);
    }
}
