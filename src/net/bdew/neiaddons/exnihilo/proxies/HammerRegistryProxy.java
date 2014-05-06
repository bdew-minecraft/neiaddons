/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/neiaddons/master/MMPL-1.0.txt
 */

package net.bdew.neiaddons.exnihilo.proxies;

import net.bdew.neiaddons.Utils;
import net.bdew.neiaddons.utils.TypedField;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HammerRegistryProxy {
    private static TypedField<List> f_rewards;
    public static List<ItemStack> hammers = new ArrayList<ItemStack>();
    public static Class<? extends Item> clsBaseHammer;
    public static Set<Integer> sourceIds;
    public static Set<Integer> dropIds;

    @SuppressWarnings("unchecked")
    public static List<SmashableProxy> getRegistry() {
        return new HammerListView(f_rewards.get(null));
    }

    public static void init() throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        Class<?> c_HammerRegistry = Utils.getAndCheckClass("exnihilo.registries.HammerRegistry", Object.class);
        f_rewards = TypedField.from(c_HammerRegistry, "rewards", List.class);

        clsBaseHammer = Utils.getAndCheckClass("exnihilo.items.hammers.ItemHammerBase", Item.class);

        hammers.add(new ItemStack(Utils.getAndCheckStaicField("exnihilo.Items", "HammerWood", Item.class)));
        hammers.add(new ItemStack(Utils.getAndCheckStaicField("exnihilo.Items", "HammerStone", Item.class)));
        hammers.add(new ItemStack(Utils.getAndCheckStaicField("exnihilo.Items", "HammerIron", Item.class)));
        hammers.add(new ItemStack(Utils.getAndCheckStaicField("exnihilo.Items", "HammerGold", Item.class)));
        hammers.add(new ItemStack(Utils.getAndCheckStaicField("exnihilo.Items", "HammerDiamond", Item.class)));

        dropIds = new HashSet<Integer>();
        sourceIds = new HashSet<Integer>();

        for (SmashableProxy smashable : getRegistry()) {
            sourceIds.add(smashable.sourceID());
            dropIds.add(smashable.id());
        }
    }
}
