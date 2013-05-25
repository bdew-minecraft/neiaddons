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
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.item.ItemStack;
import forestry.api.apiculture.EnumBeeType;
import forestry.api.apiculture.IAlleleBeeSpecies;
import forestry.api.apiculture.IBeeRoot;
import forestry.api.arboriculture.EnumGermlingType;
import forestry.api.arboriculture.ITreeRoot;
import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.IAllele;
import forestry.api.genetics.IAlleleSpecies;
import forestry.api.genetics.IIndividual;
import forestry.api.genetics.ISpeciesRoot;

public class BeeUtils {
    public enum RecipePosition {
        Parent1, Parent2, Offspring;
    }
    
    public static Map<RecipePosition,Integer> beePositionToType;
    public static Map<RecipePosition,Integer> treePositionToType;
    
    static {
        beePositionToType = new HashMap<RecipePosition, Integer>();
        beePositionToType.put(RecipePosition.Parent1, EnumBeeType.PRINCESS.ordinal());
        beePositionToType.put(RecipePosition.Parent2, EnumBeeType.DRONE.ordinal());
        beePositionToType.put(RecipePosition.Offspring, EnumBeeType.QUEEN.ordinal());

        beePositionToType = new HashMap<RecipePosition, Integer>();
        beePositionToType.put(RecipePosition.Parent1, EnumGermlingType.POLLEN.ordinal());
        beePositionToType.put(RecipePosition.Parent2, EnumGermlingType.GERMLING.ordinal());
        beePositionToType.put(RecipePosition.Offspring, EnumGermlingType.SAPLING.ordinal());
    }

    public static ItemStack stackFromSecies(IAlleleSpecies species, RecipePosition position) {
        ISpeciesRoot root = species.getRoot();
        int type = 0;
        if (root instanceof IBeeRoot) {
            type = beePositionToType.get(position);
        } else if (root instanceof ITreeRoot) {
            type = beePositionToType.get(position);
        }
        return stackFromSecies(species, type);
    }

    public static ItemStack stackFromSecies(IAlleleSpecies species, int type) {
        ISpeciesRoot root = species.getRoot();
        IAllele[] template = root.getTemplate(species.getUID());
        IIndividual individual = root.templateAsIndividual(template);
        individual.analyze();
        return root.getMemberStack(individual, type);
    }

    
    @Deprecated
    public static ItemStack stackFromAllele(IAllele allele, EnumBeeType type) {
        assert allele instanceof IAlleleSpecies;
        return stackFromSecies((IAlleleSpecies)allele, type.ordinal());
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
