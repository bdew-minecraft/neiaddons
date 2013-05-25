package net.bdew.neiaddons.forestry.trees;

import java.util.Collection;
import java.util.Map;

import net.bdew.neiaddons.forestry.BaseProduceRecipeHandler;
import forestry.api.arboriculture.IAlleleTreeSpecies;
import forestry.api.genetics.IAlleleSpecies;

public class TreeProduceHandler extends BaseProduceRecipeHandler {

    public TreeProduceHandler() {
        super(TreeHelper.root);
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
        return TreeHelper.allSpecies;
    }

    @Override
    public Map<Integer, Collection<IAlleleSpecies>> getProduceCache() {
        return TreeHelper.productsCache;
    }

}
