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
import net.bdew.neiaddons.forestry.bees.BeeProduceHandler;
import net.bdew.neiaddons.forestry.trees.TreeProduceHandler;
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
import forestry.api.arboriculture.EnumGermlingType;
import forestry.api.arboriculture.IAlleleTreeSpecies;
import forestry.api.arboriculture.ITreeRoot;
import forestry.api.core.ItemInterface;
import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.IAlleleSpecies;

@Mod(modid = NEIAddons.modid + "|Forestry", name = "NEI Addons: Forestry", version = "@@VERSION@@", dependencies = "after:NEIAddons;after:Forestry")
public class AddonForestry extends BaseAddon {
    private BreedingRecipeHandler beeBreedingRecipeHandler,treeBreedingRecipeHandler;
    private ProduceRecipeHandler beeProductsRecipeHandler,treeProduceRecipeHandler;

    public static IBeeRoot beeRoot;
    public static ITreeRoot treeRoot;

    public static Collection<IAlleleBeeSpecies> allBeeSpecies;
    public static Collection<IAlleleTreeSpecies> allTreeSpecies;

    public static Map<Integer, Collection<IAlleleSpecies>> beeProductsCache;
    public static Map<Integer, Collection<IAlleleSpecies>> treeProductsCache;
    
    public static boolean showSecret;
    public static boolean addBees;
    public static boolean addCombs;
    public static boolean addSaplings;
    public static boolean addPollen;
    public static boolean loadBlacklisted;

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
        addSaplings = NEIAddons.config.get(getName(), "Add Saplings to Search", true, "Set to true to add all saplings to NEI search").getBoolean(false);
        addPollen = NEIAddons.config.get(getName(), "Add Pollen to Search", true, "Set to true to add all pollen types to NEI search").getBoolean(false);
        loadBlacklisted = NEIAddons.config.get(getName(), "Load blacklisted", false, "Set to true to load blacklisted species and alleles, it's dangerous and (mostly) useless").getBoolean(false);
        active = true;
    }

    private void addProductToBeeCache(int id, IAlleleBeeSpecies species) {
        if (!beeProductsCache.containsKey(id)) {
            beeProductsCache.put(id, new ArrayList<IAlleleSpecies>());
        }
        beeProductsCache.get(id).add(species);
    }

    private void addProductToTreeCache(int id, IAlleleTreeSpecies species) {
        if (!treeProductsCache.containsKey(id)) {
            treeProductsCache.put(id, new ArrayList<IAlleleSpecies>());
        }
        treeProductsCache.get(id).add(species);
    }
    
    public void setupBees() {
        beeRoot = (IBeeRoot) AlleleManager.alleleRegistry.getSpeciesRoot("rootBees");
        allBeeSpecies = GeneticsUtils.getAllBeeSpecies(loadBlacklisted);
        
        beeBreedingRecipeHandler = new BeeBreedingHandler();
        API.registerRecipeHandler(beeBreedingRecipeHandler);
        API.registerUsageHandler(beeBreedingRecipeHandler);

        beeProductsRecipeHandler = new BeeProduceHandler();
        API.registerRecipeHandler(beeProductsRecipeHandler);
        API.registerUsageHandler(beeProductsRecipeHandler);

        Item comb = ItemInterface.getItem("beeComb").getItem();
        HashSet<Integer> seencombs = new HashSet<Integer>();

        beeProductsCache = new HashMap<Integer, Collection<IAlleleSpecies>>();

        for (IAlleleBeeSpecies species : allBeeSpecies) {
            if (addBees) {
                API.addNBTItem(GeneticsUtils.stackFromSpecies(species, EnumBeeType.QUEEN.ordinal()));
                API.addNBTItem(GeneticsUtils.stackFromSpecies(species, EnumBeeType.DRONE.ordinal()));
                API.addNBTItem(GeneticsUtils.stackFromSpecies(species, EnumBeeType.PRINCESS.ordinal()));
            }
            for (ItemStack prod : GeneticsUtils.getProduceFromSpecies(species).keySet()) {
                addProductToBeeCache(prod.itemID, species);
                if (addCombs && (prod.itemID == comb.itemID)) {
                    seencombs.add(prod.getItemDamage());
                }
            }
            for (ItemStack prod : GeneticsUtils.getSpecialtyFromSpecies(species).keySet()) {
                addProductToBeeCache(prod.itemID, species);
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

    }
    
    public void setupTrees() {
        treeRoot = (ITreeRoot) AlleleManager.alleleRegistry.getSpeciesRoot("rootTrees");
        allTreeSpecies = GeneticsUtils.getAllTreeSpecies(loadBlacklisted);

        treeBreedingRecipeHandler = new TreeBreedingHandler();
        API.registerRecipeHandler(treeBreedingRecipeHandler);
        API.registerUsageHandler(treeBreedingRecipeHandler);

        treeProduceRecipeHandler = new TreeProduceHandler();
        API.registerRecipeHandler(treeProduceRecipeHandler);
        API.registerUsageHandler(treeProduceRecipeHandler);
        
        treeProductsCache = new HashMap<Integer, Collection<IAlleleSpecies>>();

        for (IAlleleTreeSpecies species : allTreeSpecies) {
            if (addSaplings) {
                API.addNBTItem(GeneticsUtils.stackFromSpecies(species, EnumGermlingType.SAPLING.ordinal()));
            }
            if (addPollen) {
                API.addNBTItem(GeneticsUtils.stackFromSpecies(species, EnumGermlingType.POLLEN.ordinal()));
            }
            for (ItemStack prod : GeneticsUtils.getProduceFromSpecies(species).keySet()) {
                addProductToTreeCache(prod.itemID, species);
            }
            for (ItemStack prod : GeneticsUtils.getSpecialtyFromSpecies(species).keySet()) {
                addProductToTreeCache(prod.itemID, species);
            }
        };
        
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void loadClient() {
        setupBees();
        setupTrees();
        FMLInterModComms.sendRuntimeMessage(this, "NEIPlugins", "register-crafting-handler", "Forestry Genetics@Bee Products@beeproducts");
        FMLInterModComms.sendRuntimeMessage(this, "NEIPlugins", "register-crafting-handler", "Forestry Genetics@Bee Breeding@beebreeding");
        FMLInterModComms.sendRuntimeMessage(this, "NEIPlugins", "register-crafting-handler", "Forestry Genetics@Tree Breeding@treebreeding");
    }
}
