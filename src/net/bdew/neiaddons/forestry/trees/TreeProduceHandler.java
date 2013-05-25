package net.bdew.neiaddons.forestry.trees;

import java.util.Collection;
import java.util.Map;

import net.bdew.neiaddons.forestry.AddonForestry;
import net.bdew.neiaddons.forestry.ProduceRecipeHandler;
import forestry.api.arboriculture.IAlleleTreeSpecies;
import forestry.api.genetics.IAlleleSpecies;

public class TreeProduceHandler extends ProduceRecipeHandler {

    public TreeProduceHandler() {
        super(AddonForestry.treeRoot);
    }

    @Override
    public String getRecipeName() {
        return "Tree Produce";
    }

    @Override
    public String getRecipeIdent() {
        return "treeproduce";
    }

    @Override
    public Collection<IAlleleTreeSpecies> getAllSpecies() {
        return AddonForestry.allTreeSpecies;
    }

    @Override
    public Map<Integer, Collection<IAlleleSpecies>> getProduceCache() {
        return AddonForestry.treeProductsCache;
    }

}
