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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import codechicken.nei.ItemRange;
import codechicken.nei.MultiItemRange;
import codechicken.nei.api.API;
import cpw.mods.fml.common.Loader;
import forestry.api.apiculture.EnumBeeType;
import forestry.api.apiculture.IAlleleBeeSpecies;
import forestry.api.apiculture.IBeeRoot;
import forestry.api.core.ItemInterface;
import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.IAlleleSpecies;

public class BeeHelper {
    private static BeeBreedingHandler breedingRecipeHandler;
    private static BeeProduceHandler productsRecipeHandler;

    public static Collection<IAlleleBeeSpecies> allSpecies;
    public static Map<Integer, Collection<IAlleleSpecies>> productsCache;

    public static IBeeRoot root;

    private static void addProductToCache(int id, IAlleleBeeSpecies species) {
        if (!productsCache.containsKey(id)) {
            productsCache.put(id, new ArrayList<IAlleleSpecies>());
        }
        productsCache.get(id).add(species);
    }

    private static void addHandlers() {
        breedingRecipeHandler = new BeeBreedingHandler();
        API.registerRecipeHandler(breedingRecipeHandler);
        API.registerUsageHandler(breedingRecipeHandler);
        AddonForestry.instance.registerWithNEIPlugins(breedingRecipeHandler.getRecipeName(), breedingRecipeHandler.getRecipeIdent());

        productsRecipeHandler = new BeeProduceHandler();
        API.registerRecipeHandler(productsRecipeHandler);
        API.registerUsageHandler(productsRecipeHandler);
        AddonForestry.instance.registerWithNEIPlugins(productsRecipeHandler.getRecipeName(), productsRecipeHandler.getRecipeIdent());
    }

    public static void setup() {
        root = (IBeeRoot) AlleleManager.alleleRegistry.getSpeciesRoot("rootBees");
        allSpecies = GeneticsUtils.getAllBeeSpecies(AddonForestry.loadBlacklisted);

        addHandlers();

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

        if (!Loader.isModLoaded("NEIPlugins")) {
            MultiItemRange queenRange = new MultiItemRange();
            queenRange.add(ItemInterface.getItem("beeQueenGE"));
            API.addSetRange("Forestry.Bees.Queens", queenRange);

            MultiItemRange princessRange = new MultiItemRange();
            princessRange.add(ItemInterface.getItem("beePrincessGE"));
            API.addSetRange("Forestry.Bees.Princesses", princessRange);

            MultiItemRange droneRange = new MultiItemRange();
            droneRange.add(ItemInterface.getItem("beeDroneGE"));
            API.addSetRange("Forestry.Bees.Drones", droneRange);

            MultiItemRange combRange = new MultiItemRange();
            combRange.add(new ItemRange(comb.itemID));
            addModCombs(combRange);
            API.addSetRange("Forestry.Bees.Combs", combRange);
        };
    }

    private static void addModCombs(MultiItemRange combs) {
        if (Loader.isModLoaded("ExtraBees")) {
            try {
                Class<?> ebItems = Class.forName("binnie.extrabees.ExtraBees");
                Object ebComb = ebItems.getField("comb").get(null);
                AddonForestry.instance.logInfo("Loaded EB comb item: %s", ebComb.toString());
                if (ebComb instanceof Item) {
                    ItemRange range = new ItemRange(((Item) ebComb).itemID);
                    AddonForestry.instance.logInfo("Registered EB combs: %s", range.toString());
                    combs.add(range);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        if (Loader.isModLoaded("MagicBees")) {
            try {
                Class<?> ebItems = Class.forName("magicbees.main.Config");
                Object tbComb = ebItems.getField("combs").get(null);
                AddonForestry.instance.logInfo("Loaded TB comb item: %s", tbComb.toString());
                if (tbComb instanceof Item) {
                    ItemRange range = new ItemRange(((Item) tbComb).itemID);
                    AddonForestry.instance.logInfo("Registered TB combs: %s", range.toString());
                    combs.add(range);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
}
