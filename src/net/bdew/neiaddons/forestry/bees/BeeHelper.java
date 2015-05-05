/*
 * Copyright (c) bdew, 2013 - 2015
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.neiaddons.forestry.bees;

import codechicken.nei.api.API;
import codechicken.nei.config.ArrayDumper;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import forestry.api.apiculture.EnumBeeType;
import forestry.api.apiculture.IAlleleBeeSpecies;
import forestry.api.apiculture.IBeeRoot;
import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.IAlleleSpecies;
import forestry.api.genetics.IMutation;
import net.bdew.neiaddons.Utils;
import net.bdew.neiaddons.forestry.AddonForestry;
import net.bdew.neiaddons.forestry.ForestryOtherFilter;
import net.bdew.neiaddons.forestry.GeneticItemFilter;
import net.bdew.neiaddons.forestry.GeneticsUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

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
            
            // Add possibility to dump bee mutations
            API.addOption(new ArrayDumper<IMutation>("bdew.neiaddons.forestry.bee_mutations") {
				@Override
				public String[] header() {
					return new String[]{"UID", "Name", "Allele0", "Allele1", "isSecret", "baseChance", "conditions"};
				}
				
				@Override
				public String[] dump(IMutation obj, int id) {
					StringBuilder specialConditions = new StringBuilder();
					for (String specialCondition : obj.getSpecialConditions()) {
						if(specialConditions.length() > 0)
							specialConditions.append("|");
						specialConditions.append(specialCondition);
					}
					return new String[]{
							obj.getTemplate()[0].getUID(),
							obj.getTemplate()[0].getName(),
							obj.getAllele0().getUID(),
							obj.getAllele1().getUID(),
							Boolean.toString(obj.isSecret()),
							Float.toString(obj.getBaseChance()),
							specialConditions.toString()
					};
				}
				
				@Override
				public IMutation[] array() {
					return root.getMutations(false).toArray(new IMutation[0]);
				}
			});
        }

        if (AddonForestry.showBeeProducts) {
            BeeProduceHandler productsRecipeHandler = new BeeProduceHandler();
            API.registerRecipeHandler(productsRecipeHandler);
            API.registerUsageHandler(productsRecipeHandler);
            AddonForestry.instance.registerWithNEIPlugins(productsRecipeHandler.getRecipeName(), productsRecipeHandler.getRecipeIdent());
        }
    }

    public static void setup() {
        root = (IBeeRoot) AlleleManager.alleleRegistry.getSpeciesRoot("rootBees");

        if (root == null) {
            AddonForestry.instance.logWarning("Bee Species Root not found, some functionality will be unavailable");
            return;
        }

        allSpecies = GeneticsUtils.getAllBeeSpecies(AddonForestry.loadBlacklisted);

        addHandlers();

        HashMap<Item, HashSet<Integer>> seencombs = new HashMap<Item, HashSet<Integer>>();

        List<Item> modCombs = getMobCombs();
        for (Item combItem : modCombs) {
            seencombs.put(combItem, new HashSet<Integer>());
        }

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

        API.addSubset("Forestry.Bees.Princesses", new GeneticItemFilter(root, EnumBeeType.PRINCESS.ordinal(), true));
        API.addSubset("Forestry.Bees.Drones", new GeneticItemFilter(root, EnumBeeType.DRONE.ordinal(), true));
        API.addSubset("Forestry.Bees.Queens", new GeneticItemFilter(root, EnumBeeType.QUEEN.ordinal(), true));

        API.addSubset("Forestry.Bees.Combs", OreDictionary.getOres("beeComb"));
        API.addSubset("Forestry.Blocks", new ForestryOtherFilter(false));
        API.addSubset("Forestry.Items", new ForestryOtherFilter(true));
    }

    private static List<Item> getMobCombs() {
        List<Item> res = new ArrayList<Item>();

        Item vanillaComb = GameRegistry.findItem("Forestry", "beeCombs");

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
                    AddonForestry.instance.logInfo("Loaded Extra Bees comb item: %s (%s)", ebComb.toString(), ebComb);
                    res.add((Item) ebComb);
                } else {
                    AddonForestry.instance.logWarning("Extra Bees comb is not Item subclass!");
                }
            } catch (Throwable e) {
                AddonForestry.instance.logWarningExc(e, "Error locating Extra Bees comb item");
            }
        }

        if (Loader.isModLoaded("MagicBees")) {
            try {
                Class<?> mbConfig = Class.forName("magicbees.main.Config");
                Object mbComb = mbConfig.getField("combs").get(null);
                AddonForestry.instance.logInfo("Loaded TB comb item: %s", mbComb.toString());
                if (mbComb instanceof Item) {
                    AddonForestry.instance.logInfo("Loaded Magic Bees comb item: %s (%s)", mbComb.toString(), mbComb);
                    res.add((Item) mbComb);
                } else {
                    AddonForestry.instance.logWarning("Magic Bees comb is not Item subclass!");
                }
            } catch (Throwable e) {
                AddonForestry.instance.logWarningExc(e, "Error locating Magic Bees comb item");
            }
        }

        return res;
    }
}
