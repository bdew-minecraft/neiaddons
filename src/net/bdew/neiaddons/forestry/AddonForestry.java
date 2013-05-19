package net.bdew.neiaddons.forestry;

import java.util.Map.Entry;

import net.bdew.neiaddons.NEIAddons;
import net.bdew.neiaddons.api.NEIAddon;
import codechicken.nei.api.API;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.relauncher.Side;
import forestry.api.apiculture.EnumBeeType;
import forestry.api.apiculture.IAlleleBeeSpecies;
import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.IAllele;

public class AddonForestry implements NEIAddon {
    private Boolean active = false;
    private BeeBreedingRecipeHandler beeBreedingRecipeHandler;
    private BeeProductsRecipeHandler beeProductsRecipeHandler;

    public static boolean showSecret;
    public static boolean addSearch;

    @Override
    public String getName() {
        return "Forestry";
    }

    @Override
    public Boolean isActive() {
        return active ;
    }

    @Override
    public void init(Side side) throws Exception {
        if (!Loader.isModLoaded("Forestry")) {
            NEIAddons.log.info("Forestry is not installed, skipping");
            return;
        }

        if (side==Side.CLIENT) {
            
            showSecret = NEIAddons.config.get(getName(), "Show Secret Mutations", false, "Set to true to show secret mutations").getBoolean(false);
            addSearch = NEIAddons.config.get(getName(), "Add Bees to Search", true, "Set to true to add ALL bees to NEI search").getBoolean(true);
            
            active = true;
        } else {
            NEIAddons.log.info("Forestry Addon is client-side only, skipping");
        }
    }

    @Override
    public void loadClient() {
        beeBreedingRecipeHandler = new BeeBreedingRecipeHandler();
        API.registerRecipeHandler(beeBreedingRecipeHandler);
        API.registerUsageHandler(beeBreedingRecipeHandler);

        beeProductsRecipeHandler = new BeeProductsRecipeHandler();
        API.registerRecipeHandler(beeProductsRecipeHandler);
        API.registerUsageHandler(beeProductsRecipeHandler);

        if (addSearch) {
            for (Entry<String, IAllele> entry : AlleleManager.alleleRegistry.getRegisteredAlleles().entrySet()) {
                if (entry.getValue() instanceof IAlleleBeeSpecies) {

                    IAlleleBeeSpecies species = (IAlleleBeeSpecies) entry.getValue();

                    API.addNBTItem(BeeUtils.stackFromAllele(species, EnumBeeType.QUEEN));
                    API.addNBTItem(BeeUtils.stackFromAllele(species, EnumBeeType.DRONE));
                    API.addNBTItem(BeeUtils.stackFromAllele(species, EnumBeeType.PRINCESS));
                }
            }
        }
        
        FMLInterModComms.sendRuntimeMessage(NEIAddons.instance,"NEIPlugins","register-crafting-handler","Forestry Bees@Bee Products@beeproducts");
        FMLInterModComms.sendRuntimeMessage(NEIAddons.instance,"NEIPlugins","register-crafting-handler","Forestry Bees@Bee Breeding@beebreeding");
    }

}
