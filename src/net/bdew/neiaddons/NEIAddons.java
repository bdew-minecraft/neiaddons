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
import java.util.logging.Logger;

import net.bdew.neiaddons.api.NEIAddon;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = NEIAddons.modid, name = "NEI Addons", version = "@@VERSION@@", dependencies = "after:NotEnoughItems;after:AppliedEnergistics;after:EE3;after:Forestry")
public class NEIAddons {
    public static Logger log;
    public static final String modid = "NEIAddons";

    public static List<NEIAddon> addons;
    public static Configuration config;

    public static void register(NEIAddon addon) {
        addons.add(addon);
    }
    
    @PreInit
    public void preInit(FMLPreInitializationEvent event) {
        log = event.getModLog();
        config = new Configuration(event.getSuggestedConfigurationFile());
        config.addCustomCategoryComment("Addons", "Controls loading of different addons, set to false to disable");
        addons = new ArrayList<NEIAddon>();

        if ((event.getSide()==Side.CLIENT) && !Loader.isModLoaded("NotEnoughItems")) {
           log.severe("NEI doesn't seem to be installed... NEI Addons require it to do anything useful client-side");
        };
    }
    
    @Init
    public void init(FMLInitializationEvent event) {
        log.info("Loading NEI Addons");
        for (NEIAddon addon : addons) {
            if (config.get("Addons", addon.getName(), true).getBoolean(false)) {
                log.info("Loading " + addon.getName() + " Addon...");
                try {
                    addon.init(event.getSide());
                    if (addon.isActive()) log.info(addon.getName() + " Addon successfully loadded");
                } catch (Exception e) {
                    log.severe("Loading " + addon.getName() + " Addon - Failed:");
                    e.printStackTrace();
                }
            } else {
                log.info(addon.getName() + " Addon disabled - skipping");
            }
        }
        config.save();
    }

}
