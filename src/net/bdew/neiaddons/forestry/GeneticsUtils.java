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
import forestry.api.arboriculture.IAlleleTreeSpecies;
import forestry.api.arboriculture.ITree;
import forestry.api.arboriculture.ITreeRoot;
import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.IAllele;
import forestry.api.genetics.IAlleleSpecies;
import forestry.api.genetics.IIndividual;
import forestry.api.genetics.ISpeciesRoot;

public class GeneticsUtils {
    public enum RecipePosition {
        Parent1, Parent2, Offspring, Producer;
    }

    public static Map<RecipePosition, Integer> beePositionToType;
    public static Map<RecipePosition, Integer> treePositionToType;

    static {
        beePositionToType = new HashMap<RecipePosition, Integer>();
        beePositionToType.put(RecipePosition.Parent1, EnumBeeType.PRINCESS.ordinal());
        beePositionToType.put(RecipePosition.Parent2, EnumBeeType.DRONE.ordinal());
        beePositionToType.put(RecipePosition.Offspring, EnumBeeType.QUEEN.ordinal());
        beePositionToType.put(RecipePosition.Producer, EnumBeeType.QUEEN.ordinal());

        treePositionToType = new HashMap<RecipePosition, Integer>();
        treePositionToType.put(RecipePosition.Parent1, EnumGermlingType.SAPLING.ordinal());
        treePositionToType.put(RecipePosition.Parent2, EnumGermlingType.POLLEN.ordinal());
        treePositionToType.put(RecipePosition.Offspring, EnumGermlingType.SAPLING.ordinal());
        treePositionToType.put(RecipePosition.Producer, EnumGermlingType.SAPLING.ordinal());
    }

    public static ItemStack stackFromSpecies(IAlleleSpecies species, RecipePosition position) {
        ISpeciesRoot root = species.getRoot();
        int type = 0;
        if (root instanceof IBeeRoot) {
            type = beePositionToType.get(position);
        } else if (root instanceof ITreeRoot) {
            type = treePositionToType.get(position);
        }
        return stackFromSpecies(species, type);
    }

    public static ItemStack stackFromSpecies(IAlleleSpecies species, int type) {
        ISpeciesRoot root = species.getRoot();
        IAllele[] template = root.getTemplate(species.getUID());
        if (template==null) {
            AddonForestry.instance.logWarning("Template for %s is null, wtf?", species.getUID());
            return null;
        }
        IIndividual individual = root.templateAsIndividual(template);
        individual.analyze();
        return root.getMemberStack(individual, type);
    }

    @Deprecated
    public static ItemStack stackFromAllele(IAllele allele, EnumBeeType type) {
        assert allele instanceof IAlleleSpecies;
        return stackFromSpecies((IAlleleSpecies) allele, type.ordinal());
    }

    public static Collection<IAlleleBeeSpecies> getAllBeeSpecies(boolean includeBlacklisted) {
        return getAllTypedSpecies(IAlleleBeeSpecies.class, includeBlacklisted);
    }

    public static Collection<IAlleleTreeSpecies> getAllTreeSpecies(boolean includeBlacklisted) {
        return getAllTypedSpecies(IAlleleTreeSpecies.class, includeBlacklisted);
    }

    @SuppressWarnings("unchecked")
    public static <T> Collection<T> getAllTypedSpecies(Class<? extends T> type, boolean includeBlacklisted) {
        ArrayList<T> list = new ArrayList<T>();
        for (Entry<String, IAllele> entry : AlleleManager.alleleRegistry.getRegisteredAlleles().entrySet()) {
            if (type.isInstance(entry.getValue())) {
                if (includeBlacklisted || !AlleleManager.alleleRegistry.isBlacklisted(entry.getValue().getUID())) {
                    list.add((T) entry.getValue());
                }
            }
        }
        return list;
    }

    static public Map<ItemStack, Integer> getProduceFromSpecies(IAlleleSpecies species) {
        if (species instanceof IAlleleBeeSpecies) {
            return ((IAlleleBeeSpecies) species).getProducts();
        } else if (species instanceof IAlleleTreeSpecies) {
            ITreeRoot root = (ITreeRoot) species.getRoot();
            ITree tree = (ITree) root.templateAsIndividual(root.getTemplate(species.getUID()));
            Map<ItemStack, Integer> result = new HashMap<ItemStack, Integer>();
            for (ItemStack stack : tree.getProduceList()) {
                result.put(stack, 100);
            }
            return result;
        }
        return null;
    }

    static public Map<ItemStack, Integer> getSpecialtyFromSpecies(IAlleleSpecies species) {
        if (species instanceof IAlleleBeeSpecies) {
            return ((IAlleleBeeSpecies) species).getSpecialty();
        } else if (species instanceof IAlleleTreeSpecies) {
            ITreeRoot root = (ITreeRoot) species.getRoot();
            ITree tree = (ITree) root.templateAsIndividual(root.getTemplate(species.getUID()));
            Map<ItemStack, Integer> result = new HashMap<ItemStack, Integer>();
            for (ItemStack stack : tree.getSpecialtyList()) {
                result.put(stack, 100);
            }
            return result;
        }
        return null;
    };

}
