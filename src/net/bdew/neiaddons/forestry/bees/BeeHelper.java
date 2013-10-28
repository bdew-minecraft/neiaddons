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
import java.util.List;
import java.util.Map;

import net.bdew.neiaddons.NEIAddons;
import net.bdew.neiaddons.Utils;
import net.bdew.neiaddons.forestry.AddonForestry;
import net.bdew.neiaddons.forestry.GeneticsUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
        if (AddonForestry.showBeeMutations) {
            breedingRecipeHandler = new BeeBreedingHandler();
            API.registerRecipeHandler(breedingRecipeHandler);
            API.registerUsageHandler(breedingRecipeHandler);
            AddonForestry.instance.registerWithNEIPlugins(breedingRecipeHandler.getRecipeName(), breedingRecipeHandler.getRecipeIdent());
        }

        if (AddonForestry.showBeeProducts) {
            productsRecipeHandler = new BeeProduceHandler();
            API.registerRecipeHandler(productsRecipeHandler);
            API.registerUsageHandler(productsRecipeHandler);
            AddonForestry.instance.registerWithNEIPlugins(productsRecipeHandler.getRecipeName(), productsRecipeHandler.getRecipeIdent());
        }
    }

    public static void setup() {
        root = (IBeeRoot) AlleleManager.alleleRegistry.getSpeciesRoot("rootBees");
        allSpecies = GeneticsUtils.getAllBeeSpecies(AddonForestry.loadBlacklisted);

        addHandlers();

        HashMap<Integer, HashSet<Integer>> seencombs = new HashMap<Integer, HashSet<Integer>>();

        List<Item> modCombs = getMobCombs();
        for (Item combItem : modCombs) {
            seencombs.put(combItem.itemID, new HashSet<Integer>());
        }

        productsCache = new HashMap<Integer, Collection<IAlleleSpecies>>();

        for (IAlleleBeeSpecies species : allSpecies) {
            if (AddonForestry.addBees) {
                if (NEIAddons.fakeItemsOn) {
                    Utils.safeAddNBTItem(NEIAddons.fakeItem.addItem(GeneticsUtils.stackFromSpecies(species, EnumBeeType.PRINCESS.ordinal())));
                } else {
                    Utils.safeAddNBTItem(GeneticsUtils.stackFromSpecies(species, EnumBeeType.QUEEN.ordinal()));
                    Utils.safeAddNBTItem(GeneticsUtils.stackFromSpecies(species, EnumBeeType.DRONE.ordinal()));
                    Utils.safeAddNBTItem(GeneticsUtils.stackFromSpecies(species, EnumBeeType.PRINCESS.ordinal()));
                }
            }
            for (ItemStack prod : GeneticsUtils.getProduceFromSpecies(species).keySet()) {
                addProductToCache(prod.itemID, species);
                if (AddonForestry.addCombs && seencombs.containsKey(prod.itemID)) {
                    seencombs.get(prod.itemID).add(prod.getItemDamage());
                }
            }
            for (ItemStack prod : GeneticsUtils.getSpecialtyFromSpecies(species).keySet()) {
                addProductToCache(prod.itemID, species);
                if (AddonForestry.addCombs && seencombs.containsKey(prod.itemID)) {
                    seencombs.get(prod.itemID).add(prod.getItemDamage());
                }
            }
        }

        if (AddonForestry.addCombs) {
            for (Item combItem : modCombs) {
                HashSet<Integer> subitems = seencombs.get(combItem.itemID);

                ArrayList<ItemStack> combs = new ArrayList<ItemStack>();
                combItem.getSubItems(combItem.itemID, null, combs);

                for (ItemStack item : combs) {
                    subitems.add(item.getItemDamage());
                }

                AddonForestry.instance.logInfo("Registering variants for %s: %s", combItem.getClass().getName(), subitems.toString());

                API.setItemDamageVariants(combItem.itemID, subitems);

            }
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
            for (Item i : modCombs) {
                combRange.add(i.itemID);
            }
            ;
            API.addSetRange("Forestry.Bees.Combs", combRange);
        }
        ;
    }

    private static List<Item> getMobCombs() {
        List<Item> res = new ArrayList<Item>();

        Item vanillaComb = ItemInterface.getItem("beeComb").getItem();

        if (vanillaComb == null) {
            AddonForestry.instance.logWarning("Failed to get forestry bee comb item, something is messed up");
        } else {
            res.add(vanillaComb);
        }

        if (Loader.isModLoaded("ExtraBees")) {
            try {
                Class<?> ebItems = Class.forName("binnie.extrabees.ExtraBees");
                Object ebComb = ebItems.getField("comb").get(null);
                if (ebComb instanceof Item) {
                    AddonForestry.instance.logInfo("Loaded Extra Bees comb item: %s (%d)", ebComb.toString(), ((Item) ebComb).itemID);
                    res.add((Item) ebComb);
                } else {
                    AddonForestry.instance.logWarning("Extra Bees comb is not Item subclass!");
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        if (Loader.isModLoaded("MagicBees")) {
            try {
                Class<?> mbConfig = Class.forName("magicbees.main.Config");
                Object mbComb = mbConfig.getField("combs").get(null);
                AddonForestry.instance.logInfo("Loaded TB comb item: %s", mbComb.toString());
                if (mbComb instanceof Item) {
                    AddonForestry.instance.logInfo("Loaded Magic Bees comb item: %s (%d)", mbComb.toString(), ((Item) mbComb).itemID);
                    res.add((Item) mbComb);
                } else {
                    AddonForestry.instance.logWarning("Magic Bees comb is not Item subclass!");
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        return res;
    }
}
