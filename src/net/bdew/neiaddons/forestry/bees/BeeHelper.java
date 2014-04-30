/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/neiaddons/master/MMPL-1.0.txt
 */

package net.bdew.neiaddons.forestry.bees;

import codechicken.nei.api.API;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import forestry.api.apiculture.EnumBeeType;
import forestry.api.apiculture.IAlleleBeeSpecies;
import forestry.api.apiculture.IBeeRoot;
import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.IAlleleSpecies;
import net.bdew.neiaddons.Utils;
import net.bdew.neiaddons.forestry.AddonForestry;
import net.bdew.neiaddons.forestry.GeneticsUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.*;

public class BeeHelper {

    public static Collection<IAlleleBeeSpecies> allSpecies;
    public static Map<Item, Collection<IAlleleSpecies>> productsCache = new HashMap<Item, Collection<IAlleleSpecies>>();

    public static IBeeRoot root;

    private static void addProductToCache(Item item, IAlleleBeeSpecies species) {
        if (!productsCache.containsKey(item)) {
            productsCache.put(item, new ArrayList<IAlleleSpecies>());
        }
        productsCache.get(item).add(species);
    }

    private static void addHandlers() {
        if (AddonForestry.showBeeMutations) {
            BeeBreedingHandler breedingRecipeHandler = new BeeBreedingHandler();
            API.registerRecipeHandler(breedingRecipeHandler);
            API.registerUsageHandler(breedingRecipeHandler);
            AddonForestry.instance.registerWithNEIPlugins(breedingRecipeHandler.getRecipeName(), breedingRecipeHandler.getRecipeIdent());
        }

        if (AddonForestry.showBeeProducts) {
            BeeProduceHandler productsRecipeHandler = new BeeProduceHandler();
            API.registerRecipeHandler(productsRecipeHandler);
            API.registerUsageHandler(productsRecipeHandler);
            AddonForestry.instance.registerWithNEIPlugins(productsRecipeHandler.getRecipeName(), productsRecipeHandler.getRecipeIdent());
        }
    }

    public static void setup() {
        // TODO: Check if needed after NEI re-adds ranges
        //API.getRangeTag("Forestry").saveTag = false;

        root = (IBeeRoot) AlleleManager.alleleRegistry.getSpeciesRoot("rootBees");
        allSpecies = GeneticsUtils.getAllBeeSpecies(AddonForestry.loadBlacklisted);

        addHandlers();

        HashMap<Item, HashSet<Integer>> seencombs = new HashMap<Item, HashSet<Integer>>();

        List<Item> modCombs = getMobCombs();
        for (Item combItem : modCombs) {
            seencombs.put(combItem, new HashSet<Integer>());
        }

        // TODO: fix after NEI re-adds ranges
        //MultiItemRange fakeRange = new MultiItemRange();

        for (IAlleleBeeSpecies species : allSpecies) {
            if (AddonForestry.addBees) {
                Utils.safeAddNBTItem(GeneticsUtils.stackFromSpecies(species, EnumBeeType.QUEEN.ordinal()));
                Utils.safeAddNBTItem(GeneticsUtils.stackFromSpecies(species, EnumBeeType.DRONE.ordinal()));
                Utils.safeAddNBTItem(GeneticsUtils.stackFromSpecies(species, EnumBeeType.PRINCESS.ordinal()));
            }
            for (ItemStack prod : GeneticsUtils.getProduceFromSpecies(species).keySet()) {
                addProductToCache(prod.getItem(), species);
                if (AddonForestry.addCombs && seencombs.containsKey(prod.getItem())) {
                    seencombs.get(prod.getItem()).add(prod.getItemDamage());
                }
            }
            for (ItemStack prod : GeneticsUtils.getSpecialtyFromSpecies(species).keySet()) {
                addProductToCache(prod.getItem(), species);
                if (AddonForestry.addCombs && seencombs.containsKey(prod.getItem())) {
                    seencombs.get(prod.getItem()).add(prod.getItemDamage());
                }
            }
        }

        if (AddonForestry.addCombs) {
            for (Item combItem : modCombs) {
                HashSet<Integer> subitems = seencombs.get(combItem);

                ArrayList<ItemStack> combs = new ArrayList<ItemStack>();
                combItem.getSubItems(combItem, null, combs);

                for (ItemStack item : combs) {
                    subitems.add(item.getItemDamage());
                    AddonForestry.instance.logInfo("Registering comb variant for %s: %s", combItem.getClass().getName(), item.toString());
                    API.addItemListEntry(item);
                }
            }
        }

        // TODO: fix after NEI re-adds ranges
        /*
        API.addToRange("Forestry.Bees.Princesses", fakeRange);

        if (!Loader.isModLoaded("NEIPlugins")) {
            MultiItemRange queenRange = new MultiItemRange();
            queenRange.add(ItemInterface.getItem("beeQueenGE"));
            API.addToRange("Forestry.Bees.Queens", queenRange);

            MultiItemRange princessRange = new MultiItemRange();
            princessRange.add(ItemInterface.getItem("beePrincessGE"));
            API.addToRange("Forestry.Bees.Princesses", princessRange);

            MultiItemRange droneRange = new MultiItemRange();
            droneRange.add(ItemInterface.getItem("beeDroneGE"));
            API.addToRange("Forestry.Bees.Drones", droneRange);

            MultiItemRange combRange = new MultiItemRange();
            for (Item i : modCombs) {
                combRange.add(i.getItem());
            }
            API.addToRange("Forestry.Bees.Combs", combRange);
        }
        */
    }

    private static List<Item> getMobCombs() {
        List<Item> res = new ArrayList<Item>();

        Item vanillaComb = GameRegistry.findItem("forestry", "beeComb");

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
                    AddonForestry.instance.logInfo("Loaded Extra Bees comb item: %s (%d)", ebComb.toString(), ebComb);
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
                    AddonForestry.instance.logInfo("Loaded Magic Bees comb item: %s (%d)", mbComb.toString(), mbComb);
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
