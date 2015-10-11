/*
 * Copyright (c) bdew, 2013 - 2015
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.neiaddons.forestry;

import forestry.api.apiculture.EnumBeeType;
import forestry.api.apiculture.IAlleleBeeSpecies;
import forestry.api.apiculture.IBeeRoot;
import forestry.api.arboriculture.EnumGermlingType;
import forestry.api.arboriculture.IAlleleTreeSpecies;
import forestry.api.arboriculture.ITree;
import forestry.api.arboriculture.ITreeRoot;
import forestry.api.genetics.*;
import forestry.api.lepidopterology.IAlleleButterflySpecies;
import net.bdew.neiaddons.Utils;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class GeneticsUtils {
    public enum RecipePosition {
        Parent1, Parent2, Offspring, Producer
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
        if (template == null) {
            AddonForestry.instance.logWarning("Template for %s is null, wtf?", species.getUID());
            return null;
        }
        IIndividual individual = root.templateAsIndividual(template);
        individual.analyze();
        ItemStack stack = root.getMemberStack(individual, type);
        if (stack == null) {
            AddonForestry.instance.logWarning("Got null from getMemberStack, wtf? (%s)", species.getUID());
        }
        return stack;
    }

    public static Collection<IAlleleBeeSpecies> getAllBeeSpecies(boolean includeBlacklisted) {
        return getAllTypedSpecies(IAlleleBeeSpecies.class, includeBlacklisted);
    }

    public static Collection<IAlleleButterflySpecies> getAllButterflySpecies(boolean includeBlacklisted) {
        return getAllTypedSpecies(IAlleleButterflySpecies.class, includeBlacklisted);
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

    static public Map<ItemStack, Float> getProduceFromSpecies(IAlleleSpecies species) {
        if (species instanceof IAlleleBeeSpecies) {
            return Utils.sanitizeDrops(((IAlleleBeeSpecies) species).getProductChances(), species.getUID() + " drops");
        } else if (species instanceof IAlleleTreeSpecies) {
            Map<ItemStack, Float> result = new HashMap<ItemStack, Float>();
            ITreeRoot root = (ITreeRoot) species.getRoot();
            IAllele[] template = root.getTemplate(species.getUID());
            if (template == null) {
                AddonForestry.instance.logWarning("Template for %s is null, wtf?", species.getUID());
                return result;
            }
            ITree tree = root.templateAsIndividual(template);
            for (ItemStack stack : tree.getProduceList()) {
                if (stack == null || stack.getItem() == null) {
                    AddonForestry.instance.logWarning("%s returned null in produce list", species.getUID());
                    continue;
                }
                result.put(stack, 1F);
            }
            return result;
        }
        return new HashMap<ItemStack, Float>();
    }

    static public Map<ItemStack, Float> getSpecialtyFromSpecies(IAlleleSpecies species) {
        if (species instanceof IAlleleBeeSpecies) {
            return Utils.sanitizeDrops(((IAlleleBeeSpecies) species).getSpecialtyChances(), species.getUID() + " specialty");
        } else if (species instanceof IAlleleTreeSpecies) {
            Map<ItemStack, Float> result = new HashMap<ItemStack, Float>();
            ITreeRoot root = (ITreeRoot) species.getRoot();
            IAllele[] template = root.getTemplate(species.getUID());
            if (template == null) {
                AddonForestry.instance.logWarning("Template for %s is null, wtf?", species.getUID());
                return result;
            }
            ITree tree = root.templateAsIndividual(template);
            for (ItemStack stack : tree.getSpecialtyList()) {
                if (stack == null || stack.getItem() == null) {
                    AddonForestry.instance.logWarning("%s returned null in specialty list", species.getUID());
                    continue;
                }
                result.put(stack, 1F);
            }
            return result;
        }
        return new HashMap<ItemStack, Float>();
    }
}
