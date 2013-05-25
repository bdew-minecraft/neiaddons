package net.bdew.neiaddons.forestry;


public class TreeBreedingHandler extends BreedingRecipeHandler {

    public TreeBreedingHandler() {
        super(AddonForestry.treeRoot);
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
