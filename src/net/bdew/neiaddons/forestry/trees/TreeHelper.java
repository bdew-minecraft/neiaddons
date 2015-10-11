/*
 * Copyright (c) bdew, 2013 - 2015
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.neiaddons.forestry.trees;

import codechicken.nei.api.API;
import forestry.api.arboriculture.EnumGermlingType;
import forestry.api.arboriculture.IAlleleTreeSpecies;
import forestry.api.arboriculture.ITreeRoot;
import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.IAlleleSpecies;
import net.bdew.neiaddons.Utils;
import net.bdew.neiaddons.forestry.AddonForestry;
import net.bdew.neiaddons.forestry.GeneticsUtils;
import net.bdew.neiaddons.forestry.MutationDumper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TreeHelper {
    public static Collection<IAlleleTreeSpecies> allSpecies;
    public static Map<Item, Collection<IAlleleSpecies>> productsCache = new HashMap<Item, Collection<IAlleleSpecies>>();

    public static ITreeRoot root;

    private static void addProductToCache(Item item, IAlleleTreeSpecies species) {
        if (!productsCache.containsKey(item)) {
            productsCache.put(item, new ArrayList<IAlleleSpecies>());
        }
        productsCache.get(item).add(species);
    }

    public static void setup() {
        root = (ITreeRoot) AlleleManager.alleleRegistry.getSpeciesRoot("rootTrees");

        if (root == null) {
            AddonForestry.instance.logWarning("Tree Species Root not found, some functionality will be unavailable");
            return;
        }

        allSpecies = GeneticsUtils.getAllTreeSpecies(AddonForestry.loadBlacklisted);

        if (AddonForestry.showTreeMutations) {
            TreeBreedingHandler breedingRecipeHandler = new TreeBreedingHandler();
            API.registerRecipeHandler(breedingRecipeHandler);
            API.registerUsageHandler(breedingRecipeHandler);
            AddonForestry.instance.registerWithNEIPlugins(breedingRecipeHandler.getRecipeName(), breedingRecipeHandler.getRecipeIdent());
        }

        if (AddonForestry.showTreeProducts) {
            TreeProduceHandler produceRecipeHandler = new TreeProduceHandler();
            API.registerRecipeHandler(produceRecipeHandler);
            API.registerUsageHandler(produceRecipeHandler);
            AddonForestry.instance.registerWithNEIPlugins(produceRecipeHandler.getRecipeName(), produceRecipeHandler.getRecipeIdent());
        }

        for (IAlleleTreeSpecies species : allSpecies) {
            if (AddonForestry.addSaplings) {
                Utils.safeAddNBTItem(GeneticsUtils.stackFromSpecies(species, EnumGermlingType.SAPLING.ordinal()));
            }
            if (AddonForestry.addPollen) {
                Utils.safeAddNBTItem(GeneticsUtils.stackFromSpecies(species, EnumGermlingType.POLLEN.ordinal()));
            }
            for (ItemStack prod : GeneticsUtils.getProduceFromSpecies(species).keySet()) {
                addProductToCache(prod.getItem(), species);
            }
            for (ItemStack prod : GeneticsUtils.getSpecialtyFromSpecies(species).keySet()) {
                addProductToCache(prod.getItem(), species);
            }
        }

        API.addOption(new MutationDumper(root, "tree_mutation"));
    }
}
