/**
 * Copyright (c) bdew, 2013
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/neiaddons/master/MMPL-1.0.txt
 */

package net.bdew.neiaddons.extrabees;

import java.util.HashSet;
import java.util.Set;

import net.bdew.neiaddons.NEIAddons;
import net.bdew.neiaddons.Utils;
import net.bdew.neiaddons.forestry.GeneticsUtils;
import net.minecraft.item.ItemStack;
import codechicken.nei.MultiItemRange;
import codechicken.nei.api.API;
import cpw.mods.fml.common.event.FMLInterModComms;
import forestry.api.apiculture.EnumBeeChromosome;
import forestry.api.apiculture.IAlleleBeeSpecies;
import forestry.api.apiculture.IBeeRoot;
import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.IAllele;

public class AddonExtraBeesClient {

    public static IBeeRoot beeRoot;

    public static void registerSerums() {
        Set<AlleleBeeChromosomePair> res = new HashSet<AlleleBeeChromosomePair>();

        beeRoot = (IBeeRoot) AlleleManager.alleleRegistry.getSpeciesRoot("rootBees");

        for (IAlleleBeeSpecies species : AddonExtraBees.allBeeSpecies) {
            IAllele[] template = beeRoot.getTemplate(species.getUID());
            if (template == null) {
                AddonExtraBees.instance.logWarning("Template for %s is null, wtf?", species.getUID());
                continue;
            }
            for (int i = 0; i < template.length; i++) {
                if (template[i] != null) {
                    if ((!AddonExtraBees.loadBlacklisted) && AlleleManager.alleleRegistry.isBlacklisted(template[i].getUID())) {
                        if (AddonExtraBees.dumpSerums) {
                            AddonExtraBees.instance.logInfo("Skipping blacklisted allele: %s", template[i].getUID());
                        }
                        continue;
                    }
                    if (SerumUtils.shouldMakeSerum(template[i].getUID(), i)) {
                        res.add(new AlleleBeeChromosomePair(template[i], i));
                    }
                }
            }
        }

        if (AddonExtraBees.dumpSerums) {
            AddonExtraBees.instance.logInfo("==== Serum dump ====");
            for (EnumBeeChromosome chromosome : EnumBeeChromosome.values()) {
                AddonExtraBees.instance.logInfo("%s:", chromosome.toString());
                for (AlleleBeeChromosomePair pair : res) {
                    if (pair.chromosome == chromosome.ordinal()) {
                        try {
                            AddonExtraBees.instance.logInfo(" * %s -> %s", pair.allele, SerumUtils.getSerum(pair).getDisplayName());
                        } catch (Throwable e) {
                            AddonExtraBees.instance.logInfo(" * %s -> BORKED! %s", pair.allele, e.toString());
                            e.printStackTrace();
                        }
                    }
                }
                AddonExtraBees.instance.logInfo("===================================");
            }
        }

        if (NEIAddons.fakeItemsOn) {
            MultiItemRange range = new MultiItemRange();
            for (AlleleBeeChromosomePair pair : res) {
                ItemStack fake = NEIAddons.fakeItem.addItem(SerumUtils.getSerum(pair));
                range.add(fake);
                API.addNBTItem(fake);
            }
            API.addToRange("Extra Bees.Serums", range);
        } else {
            for (AlleleBeeChromosomePair pair : res) {
                API.addNBTItem(SerumUtils.getSerum(pair));
            }
        }
    }

    private static void addSubsets() {
        Class<?> ebMain;
        Class<?> ebConfig;
        try {
            ebMain = Class.forName("binnie.extrabees.ExtraBees");
            ebConfig = Class.forName("binnie.extrabees.config.ConfigurationMain");
        } catch (Throwable e) {
            AddonExtraBees.instance.logWarning("Failed to get Extra Bees items and blocks");
            e.printStackTrace();
            return;
        }
        Utils.addSubsetForItem(ebMain, "template", "Extra Bees.Templates");
        Utils.addSubsetForItem(ebMain, "serum", "Extra Bees.Serums");
        Utils.addSubsetForItem(ebMain, "liquidContainer", "Extra Bees.Containers");
        Utils.addSubsetForItem(ebMain, "apiaristMachine", "Extra Bees.Machines.Apiarist");
        Utils.addSubsetForItem(ebMain, "geneticMachine", "Extra Bees.Machines.Genetic");
        Utils.addSubsetForItem(ebMain, "advGeneticMachine", "Extra Bees.Machines.Advanced");
        Utils.addSubsetForItem(ebMain, "hive", "Extra Bees.Hives");
        Utils.addSubsetForItems(ebMain, new String[] { "templateBlank", "dictionary", "serumEmpty", "itemMisc" }, "Extra Bees.Misc");
        Utils.addSubsetForItems(ebConfig, new String[] { "hiveFrameID", "hiveFrame2ID", "hiveFrame3ID", "hiveFrame4ID", "hiveFrame5ID" }, "Extra Bees.Frames", 256);
        Utils.addSubsetForItem(ebConfig, "alvearyID", "Extra Bees.Machines.Alveary");

    }

    public static void load() {
        try {
            SerumUtils.setup();
        } catch (Throwable e) {
            AddonExtraBees.instance.logWarning("Failed to get serum item:");
            e.printStackTrace();
            return;
        }

        AddonExtraBees.allBeeSpecies = GeneticsUtils.getAllBeeSpecies(AddonExtraBees.loadBlacklisted);
        registerSerums();
        API.registerRecipeHandler(new IsolatorRecipeHandler());

        addSubsets();

        FMLInterModComms.sendRuntimeMessage(AddonExtraBees.instance, "NEIPlugins", "register-crafting-handler", "Exta Bees@Isolator@isolator");
    }
}
