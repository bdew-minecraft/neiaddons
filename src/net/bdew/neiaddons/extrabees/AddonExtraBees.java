/**
 * Copyright (c) bdew, 2013
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/neiaddons/master/MMPL-1.0.txt
 */

package net.bdew.neiaddons.extrabees;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.bdew.neiaddons.BaseAddon;
import net.bdew.neiaddons.NEIAddons;
import net.bdew.neiaddons.forestry.GeneticsUtils;
import codechicken.nei.api.API;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import forestry.api.apiculture.EnumBeeChromosome;
import forestry.api.apiculture.IAlleleBeeSpecies;
import forestry.api.apiculture.IBeeRoot;
import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.IAllele;

@Mod(modid = NEIAddons.modid + "|ExtraBees", name = "NEI Addons: Extra Bees", version = "@@VERSION@@", dependencies = "after:NEIAddons;after:ExtraBees;after:Forestry")
public class AddonExtraBees extends BaseAddon {

    public static boolean loadBlacklisted;
    public static boolean dumpSerums;
    public static Collection<IAlleleBeeSpecies> allBeeSpecies;

    public static IBeeRoot beeRoot;
    
    @Override
    public String getName() {
        return "Extra Bees";
    }

    @Override
    public String[] getDependencies() {
        return new String[] { "ExtraBees", "Forestry@[2.2.4.0,)" };
    }

    @Override
    public boolean checkSide(Side side) {
        return side.isClient();
    }

    @PreInit
    public void preInit(FMLPreInitializationEvent ev) {
        doPreInit(ev);
    }

    @Override
    public void init(Side side) throws Exception {
        loadBlacklisted = NEIAddons.config.get(getName(), "Load blacklisted", false, "Set to true to load blacklisted species and alleles, it's dangerous and (mostly) useless").getBoolean(false);
        dumpSerums = NEIAddons.config.get(getName(), "Dump serum list", false, "Set to true to dump all serums on startup for debug purposes").getBoolean(false);
        active = true;
    }

    public void registerSerums() {
        Set<AlleleBeeChromosomePair> res = new HashSet<AlleleBeeChromosomePair>();

        beeRoot = (IBeeRoot) AlleleManager.alleleRegistry.getSpeciesRoot("rootBees");
        
        for (IAlleleBeeSpecies species : allBeeSpecies) {
            IAllele[] template = beeRoot.getTemplate(species.getUID());
            for (int i = 0; i < template.length; i++) {
                if (template[i] != null) {
                    if ((!loadBlacklisted) && AlleleManager.alleleRegistry.isBlacklisted(template[i].getUID())) {
                        if (dumpSerums) {
                            logInfo("Skipping blacklisted allele: %s", template[i].getUID());
                        }
                        continue;
                    }
                    if (SerumUtils.shouldMakeSerum(template[i].getUID(),i)) {
                        res.add(new AlleleBeeChromosomePair(template[i], i));
                    }
                }
            }
        }

        if (dumpSerums) {
            logInfo("==== Serum dump ====");
            for (EnumBeeChromosome chromosome : EnumBeeChromosome.values()) {
                logInfo("%s:",chromosome.toString());
                for (AlleleBeeChromosomePair pair : res) {
                    if (pair.chromosome == chromosome.ordinal()) {
                        AlleleManager.alleleRegistry.getAllele(pair.allele);
                        logInfo(" * %s -> %s",pair.allele,SerumUtils.getSerum(pair).getDisplayName());
                    }
                }
                logInfo("===================================");
            }
        }

        for (AlleleBeeChromosomePair pair : res) {
            API.addNBTItem(SerumUtils.getSerum(pair));
        }
    }

    @Override
    public void loadClient() {
        try {
            SerumUtils.setup();
        } catch (Throwable e) {
            logWarning("Failed to get serum item:");
            e.printStackTrace();
            return;
        }        

        allBeeSpecies = GeneticsUtils.getAllBeeSpecies(loadBlacklisted);
        registerSerums();
        API.registerRecipeHandler(new IsolatorRecipeHandler());
        
        FMLInterModComms.sendRuntimeMessage(this, "NEIPlugins", "register-crafting-handler", "Exta Bees@Isolator@isolator");
    }
}
