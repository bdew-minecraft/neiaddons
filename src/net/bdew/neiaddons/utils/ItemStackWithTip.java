/*
 * Copyright (c) bdew, 2013 - 2015
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
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
