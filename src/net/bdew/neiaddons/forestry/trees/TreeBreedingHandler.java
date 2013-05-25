package net.bdew.neiaddons.forestry.trees;

import net.bdew.neiaddons.forestry.BaseBreedingRecipeHandler;


public class TreeBreedingHandler extends BaseBreedingRecipeHandler {

    public TreeBreedingHandler() {
        super(TreeHelper.root);
    }

    @Override
    public String getRecipeName() {
        return "Tree Breeding";
    }

    @Override
    public String getRecipeIdent() {
        return "treebreeding";
    }


}
