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

import java.util.ArrayList;
import java.util.List;

public class ItemStackWithTip {
    public ItemStack itemStack;
    public List<String> tip;

    public ItemStackWithTip(ItemStack itemStack) {
        this(itemStack, new ArrayList<String>());
    }

    public ItemStackWithTip(ItemStack itemStack, List<String> tip) {
        this.itemStack = itemStack;
        this.tip = tip;
    }
}
