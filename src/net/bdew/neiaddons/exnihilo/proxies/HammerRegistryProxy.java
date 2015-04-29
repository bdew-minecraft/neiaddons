/*
 * Copyright (c) bdew, 2013 - 2015
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
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
    public static Set<Item> sourceIds;
    public static Set<Item> dropIds;

    @SuppressWarnings("unchecked")
    public static List<SmashableProxy> getRegistry() {
        return new ProxyListView<SmashableProxy>(f_rewards.get(null), SmashableProxy.class);
    }

    @SuppressWarnings("unchecked")
    public static void init() throws ClassNotFoundException, NoSuchFieldException {
        Class<?> c_HammerRegistry = Utils.getAndCheckClass("exnihilo.registries.HammerRegistry", Object.class);
        f_rewards = TypedField.from(c_HammerRegistry, "rewards", List.class);

        clsBaseHammer = Utils.getAndCheckClass("exnihilo.items.hammers.ItemHammerBase", Item.class);

        for (String name : (Set<String>) Item.itemRegistry.getKeys()) {
            Item item = (Item) Item.itemRegistry.getObject(name);
            if (clsBaseHammer.isInstance(item))
                hammers.add(new ItemStack(item, 1));
        }

        dropIds = new HashSet<Item>();
        sourceIds = new HashSet<Item>();

        for (SmashableProxy smashable : getRegistry()) {
            if (smashable != null) {
                sourceIds.add(Item.getItemFromBlock(smashable.source()));
                dropIds.add(smashable.item());
            }
        }
    }
}
