/**
 * Copyright (c) bdew, 2013
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/neiaddons/master/MMPL-1.0.txt
 */

package net.bdew.neiaddons.forestry.bees;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import net.bdew.neiaddons.Utils;
import net.bdew.neiaddons.forestry.AddonForestry;
import net.bdew.neiaddons.forestry.GeneticsUtils;
import net.bdew.neiaddons.forestry.fake.FakeBeeRoot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import codechicken.nei.api.API;
import forestry.api.apiculture.EnumBeeType;
import forestry.api.apiculture.IAlleleBeeSpecies;
import forestry.api.core.ItemInterface;
import forestry.api.genetics.IAlleleSpecies;

public class BeeHelper {
    private static BeeBreedingHandler breedingRecipeHandler;
    private static BeeProduceHandler productsRecipeHandler;

    public static Collection<IAlleleBeeSpecies> allSpecies;
    public static Map<Integer, Collection<IAlleleSpecies>> productsCache;

    public static FakeBeeRoot root;

    private static void addProductToCache(int id, IAlleleBeeSpecies species) {
        if (!productsCache.containsKey(id)) {
            productsCache.put(id, new ArrayList<IAlleleSpecies>());
        }
        productsCache.get(id).add(species);
    }

    public static void setup() {
        root = new FakeBeeRoot();
        allSpecies = GeneticsUtils.getAllBeeSpecies(AddonForestry.loadBlacklisted);

        breedingRecipeHandler = new BeeBreedingHandler();
        API.registerRecipeHandler(breedingRecipeHandler);
        API.registerUsageHandler(breedingRecipeHandler);
        AddonForestry.instance.registerWithNEIPlugins(breedingRecipeHandler.getRecipeName(), breedingRecipeHandler.getRecipeIdent());

        productsRecipeHandler = new BeeProduceHandler();
        API.registerRecipeHandler(productsRecipeHandler);
        API.registerUsageHandler(productsRecipeHandler);
        AddonForestry.instance.registerWithNEIPlugins(productsRecipeHandler.getRecipeName(), productsRecipeHandler.getRecipeIdent());

        Item comb = ItemInterface.getItem("beeComb").getItem();
        HashSet<Integer> seencombs = new HashSet<Integer>();

        productsCache = new HashMap<Integer, Collection<IAlleleSpecies>>();

        for (IAlleleBeeSpecies species : allSpecies) {
            if (AddonForestry.addBees) {
                Utils.safeAddNBTItem(GeneticsUtils.stackFromSpecies(species, EnumBeeType.QUEEN.ordinal()));
                Utils.safeAddNBTItem(GeneticsUtils.stackFromSpecies(species, EnumBeeType.DRONE.ordinal()));
                Utils.safeAddNBTItem(GeneticsUtils.stackFromSpecies(species, EnumBeeType.PRINCESS.ordinal()));
            }
            for (ItemStack prod : GeneticsUtils.getProduceFromSpecies(species).keySet()) {
                addProductToCache(prod.itemID, species);
                if (AddonForestry.addCombs && (prod.itemID == comb.itemID)) {
                    seencombs.add(prod.getItemDamage());
                }
            }
            for (ItemStack prod : GeneticsUtils.getSpecialtyFromSpecies(species).keySet()) {
                addProductToCache(prod.itemID, species);
                if (AddonForestry.addCombs && (prod.itemID == comb.itemID)) {
                    seencombs.add(prod.getItemDamage());
                }
            }
        }

        if (AddonForestry.addCombs) {
            ArrayList<ItemStack> combs = new ArrayList<ItemStack>();
            comb.getSubItems(comb.itemID, null, combs);

            for (ItemStack item : combs) {
                seencombs.add(item.getItemDamage());
            }

            API.setItemDamageVariants(comb.itemID, seencombs);
        }
    }
}
