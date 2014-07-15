/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/neiaddons/master/MMPL-1.0.txt
 */

package net.bdew.neiaddons.forestry;

import codechicken.nei.api.ItemFilter;
import forestry.api.genetics.IIndividual;
import forestry.api.genetics.ISpeciesRoot;
import net.minecraft.item.ItemStack;

public class GeneticItemFilter implements ItemFilter {
    private ISpeciesRoot root;
    private int type;
    private boolean analyzedOnly;

    public GeneticItemFilter(ISpeciesRoot root, int type, boolean analyzedOnly) {
        this.root = root;
        this.type = type;
        this.analyzedOnly = analyzedOnly;
    }

    private boolean isMember(ItemStack item) {
        if (type >= 0)
            return root.isMember(item, type);
        else
            return root.isMember(item);
    }

    @Override
    public boolean matches(ItemStack item) {
        if (analyzedOnly) {
            if (!isMember(item)) return false;
            IIndividual individual = root.getMember(item);
            return individual.isAnalyzed();
        } else return isMember(item);
    }
}
