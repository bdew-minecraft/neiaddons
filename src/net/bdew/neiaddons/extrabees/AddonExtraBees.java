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
import java.util.Map.Entry;

import net.bdew.neiaddons.BaseAddon;
import net.bdew.neiaddons.NEIAddons;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import codechicken.nei.api.API;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import forestry.api.apiculture.BeeManager;
import forestry.api.apiculture.EnumBeeChromosome;
import forestry.api.apiculture.IAlleleBeeSpecies;
import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.IAllele;

@Mod(modid = NEIAddons.modid + "|ExtraBees", name = "NEI Addons: Extra Bees", version = "@@VERSION@@", dependencies = "after:NEIAddons;after:ExtraBees;after:Forestry")
public class AddonExtraBees extends BaseAddon {

    public static Item itemSerum;

    public static boolean loadBlacklisted;
    public static boolean dumpSerums;

    @Override
    public String getName() {
        return "Extra Bees";
    }

    @Override
    public String[] getDependencies() {
        return new String[] { "ExtraBees", "Forestry@[2.2.0.0,2.2.3.0)" };
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
        loadBlacklisted = NEIAddons.config.get(getName(), "Load blacklisted serums", false, "Set to true to load blacklisted serums, it's dangerous and (mostly) useless").getBoolean(false);
        dumpSerums = NEIAddons.config.get(getName(), "Dump serum list", false, "Set to true to dump all serums on startup for debug purposes").getBoolean(false);
        active = true;
    }

    @SuppressWarnings("deprecation")
    public static ItemStack getSerum(AlleleBeeChromosomePair pair) {
        ItemStack serum = new ItemStack(itemSerum);
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("uid", pair.allele);
        if (pair.chromosome >= EnumBeeChromosome.HUMIDITY.ordinal()) {
            nbt.setInteger("chromosome", pair.chromosome - 1);
        } else {
            nbt.setInteger("chromosome", pair.chromosome);
        }
        nbt.setInteger("quality", 10);
        serum.setTagCompound(nbt);
        return serum;
    }

    public static void registerSerums() {
        Set<AlleleBeeChromosomePair> res = new HashSet<AlleleBeeChromosomePair>();
        for (Entry<String, IAllele> entry : AlleleManager.alleleRegistry.getRegisteredAlleles().entrySet()) {
            if (entry.getValue() instanceof IAlleleBeeSpecies) {
                IAllele[] template = BeeManager.breedingManager.getBeeTemplate(entry.getValue().getUID());
                for (int i = 0; i < template.length; i++) {
                    if (template[i] != null) {
                        if ((!loadBlacklisted) && AlleleManager.alleleRegistry.isBlacklisted(template[i].getUID())) {
                            if (dumpSerums) {
                                logInfo("Skipping blacklisted allele: %s", template[i].getUID());
                            }
                            continue;
                        }
                        if (template[i].getUID().equals("forestry.boolFalse")) {
                            continue;
                        }
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
                        logInfo(" * %s -> %s",pair.allele,AddonExtraBees.getSerum(pair).getDisplayName());
                    }
                }
                logInfo("===================================");
            }
        }

        for (AlleleBeeChromosomePair pair : res) {
            API.addNBTItem(getSerum(pair));
        }
    }

    @Override
    public void loadClient() {
        try {
            itemSerum = (Item) Class.forName("binnie.extrabees.core.ExtraBeeItem").getField("serum").get(null);
        } catch (Throwable e) {
            logWarning("Failed to get serum item:");
            e.printStackTrace();
            return;
        }

        registerSerums();
    }
}
