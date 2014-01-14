/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/neiaddons/master/MMPL-1.0.txt
 */

package net.bdew.neiaddons;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemFakeNBT extends Item {
    private Map<Integer, ItemStack> map;
    private int nextid;

    public ItemFakeNBT(int id) {
        super(id);
        map = new HashMap<Integer, ItemStack>();
        nextid = 0;
        setHasSubtypes(true);
        setMaxDamage(-1);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List list) {
        for (Entry<Integer, ItemStack> e : map.entrySet()) {
            list.add(new ItemStack(itemID, 1, e.getKey()));
        }
    }
    
    public ItemStack addItem(ItemStack stack) {
        map.put(nextid, stack.copy());
        return new ItemStack(this, 1, nextid++);
    }

    public ItemStack getOriginal(ItemStack stack) {
        if (stack.itemID == itemID && map.containsKey(stack.getItemDamage())) {
            return map.get(stack.getItemDamage()).copy();
        } else {
            return stack;
        }
    }

    @Override
    public Icon getIcon(ItemStack stack, int pass) {
        ItemStack orig = getOriginal(stack);
        if (orig != null)
            return orig.getItem().getIcon(orig, pass);
        return null;
    }
    
    @Override
    public Icon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining) {
        ItemStack orig = getOriginal(stack);
        if (orig != null)
            return orig.getItem().getIcon(orig, renderPass, player, usingItem, useRemaining);
        return null;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIconFromDamageForRenderPass(int damage, int pass) {
        if (map.containsKey(damage)) {
            ItemStack orig = map.get(damage);
            return orig.getItem().getIconFromDamageForRenderPass(orig.getItemDamage(), pass);
        }
        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIconFromDamage(int damage) {
        if (map.containsKey(damage)) {
            ItemStack orig = map.get(damage);
            return orig.getItem().getIconFromDamage(orig.getItemDamage());
        }
        return null;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int par2) {
        ItemStack orig = getOriginal(stack);
        if (orig != null)
            return orig.getItem().getColorFromItemStack(orig, par2);
        return super.getColorFromItemStack(stack, par2);
    }

    @Override
    public int getRenderPasses(int metadata) {
        if (map.containsKey(metadata)) {
            ItemStack orig = map.get(metadata);
            return orig.getItem().getRenderPasses(orig.getItemDamage());
        }
        return 1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return true;
    }
    
    @Override
    public String getItemDisplayName(ItemStack stack) {
        ItemStack orig = getOriginal(stack);
        if (orig != null)
            return "[F] "+orig.getItem().getItemDisplayName(orig);
        return "Unknown";
    }

    @SuppressWarnings("rawtypes")
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
        ItemStack orig = getOriginal(stack);
        if (orig != null)
            orig.getItem().addInformation(orig, player, list, par4);
    }
}
