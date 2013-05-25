package net.bdew.neiaddons.forestry.bees;

import java.util.Collection;
import java.util.Map;

import net.bdew.neiaddons.forestry.AddonForestry;
import net.bdew.neiaddons.forestry.ProduceRecipeHandler;
import forestry.api.apiculture.IAlleleBeeSpecies;
import forestry.api.genetics.IAlleleSpecies;

public class BeeProduceHandler extends ProduceRecipeHandler {

    public BeeProduceHandler() {
        super(AddonForestry.beeRoot);
    }

    @Override
    public String getRecipeName() {
        return "Bee Produce";
    }

    @Override
    public String getRecipeIdent() {
        return "beeproduce";
    }

    @Override
    public Collection<IAlleleBeeSpecies> getAllSpecies() {
        return AddonForestry.allBeeSpecies;
    }

    @Override
    public Map<Integer, Collection<IAlleleSpecies>> getProduceCache() {
        return AddonForestry.beeProductsCache;
    }

}
