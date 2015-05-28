/*
 * Copyright (c) bdew, 2013 - 2015
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.neiaddons.utils;

import codechicken.nei.api.ItemFilter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ModItemFilter implements ItemFilter {
    private String modId;
    private Boolean items;

    public ModItemFilter(String modId, Boolean items) {
        this.modId = modId;
        this.items = items;
    }

    @Override
    public boolean matches(ItemStack item) {
        if (item == null || item.getItem() == null) return false;
        if (item.getItem() instanceof ItemBlock && items) return false;
        if (!(item.getItem() instanceof ItemBlock) && !items) return false;
        String itemName = Item.itemRegistry.getNameForObject(item.getItem());
        if (itemName == null) return false;
        String[] s = itemName.split(":");
        return (s.length > 1) && s[0].equals(modId);
    }
}
