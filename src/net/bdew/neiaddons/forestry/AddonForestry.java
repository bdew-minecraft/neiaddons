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
import java.util.HashSet;
import java.util.Map.Entry;

import net.bdew.neiaddons.BaseAddon;
import net.bdew.neiaddons.NEIAddons;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import codechicken.nei.api.API;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import forestry.api.apiculture.EnumBeeType;
import forestry.api.apiculture.IAlleleBeeSpecies;
import forestry.api.core.ItemInterface;
import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.IAllele;

@Mod(modid = NEIAddons.modid + "|Forestry", name = "NEI Addons: Forestry", version = "@@VERSION@@", dependencies = "after:NEIAddons;after:Forestry")
public class AddonForestry extends BaseAddon {
    private BeeBreedingRecipeHandler beeBreedingRecipeHandler;
    private BeeProductsRecipeHandler beeProductsRecipeHandler;

    public static boolean showSecret;
    public static boolean addBees;
    public static boolean addCombs;

    @Override
    public String getName() {
        return "Forestry";
    }

    @PreInit
    public void preInit(FMLPreInitializationEvent ev) {
        if (!Loader.isModLoaded("Forestry")) {
            NEIAddons.log.info("Forestry is not installed, skipping");
            return;
        }

        if (ev.getSide() != Side.CLIENT) {
            NEIAddons.log.info("Forestry Addon is client-side only, skipping");
            return;
        }

        NEIAddons.register(this);
    }

    @Override
    public void init(Side side) throws Exception {
        showSecret = NEIAddons.config.get(getName(), "Show Secret Mutations", false, "Set to true to show secret mutations").getBoolean(false);
        addBees = NEIAddons.config.get(getName(), "Add Bees to Search", true, "Set to true to add ALL bees to NEI search, this will include secret, inactive, unfinished bees, etc.").getBoolean(false);
        addCombs = NEIAddons.config.get(getName(), "Add Combs to Search", false, "Set to true to add all combs that are produced by bees to NEI search").getBoolean(false);
        active = true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void loadClient() {
        beeBreedingRecipeHandler = new BeeBreedingRecipeHandler();
        API.registerRecipeHandler(beeBreedingRecipeHandler);
        API.registerUsageHandler(beeBreedingRecipeHandler);

        beeProductsRecipeHandler = new BeeProductsRecipeHandler();
        API.registerRecipeHandler(beeProductsRecipeHandler);
        API.registerUsageHandler(beeProductsRecipeHandler);

        Item comb = ItemInterface.getItem("beeComb").getItem();

        if (addBees || addCombs) {
            HashSet<Integer> seencombs = new HashSet<Integer>();
            for (Entry<String, IAllele> entry : AlleleManager.alleleRegistry.getRegisteredAlleles().entrySet()) {
                if (entry.getValue() instanceof IAlleleBeeSpecies) {

                    IAlleleBeeSpecies species = (IAlleleBeeSpecies) entry.getValue();
                    if (addBees) {
                        API.addNBTItem(BeeUtils.stackFromAllele(species, EnumBeeType.QUEEN));
                        API.addNBTItem(BeeUtils.stackFromAllele(species, EnumBeeType.DRONE));
                        API.addNBTItem(BeeUtils.stackFromAllele(species, EnumBeeType.PRINCESS));
                    }
                    if (addCombs) {
                        for (ItemStack prod : species.getProducts().keySet()) {
                            if (prod.itemID == comb.itemID) {
                                seencombs.add(prod.getItemDamage());
                            }
                        }
                        for (ItemStack prod : species.getSpecialty().keySet()) {
                            if (prod.itemID == comb.itemID) {
                                seencombs.add(prod.getItemDamage());
                            }
                        }
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

        FMLInterModComms.sendRuntimeMessage(this, "NEIPlugins", "register-crafting-handler", "Forestry Bees@Bee Products@beeproducts");
        FMLInterModComms.sendRuntimeMessage(this, "NEIPlugins", "register-crafting-handler", "Forestry Bees@Bee Breeding@beebreeding");
    }
}
