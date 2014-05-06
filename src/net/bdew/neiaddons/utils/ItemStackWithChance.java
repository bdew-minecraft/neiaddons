/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/neiaddons/master/MMPL-1.0.txt
 */

package net.bdew.neiaddons.utils;

import net.minecraft.item.ItemStack;

public class ItemStackWithChance {
    public ItemStack itemStack;
    public float chance;

    public ItemStackWithChance(ItemStack itemStack, float chance) {
        this.itemStack = itemStack;
        this.chance = chance;
    }
}
