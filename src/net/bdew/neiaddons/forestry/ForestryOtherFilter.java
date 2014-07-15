/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/neiaddons/master/MMPL-1.0.txt
 */

package net.bdew.neiaddons.forestry;

import forestry.api.genetics.AlleleManager;
import net.bdew.neiaddons.utils.ModItemFilter;
import net.minecraft.item.ItemStack;

public class ForestryOtherFilter extends ModItemFilter {
    public ForestryOtherFilter(Boolean items) {
        super("Forestry", items);
    }

    @Override
    public boolean matches(ItemStack item) {
        return !AlleleManager.alleleRegistry.isIndividual(item) && super.matches(item);
    }
}
