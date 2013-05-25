/**
 * Copyright (c) bdew, 2013
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/neiaddons/master/MMPL-1.0.txt
 */

package net.bdew.neiaddons.forestry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import net.bdew.neiaddons.BaseAddon;
import net.bdew.neiaddons.NEIAddons;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import codechicken.nei.api.API;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import forestry.api.apiculture.EnumBeeType;
import forestry.api.apiculture.IAlleleBeeSpecies;
import forestry.api.apiculture.IBeeRoot;
import forestry.api.arboriculture.ITreeRoot;
import forestry.api.core.ItemInterface;
import forestry.api.genetics.AlleleManager;

@Mod(modid = NEIAddons.modid + "|Forestry", name = "NEI Addons: Forestry", version = "@@VERSION@@", dependencies = "after:NEIAddons;after:Forestry")
public class AddonForestry extends BaseAddon {
    private BreedingRecipeHandler beeBreedingRecipeHandler,treeBreedingRecipeHandler;
    private BeeProductsRecipeHandler beeProductsRecipeHandler;

    public static IBeeRoot beeRoot;
    public static ITreeRoot treeRoot;

    public static Collection<IAlleleBeeSpecies> allBeeSpecies;
    
    public static boolean showSecret;
    public static boolean addBees;
    public static boolean addCombs;
    public static boolean loadBlacklisted;

    public static Map<Integer, Collection<IAlleleBeeSpecies>> productsCache;

    @Instance(NEIAddons.modid + "|Forestry")
    public static AddonForestry instance;

    @Override
    public String getName() {
        return "Forestry";
    }

    @Override
    public boolean checkSide(Side side) {
        return side.isClient();
    }

    @Override
    public String[] getDependencies() {
        return new String[]{"Forestry@[2.2.4.0,)"};
    }

    @PreInit
    public void preInit(FMLPreInitializationEvent ev) {
        doPreInit(ev);
    }

    @Override
    public void init(Side side) throws Exception {
        showSecret = NEIAddons.config.get(getName(), "Show Secret Mutations", false, "Set to true to show secret mutations").getBoolean(false);
        addBees = NEIAddons.config.get(getName(), "Add Bees to Search", true, "Set to true to add all bees to NEI search").getBoolean(false);
        addCombs = NEIAddons.config.get(getName(), "Add Combs to Search", false, "Set to true to add all combs that are produced by bees to NEI search").getBoolean(false);
        loadBlacklisted = NEIAddons.config.get(getName(), "Load blacklisted", false, "Set to true to load blacklisted species and alleles, it's dangerous and (mostly) useless").getBoolean(false);
        active = true;
    }

    private void addProductToCache(int id, IAlleleBeeSpecies species) {
        if (!productsCache.containsKey(id)) {
            productsCache.put(id, new ArrayList<IAlleleBeeSpecies>());
        }
        productsCache.get(id).add(species);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void loadClient() {
        beeRoot = (IBeeRoot) AlleleManager.alleleRegistry.getSpeciesRoot("rootBees");
        treeRoot = (ITreeRoot) AlleleManager.alleleRegistry.getSpeciesRoot("rootTrees");

        beeBreedingRecipeHandler = new BeeBreedingHandler();
        API.registerRecipeHandler(beeBreedingRecipeHandler);
        API.registerUsageHandler(beeBreedingRecipeHandler);

        treeBreedingRecipeHandler = new TreeBreedingHandler();
        API.registerRecipeHandler(treeBreedingRecipeHandler);
        API.registerUsageHandler(treeBreedingRecipeHandler);
        
        beeProductsRecipeHandler = new BeeProductsRecipeHandler();
        API.registerRecipeHandler(beeProductsRecipeHandler);
        API.registerUsageHandler(beeProductsRecipeHandler);

        allBeeSpecies = GeneticsUtils.getAllBeeSpecies(loadBlacklisted);
        
        Item comb = ItemInterface.getItem("beeComb").getItem();
        HashSet<Integer> seencombs = new HashSet<Integer>();

        productsCache = new HashMap<Integer, Collection<IAlleleBeeSpecies>>();

        for (IAlleleBeeSpecies species : allBeeSpecies) {
            if (addBees) {
                API.addNBTItem(GeneticsUtils.stackFromAllele(species, EnumBeeType.QUEEN));
                API.addNBTItem(GeneticsUtils.stackFromAllele(species, EnumBeeType.DRONE));
                API.addNBTItem(GeneticsUtils.stackFromAllele(species, EnumBeeType.PRINCESS));
            }
            for (ItemStack prod : species.getProducts().keySet()) {
                addProductToCache(prod.itemID, species);
                if (addCombs && (prod.itemID == comb.itemID)) {
                    seencombs.add(prod.getItemDamage());
                }
            }
            for (ItemStack prod : species.getSpecialty().keySet()) {
                addProductToCache(prod.itemID, species);
                if (addCombs && (prod.itemID == comb.itemID)) {
                    seencombs.add(prod.getItemDamage());
                }
            }
        }

        if (addCombs) {
            ArrayList<ItemStack> combs = new ArrayList<ItemStack>();
            comb.getSubItems(comb.itemID, null, combs);

            for (ItemStack item : combs) {
                seencombs.add(item.getItemDamage());
            }

            API.setItemDamageVariants(comb.itemID, seencombs);
        }

        FMLInterModComms.sendRuntimeMessage(this, "NEIPlugins", "register-crafting-handler", "Forestry Bees@Bee Products@beeproducts");
        FMLInterModComms.sendRuntimeMessage(this, "NEIPlugins", "register-crafting-handler", "Forestry Bees@Bee Breeding@beebreeding");
    }
}
