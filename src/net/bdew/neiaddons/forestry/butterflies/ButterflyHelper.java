/*
 * Copyright (c) bdew, 2013 - 2015
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.neiaddons.forestry.butterflies;

import codechicken.nei.api.API;
import forestry.api.genetics.AlleleManager;
import forestry.api.lepidopterology.EnumFlutterType;
import forestry.api.lepidopterology.IAlleleButterflySpecies;
import forestry.api.lepidopterology.IButterflyRoot;
import net.bdew.neiaddons.Utils;
import net.bdew.neiaddons.forestry.AddonForestry;
import net.bdew.neiaddons.forestry.GeneticsUtils;

import java.util.Collection;

public class ButterflyHelper {

    public static Collection<IAlleleButterflySpecies> allSpecies;
//    public static Map<Integer, Collection<IAlleleSpecies>> productsCache;

    public static IButterflyRoot root;

//    private static void addProductToCache(int id, IAlleleBeeSpecies species) {
//        if (!productsCache.containsKey(id)) {
//            productsCache.put(id, new ArrayList<IAlleleSpecies>());
//        }
//        productsCache.get(id).add(species);
//    }

    public static void setup() {
        root = (IButterflyRoot) AlleleManager.alleleRegistry.getSpeciesRoot("rootButterflies");

        if (root == null) {
            AddonForestry.instance.logWarning("Butterfly Species Root not found, some functionality will be unavailable");
            return;
        }

        allSpecies = GeneticsUtils.getAllButterflySpecies(AddonForestry.loadBlacklisted);

        if (AddonForestry.showButterflyMutations) {
            ButterflyBreedingHandler breedingRecipeHandler = new ButterflyBreedingHandler();
            API.registerRecipeHandler(breedingRecipeHandler);
            API.registerUsageHandler(breedingRecipeHandler);
            AddonForestry.instance.registerWithNEIPlugins(breedingRecipeHandler.getRecipeName(), breedingRecipeHandler.getRecipeIdent());
        }

//        productsCache = new HashMap<Integer, Collection<IAlleleSpecies>>();

        for (IAlleleButterflySpecies species : allSpecies) {
            if (AddonForestry.addBees) {
                Utils.safeAddNBTItem(GeneticsUtils.stackFromSpecies(species, EnumFlutterType.BUTTERFLY.ordinal()));
                Utils.safeAddNBTItem(GeneticsUtils.stackFromSpecies(species, EnumFlutterType.CATERPILLAR.ordinal()));
                Utils.safeAddNBTItem(GeneticsUtils.stackFromSpecies(species, EnumFlutterType.SERUM.ordinal()));
            }
        }
    }
}
