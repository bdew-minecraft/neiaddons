package net.bdew.neibees;

import java.util.logging.Logger;

import net.minecraftforge.common.Configuration;
import codechicken.nei.api.API;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = "neibees", version = "@@VERSION@@", name = "NEI Bees Plugin", dependencies = "required-after:Forestry;required-after:NotEnoughItems")
public class NeiBees {

    @Instance
    public static NeiBees instance;

    public static Logger log;
    public boolean showSecret;

    private BeeBreedingRecipeHandler beeBreedingRecipeHandler;
    private BeeProductsRecipeHandler beeProductsRecipeHandler;

    @PreInit
    public void preInit(FMLPreInitializationEvent event) {
        log = event.getModLog();

        Configuration config = new Configuration(event.getSuggestedConfigurationFile());

        showSecret = config.get(Configuration.CATEGORY_GENERAL, "Show Secret Mutations", false, "Set to true to show secret mutations").getBoolean(false);

        config.save();
    }

    @Init
    public void init(FMLInitializationEvent event) {
        // This is not the event we are looking for
    }

    @PostInit
    public void postInit(FMLPostInitializationEvent event) {
        if (FMLCommonHandler.instance().getSide().isClient()) {
            beeBreedingRecipeHandler = new BeeBreedingRecipeHandler();
            API.registerRecipeHandler(beeBreedingRecipeHandler);
            API.registerUsageHandler(beeBreedingRecipeHandler);

            beeProductsRecipeHandler = new BeeProductsRecipeHandler();
            API.registerRecipeHandler(beeProductsRecipeHandler);
            API.registerUsageHandler(beeProductsRecipeHandler);

            log.info("NEI Bees Plugin loaded");
        } else {
            log.warning("NEI Bees Plugin is client side only, do not install it on servers!");
        };
    }

}
