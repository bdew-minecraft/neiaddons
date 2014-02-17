/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/neiaddons/master/MMPL-1.0.txt
 */

package net.bdew.neiaddons.forestry.trees;

import codechicken.nei.MultiItemRange;
import codechicken.nei.api.API;
import cpw.mods.fml.common.Loader;
import forestry.api.arboriculture.EnumGermlingType;
import forestry.api.arboriculture.IAlleleTreeSpecies;
import forestry.api.arboriculture.ITreeRoot;
import forestry.api.core.ItemInterface;
import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.IAlleleSpecies;
import net.bdew.neiaddons.NEIAddons;
import net.bdew.neiaddons.Utils;
import net.bdew.neiaddons.forestry.AddonForestry;
import net.bdew.neiaddons.forestry.GeneticsUtils;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TreeHelper {
    private static TreeBreedingHandler breedingRecipeHandler;
    private static TreeProduceHandler produceRecipeHandler;

    public static Collection<IAlleleTreeSpecies> allSpecies;
    public static Map<Integer, Collection<IAlleleSpecies>> productsCache;

    public static ITreeRoot root;

    private static void addProductToCache(int id, IAlleleTreeSpecies species) {
        if (!productsCache.containsKey(id)) {
            productsCache.put(id, new ArrayList<IAlleleSpecies>());
        }
        productsCache.get(id).add(species);
    }

    public static void setup() {
        root = (ITreeRoot) AlleleManager.alleleRegistry.getSpeciesRoot("rootTrees");
        allSpecies = GeneticsUtils.getAllTreeSpecies(AddonForestry.loadBlacklisted);

        if (AddonForestry.showTreeMutations) {
            breedingRecipeHandler = new TreeBreedingHandler();
            API.registerRecipeHandler(breedingRecipeHandler);
            API.registerUsageHandler(breedingRecipeHandler);
            AddonForestry.instance.registerWithNEIPlugins(breedingRecipeHandler.getRecipeName(), breedingRecipeHandler.getRecipeIdent());
        }

        if (AddonForestry.showTreeProducts) {
            produceRecipeHandler = new TreeProduceHandler();
            API.registerRecipeHandler(produceRecipeHandler);
            API.registerUsageHandler(produceRecipeHandler);
            AddonForestry.instance.registerWithNEIPlugins(produceRecipeHandler.getRecipeName(), produceRecipeHandler.getRecipeIdent());
        }

        productsCache = new HashMap<Integer, Collection<IAlleleSpecies>>();

        MultiItemRange fakeRange = new MultiItemRange();

        for (IAlleleTreeSpecies species : allSpecies) {
            if (AddonForestry.addSaplings && !NEIAddons.fakeItemsOn) {
                Utils.safeAddNBTItem(GeneticsUtils.stackFromSpecies(species, EnumGermlingType.SAPLING.ordinal()));
            }
            if (AddonForestry.addPollen) {
                if (NEIAddons.fakeItemsOn) {
                    ItemStack fake = NEIAddons.fakeItem.addItem(GeneticsUtils.stackFromSpecies(species, EnumGermlingType.POLLEN.ordinal()));
                    Utils.safeAddNBTItem(fake);
                    fakeRange.add(fake);
                } else {
                    Utils.safeAddNBTItem(GeneticsUtils.stackFromSpecies(species, EnumGermlingType.POLLEN.ordinal()));
                }
            }
            for (ItemStack prod : GeneticsUtils.getProduceFromSpecies(species).keySet()) {
                addProductToCache(prod.itemID, species);
            }
            for (ItemStack prod : GeneticsUtils.getSpecialtyFromSpecies(species).keySet()) {
                addProductToCache(prod.itemID, species);
            }
        }

        API.addToRange("Forestry.Trees.Pollen", fakeRange);

        if (!Loader.isModLoaded("NEIPlugins")) {
            MultiItemRange saplingRange = new MultiItemRange();
            saplingRange.add(ItemInterface.getItem("sapling"));
            API.addToRange("Forestry.Trees.Saplings", saplingRange);
        }

        MultiItemRange pollenRange = new MultiItemRange();
        pollenRange.add(ItemInterface.getItem("pollenFertile"));
        API.addToRange("Forestry.Trees.Pollen", pollenRange);
    }
}
