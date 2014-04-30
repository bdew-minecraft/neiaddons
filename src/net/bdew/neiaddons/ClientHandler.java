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
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ClientHandler {
    public static Set<String> enabledCommands = new HashSet<String>();
    private WorldClient oldworld;

    public ClientHandler() {
        FMLCommonHandler.instance().bus().register(this);
    }

    @SubscribeEvent
    public void handleTickEvent(TickEvent ev) {
        if (ev.phase == TickEvent.Phase.START && ev.type == TickEvent.Type.WORLD) {
            WorldClient world = Minecraft.getMinecraft().theWorld;
            if ((world != null) && (world != oldworld)) {
                reset();
            }
        }
    }

    private void reset() {
        NEIAddons.logInfo("World changed, resetting");
        oldworld = Minecraft.getMinecraft().theWorld;
        enabledCommands.clear();
    }

    public void processCommand(String cmd, NBTTagCompound data) {
        try {
            if (cmd.equals("hello")) {
                reset();
                if (data.getCompoundTag("data").getInteger("version") != NEIAddons.netVersion) {
                    NEIAddons.logWarning("Client/Server version mismatch! client=%d server=%d", data.getCompoundTag("data").getInteger("version"), NEIAddons.netVersion);
                    return;
                }
                String cmds = data.getCompoundTag("data").getString("commands");
                NEIAddons.logInfo("Available server commands: %s", cmds);
                enabledCommands.addAll(Arrays.asList(StringUtils.split(cmds, ';')));
            } else {
                NEIAddons.logWarning("Uknown packet from server: %s", cmd);
            }
        } catch (Throwable e) {
            NEIAddons.logWarning("Error handling packet from server");
            e.printStackTrace();
        }
    }
}
