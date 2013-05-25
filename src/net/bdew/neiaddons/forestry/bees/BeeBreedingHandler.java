package net.bdew.neiaddons.forestry.bees;

import net.bdew.neiaddons.forestry.BaseBreedingRecipeHandler;


public class BeeBreedingHandler extends BaseBreedingRecipeHandler {

    public BeeBreedingHandler() {
        super(BeeHelper.root);
    }

    @Override
    public String getRecipeName() {
        return "Bee Breeding";
    }

    @Override
    public String getRecipeIdent() {
        return "beebreeding";
    }

}
