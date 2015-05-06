/*
 * Copyright (c) bdew, 2013 - 2015
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.neiaddons.botany.flowers;

import codechicken.nei.api.API;
import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.IAlleleSpecies;
import forestry.api.genetics.ISpeciesRoot;
import net.bdew.neiaddons.Utils;
import net.bdew.neiaddons.botany.AddonBotany;
import net.bdew.neiaddons.forestry.MutationDumper;

public class FlowerHelper {

    public static Class<? extends IAlleleSpecies> flowerSpeciesClass;

    public static ISpeciesRoot root;

    public static void setup() {
        root = AlleleManager.alleleRegistry.getSpeciesRoot("rootFlowers");

        if (root == null) {
            AddonBotany.instance.logWarning("Failed to load Flower root, Botany support disabled");
            return;
        }

        try {
            flowerSpeciesClass = Utils.getAndCheckClass("binnie.botany.api.IAlleleFlowerSpecies", IAlleleSpecies.class);
        } catch (Throwable e) {
            AddonBotany.instance.logWarningExc(e, "Failed to load Flower species interface, Botany support disabled");
            return;
        }

        AddonBotany.instance.logInfo("Root=%s Species=%s", root, flowerSpeciesClass);

        if (AddonBotany.showFlowerMutations) {
            FlowerBreedingHandler breedingRecipeHandler = new FlowerBreedingHandler();
            API.registerRecipeHandler(breedingRecipeHandler);
            API.registerUsageHandler(breedingRecipeHandler);
            AddonBotany.instance.registerWithNEIPlugins(breedingRecipeHandler.getRecipeName(), breedingRecipeHandler.getRecipeIdent());
        }

        API.addOption(new MutationDumper(root, "flower_mutation"));
    }
}
