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
import java.util.Map.Entry;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import forestry.api.apiculture.BeeManager;
import forestry.api.apiculture.EnumBeeType;
import forestry.api.apiculture.IAlleleBeeSpecies;
import forestry.api.apiculture.IBee;
import forestry.api.apiculture.IBeeGenome;
import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.IAllele;
import forestry.api.genetics.IAlleleSpecies;

public class BeeUtils {
    public static ItemStack stackFromAllele(IAllele allele, EnumBeeType type) {
        assert allele instanceof IAlleleSpecies;
        IAlleleSpecies species = (IAlleleSpecies) allele;
        IAllele[] template = BeeManager.breedingManager.getBeeTemplate(species.getUID());
        IBeeGenome genome = BeeManager.beeInterface.templateAsGenome(template);
        IBee bee = BeeManager.beeInterface.getBee(Minecraft.getMinecraft().theWorld, genome);
        bee.analyze();
        return BeeManager.beeInterface.getBeeStack(bee, type);
    }
    
    public static Collection<IAlleleBeeSpecies> getAllBeeSpecies(boolean includeBlacklisted) {
        ArrayList<IAlleleBeeSpecies> list = new ArrayList<IAlleleBeeSpecies>();
        for (Entry<String, IAllele> entry : AlleleManager.alleleRegistry.getRegisteredAlleles().entrySet()) {
            if (entry.getValue() instanceof IAlleleBeeSpecies) {
                if (includeBlacklisted || (!AlleleManager.alleleRegistry.isBlacklisted(entry.getValue().getUID()))) {
                    list.add((IAlleleBeeSpecies) entry.getValue());
                }
            }
        }
        return list;
    }
}
