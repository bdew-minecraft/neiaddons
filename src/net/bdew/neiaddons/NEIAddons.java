/**
 * Copyright (c) bdew, 2013
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/neiaddons/master/MMPL-1.0.txt
 */

package net.bdew.neiaddons;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.bdew.neiaddons.api.NEIAddon;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = NEIAddons.modid, name = "NEI Addons", version = "@@VERSION@@", dependencies = "after:NotEnoughItems")
@NetworkMod(clientSideRequired = false, serverSideRequired = false)
public class NEIAddons {
    public static Logger log;

    public static final String modid = "NEIAddons";
    public static final String channel = "bdew.neiaddons";

    public static List<NEIAddon> addons;
    public static Configuration config;

    public static void register(NEIAddon addon) {
        addons.add(addon);
    }

    public static void logInfo(String message, Object... params) {
        log.log(Level.INFO, String.format(message, params));
    }

    public static void logWarning(String message, Object... params) {
        log.log(Level.WARNING, String.format(message, params));
    }    

    public static void logSevere(String message, Object... params) {
        log.log(Level.SEVERE, String.format(message, params));
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        log = event.getModLog();
        config = new Configuration(event.getSuggestedConfigurationFile());
        config.addCustomCategoryComment("Addons", "Controls loading of different addons, set to false to disable");
        addons = new ArrayList<NEIAddon>();

        if (event.getSide() == Side.CLIENT && !Loader.isModLoaded("NotEnoughItems")) {
            logSevere("NEI doesn't seem to be installed... NEI Addons require it to do anything useful client-side");
        };
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        logInfo("Loading NEI Addons");
        for (NEIAddon addon : addons) {
            if (config.get("Addons", addon.getName(), true).getBoolean(false)) {
                logInfo("Loading %s Addon...",addon.getName());
                try {
                    addon.init(event.getSide());
                    if (addon.isActive()) {
                        logInfo("%s Addon successfully loadded",addon.getName());
                    }
                } catch (Exception e) {
                    logSevere("Loading %s Addon - Failed:",addon.getName());
                    e.printStackTrace();
                }
            } else {
                logInfo("%s Addon disabled - skipping",addon.getName());
            }
        }

        config.save();
        
        ServerHandler serverHandler = new ServerHandler();
        NetworkRegistry.instance().registerChannel(serverHandler, channel, Side.SERVER);
        GameRegistry.registerPlayerTracker(serverHandler);
        if (event.getSide() == Side.CLIENT) {
            ClientHandler clientHandler = new ClientHandler();
            NetworkRegistry.instance().registerChannel(clientHandler, channel, Side.CLIENT);
            TickRegistry.registerTickHandler(clientHandler, Side.CLIENT);
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
