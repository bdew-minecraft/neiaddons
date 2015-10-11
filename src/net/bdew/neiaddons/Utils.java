/*
 * Copyright (c) bdew, 2013 - 2015
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.neiaddons;

import codechicken.nei.api.API;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Utils {
    @SuppressWarnings("unchecked")
    public static <T> Class<? extends T> getAndCheckClass(String cls, Class<? extends T> sup) throws ClassNotFoundException {
        Class<?> c = Class.forName(cls);
        if (c != null) {
            if (sup.isAssignableFrom(c)) {
                return (Class<? extends T>) c;
            } else {
                throw new RuntimeException(cls + " doesn't extend " + sup.getName());
            }
        } else {
            throw new RuntimeException("Can't get " + cls);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T getAndCheckStaicField(String cls, String field, Class<T> sup) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        Class<?> c = Class.forName(cls);
        if (c == null) throw new RuntimeException("Can't get " + cls);

        Field f = c.getField(field);
        if (f == null) throw new RuntimeException("Can't get " + cls + "." + field);

        Object v = f.get(null);
        if (v == null) throw new RuntimeException(cls + "." + field + " is null");
        if (sup.isInstance(v)) {
            return (T) v;
        } else {
            throw new RuntimeException(String.format("%s.%s is of wrong type, expected: %s, got: %s", cls, field, sup, v.getClass()));
        }
    }

    public static void drawCenteredString(String s, int x, int y, int color) {
        FontRenderer f = Minecraft.getMinecraft().fontRenderer;
        f.drawString(s, x - f.getStringWidth(s) / 2, y, color);
    }

    public static void safeAddNBTItem(ItemStack item) {
        if (item == null)
            return;
        API.addItemListEntry(item);
    }

    /**
     * Like ItemStack.isItemStackEqual but ignores stack size
     */
    public static boolean isSameItem(ItemStack s1, ItemStack s2) {
        if ((s1 == null) || (s2 == null))
            return false;
        if (s1.getItem() != s2.getItem())
            return false;
        if (s1.getItemDamage() != s2.getItemDamage())
            return false;
        if ((s1.getTagCompound() == null) && (s2.getTagCompound() == null))
            return true;
        if ((s1.getTagCompound() == null) || (s2.getTagCompound() == null))
            return false;
        return s1.getTagCompound().equals(s2.getTagCompound());
    }

    public static Map<ItemStack, Float> mergeStacks(Map<ItemStack, Float> stacks) {
        Map<ItemStack, Float> merged = new HashMap<ItemStack, Float>();
        outer:
        for (Entry<ItemStack, Float> stack : stacks.entrySet()) {
            if (stack.getKey() == null) {
                NEIAddons.logSevere("Null ItemStack in mergeStacks!");
                continue;
            }
            for (Entry<ItemStack, Float> mergedStack : merged.entrySet()) {
                if (isSameItem(stack.getKey(), mergedStack.getKey()) && (stack.getValue().equals(mergedStack.getValue()))) {
                    mergedStack.getKey().stackSize += 1;
                    continue outer;
                }
            }
            merged.put(stack.getKey().copy(), stack.getValue());
        }
        return merged;
    }

    public static Map<ItemStack, Float> sanitizeDrops(Map<ItemStack, Float> drops, String origin) {
        Map<ItemStack, Float> res = new HashMap<ItemStack, Float>();
        boolean complained = false;
        if (drops == null) {
            NEIAddons.logWarning("%s returned null", origin);
            return res;
        }
        for (Entry<ItemStack, Float> ent : drops.entrySet()) {
            if (ent.getKey() == null || ent.getKey().getItem() == null) {
                if (!complained) {
                    NEIAddons.logWarning("%s contains nulls and/or corrupt item stacks", origin);
                    complained = true;
                }
                continue;
            }
            res.put(ent.getKey(), ent.getValue());
        }
        return res;
    }
}
