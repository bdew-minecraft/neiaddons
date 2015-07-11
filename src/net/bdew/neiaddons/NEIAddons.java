/*
 * Copyright (c) bdew, 2013 - 2015
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.neiaddons;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import net.bdew.neiaddons.api.NEIAddon;
import net.bdew.neiaddons.network.ClientHandler;
import net.bdew.neiaddons.network.NetChannel;
import net.bdew.neiaddons.network.ServerHandler;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

@Mod(modid = NEIAddons.modId, name = "NEI Addons", version = "NEIADDONS_VER", dependencies = "after:NotEnoughItems")
public class NEIAddons {

    public static final String modId = "NEIAddons";
    public static final String channelId = "bdew.neiaddons";
    public static final int netVersion = 1;

    public static List<NEIAddon> addons;
    public static Configuration config;
    private static Logger log;

    public static ServerHandler serverHandler;
    public static ClientHandler clientHandler;
    public static NetChannel channel;

    public static void register(NEIAddon addon) {
        addons.add(addon);
    }

    public static void logInfo(String message, Object... params) {
        log.log(Level.INFO, String.format(message, params));
    }

    public static void logWarning(String message, Object... params) {
        log.log(Level.WARN, String.format(message, params));
    }

    public static void logSevere(String message, Object... params) {
        log.log(Level.ERROR, String.format(message, params));
    }

    public static void logWarningExc(Throwable t, String message, Object... params) {
        log.log(Level.WARN, String.format(message, params), t);
    }

    public static void logSevereExc(Throwable t, String message, Object... params) {
        log.log(Level.ERROR, String.format(message, params), t);
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        log = event.getModLog();
        channel = new NetChannel(channelId);

        config = new Configuration(event.getSuggestedConfigurationFile());
        config.addCustomCategoryComment("Addons", "Controls loading of different addons, set to false to disable");
        addons = new ArrayList<NEIAddon>();

        if (event.getSide() == Side.CLIENT && !Loader.isModLoaded("NotEnoughItems")) {
            logSevere("NEI doesn't seem to be installed... NEI Addons require it to do anything useful client-side");
        }
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        logInfo("Loading NEI Addons");
        for (NEIAddon addon : addons) {
            if (config.get("Addons", addon.getName(), addon.isEnabledByDefault()).getBoolean(false)) {
                logInfo("Loading %s Addon...", addon.getName());
                try {
                    addon.init(event.getSide());
                    if (addon.isActive()) {
                        logInfo("%s Addon successfully loadded", addon.getName());
                    }
                } catch (Exception e) {
                    log.warn(String.format("Loading %s Addon - Failed:", addon.getName()), e);
                }
            } else {
                logInfo("%s Addon disabled - skipping", addon.getName());
            }
        }

        config.save();

        serverHandler = new ServerHandler();
        channel.addHandler(Side.SERVER, serverHandler);

        if (event.getSide().isClient()) {
            clientHandler = new ClientHandler();
            channel.addHandler(Side.CLIENT, clientHandler);
        }

        if (addons.size() > 0) {
            String addonslist = "Loaded Addons:";
            for (NEIAddon addon : addons) {
                addonslist += "\n- " + addon.getName() + ": " + (addon.isActive() ? "Active" : "Inactive");
            }
            Loader.instance().activeModContainer().getMetadata().description = addonslist;
        } else {
            Loader.instance().activeModContainer().getMetadata().description = "No Addons loaded :(";
        }
    }
}
