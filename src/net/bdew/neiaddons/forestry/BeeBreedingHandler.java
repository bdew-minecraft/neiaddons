package net.bdew.neiaddons.forestry;


public class BeeBreedingHandler extends BreedingRecipeHandler {

    public BeeBreedingHandler() {
        super(AddonForestry.beeRoot);
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
