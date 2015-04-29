/*
 * Copyright (c) bdew, 2013 - 2015
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
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
