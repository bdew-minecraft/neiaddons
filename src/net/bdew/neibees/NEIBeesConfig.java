package net.bdew.neibees;

import java.util.Map.Entry;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import forestry.api.apiculture.EnumBeeType;
import forestry.api.apiculture.IAlleleBeeSpecies;
import forestry.api.apiculture.IBeeRoot;
import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.IAllele;

public class NEIBeesConfig implements IConfigureNEI {
    
    private BeeBreedingRecipeHandler beeBreedingRecipeHandler;
    private BeeProductsRecipeHandler beeProductsRecipeHandler;
    
    public static IBeeRoot beeRoot;
    
    @Override
    public void loadConfig() {
        beeRoot=(IBeeRoot)AlleleManager.alleleRegistry.getSpeciesRoot("rootBees");
        
        beeBreedingRecipeHandler = new BeeBreedingRecipeHandler();
        API.registerRecipeHandler(beeBreedingRecipeHandler);
        API.registerUsageHandler(beeBreedingRecipeHandler);

        beeProductsRecipeHandler = new BeeProductsRecipeHandler();
        API.registerRecipeHandler(beeProductsRecipeHandler);
        API.registerUsageHandler(beeProductsRecipeHandler);

        if (NeiBees.instance.addSearch) {
            for (Entry<String, IAllele> entry : AlleleManager.alleleRegistry.getRegisteredAlleles().entrySet()) {
                if (entry.getValue() instanceof IAlleleBeeSpecies) {

                    IAlleleBeeSpecies species = (IAlleleBeeSpecies) entry.getValue();

                    API.addNBTItem(Utils.stackFromAllele(species, EnumBeeType.QUEEN));
                    API.addNBTItem(Utils.stackFromAllele(species, EnumBeeType.DRONE));
                    API.addNBTItem(Utils.stackFromAllele(species, EnumBeeType.PRINCESS));
                }
            }
        }
    }

    @Override
    public String getName() {
        return "NEI Bees Plugin";
    }

    @Override
    public String getVersion() {
        return "@@VERSION@@";
    }

}
