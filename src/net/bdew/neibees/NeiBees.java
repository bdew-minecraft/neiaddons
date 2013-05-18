/**
 * Copyright (c) bdew, 2013
 * https://github.com/bdew/neibees
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/neibees/master/MMPL-1.0.txt
 */

package net.bdew.neibees;

import java.util.logging.Logger;

import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = "neibees", version = "@@VERSION@@", name = "NEI Bees Plugin", dependencies = "required-after:Forestry;required-after:NotEnoughItems")
public class NeiBees {

    @Instance
    public static NeiBees instance;

    public static Logger log;

    public boolean showSecret;
    public boolean addSearch;

    @PreInit
    public void preInit(FMLPreInitializationEvent event) {
        log = event.getModLog();

        Configuration config = new Configuration(event.getSuggestedConfigurationFile());

        showSecret = config.get(Configuration.CATEGORY_GENERAL, "Show Secret Mutations", false, "Set to true to show secret mutations").getBoolean(false);
        addSearch = config.get(Configuration.CATEGORY_GENERAL, "Add Bees to Search", true, "Set to true to add ALL bees to NEI search").getBoolean(true);

        config.save();
    }

    @Init
    public void init(FMLInitializationEvent event) {
        // This is not the event we are looking for
    }

    @PostInit
    public void postInit(FMLPostInitializationEvent event) {
        if (FMLCommonHandler.instance().getSide().isClient()) {
            FMLInterModComms.sendRuntimeMessage(instance,"NEIPlugins","register-crafting-handler","Forestry Bees@Bee Products@beeproducts");
            FMLInterModComms.sendRuntimeMessage(instance,"NEIPlugins","register-crafting-handler","Forestry Bees@Bee Breeding@beebreeding");
            log.info("NEI Bees Plugin loaded");
        } else {
            log.warning("NEI Bees Plugin is client side only, do not install it on servers!");
        };
    }
}
