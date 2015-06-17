/*
 * Copyright (c) bdew, 2013 - 2015
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.neiaddons.network;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.bdew.neiaddons.NEIAddons;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ClientHandler extends SimpleChannelInboundHandler<NBTTagCompound> {
    public static Set<String> enabledCommands = new HashSet<String>();

    public ClientHandler() {
        FMLCommonHandler.instance().bus().register(this);
    }

    @SubscribeEvent
    public void handleConnectEvent(FMLNetworkEvent.ClientConnectedToServerEvent ev) {
        NEIAddons.logInfo("Connected to new server, resetting commands");
        enabledCommands.clear();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, NBTTagCompound msg) throws Exception {
        String cmd = msg.getString("cmd");
        NBTTagCompound data = msg.getCompoundTag("data");
        try {
            if (cmd.equals("hello")) {
                NEIAddons.logInfo("Received handshake from server");
                enabledCommands.clear();
                if (data.getInteger("version") != NEIAddons.netVersion) {
                    NEIAddons.logWarning("Client/Server version mismatch! client=%d server=%d", data.getInteger("version"), NEIAddons.netVersion);
                    return;
                }
                String cmds = data.getString("commands");
                NEIAddons.logInfo("Available server commands: %s", cmds);
                enabledCommands.addAll(Arrays.asList(StringUtils.split(cmds, ';')));
            } else {
                NEIAddons.logWarning("Uknown packet from server: %s", cmd);
            }
        } catch (Throwable e) {
            NEIAddons.logSevereExc(e, "Error handling packet from server");
        }
    }
}
