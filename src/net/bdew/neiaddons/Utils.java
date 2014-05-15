/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/neiaddons/master/MMPL-1.0.txt
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

    public static void addSubsetForItems(Class<?> cls, String[] fields, String rangeName, int shift) {
        /* TODO: Disabled for now, no ranges in new NEI
        MultiItemRange multi = new MultiItemRange();
        for (String field : fields) {
            try {
                Object item = cls.getField(field).get(null);
                ItemRange range;
                if (item instanceof Item) {
                    range = new ItemRange(((Item) item).itemID);
                } else if (item instanceof Block) {
                    range = new ItemRange(((Block) item).blockID);
                } else if (item instanceof Integer) {
                    range = new ItemRange((Integer) item + shift);
                } else {
                    NEIAddons.logWarning("%s.%s (%s) type unknown - %s", cls.getName(), field, item.toString(), item.getClass().getName());
                    continue;
                }
                multi.add(range);
                NEIAddons.logInfo("Registered subset %s: %s", rangeName, range.toString());
            } catch (Throwable e) {
                NEIAddons.logWarningExc(e, "Failed to get %s.%s", cls.getName(), field);
            }
        }
        if (multi.ranges.size() > 0) {
            API.addToRange(rangeName, multi);
        }
        */
    }

    public static void addSubsetForItem(Class<?> cls, String field, String rangeName) {
        addSubsetForItems(cls, new String[]{field}, rangeName, 0);
    }

    public static void addSubsetForItems(Class<?> cls, String[] fields, String rangeName) {
        addSubsetForItems(cls, fields, rangeName, 0);
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

    public static Map<ItemStack, Integer> mergeStacks(Map<ItemStack, Integer> stacks) {
        Map<ItemStack, Integer> merged = new HashMap<ItemStack, Integer>();
        outer:
        for (Entry<ItemStack, Integer> stack : stacks.entrySet()) {
            for (Entry<ItemStack, Integer> mergedStack : merged.entrySet()) {
                if (isSameItem(stack.getKey(), mergedStack.getKey()) && (stack.getValue().equals(mergedStack.getValue()))) {
                    mergedStack.getKey().stackSize += 1;
                    continue outer;
                }
            }
            merged.put(stack.getKey().copy(), stack.getValue());
        }
        return merged;
    }
}
