/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/neiaddons/master/MMPL-1.0.txt
 */

package net.bdew.neiaddons.exnihilo.proxies;

import cpw.mods.fml.common.registry.GameRegistry;
import net.bdew.neiaddons.Utils;
import net.bdew.neiaddons.utils.TypedField;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SieveRegistryProxy {
    private static TypedField<List> f_rewards;
    public static Block sieveBlock;
    public static List<ItemStack> sieves = new ArrayList<ItemStack>();
    public static Set<Item> sourceIds;
    public static Set<Item> dropIds;

    @SuppressWarnings("unchecked")
    public static List<SiftRewardProxy> getRegistry() {
        return new ProxyListView<SiftRewardProxy>(f_rewards.get(null), SiftRewardProxy.class);
    }

    public static void init() throws ClassNotFoundException, NoSuchFieldException {
        Class<?> c_SieveRegistry = Utils.getAndCheckClass("exnihilo.registries.SieveRegistry", Object.class);
        f_rewards = TypedField.from(c_SieveRegistry, "rewards", List.class);

        sieveBlock = GameRegistry.findBlock("exnihilo", "sifting_table");

        sieveBlock.getSubBlocks(Item.getItemFromBlock(sieveBlock), null, sieves);

        dropIds = new HashSet<Item>();
        sourceIds = new HashSet<Item>();

        for (SiftRewardProxy siftreward : getRegistry()) {
            if (siftreward != null) {
                sourceIds.add(Item.getItemFromBlock(siftreward.source()));
                dropIds.add(siftreward.item());
            }
        }
    }
}
