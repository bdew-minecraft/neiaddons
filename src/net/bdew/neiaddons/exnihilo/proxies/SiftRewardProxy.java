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
import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class SiftRewardProxy {
    private static Class<?> c_SiftReward;
    private static TypedField<Block> f_source;
    private static TypedField<Integer> f_sourceMeta;
    private static TypedField<Item> f_item;
    private static TypedField<Integer> f_meta;
    private static TypedField<Integer> f_rarity;

    private Object reward;

    public SiftRewardProxy(Object reward) {
        if (!c_SiftReward.isInstance(reward))
            throw new RuntimeException(String.format("Invalid object (%s) passed to proxy (%s)", reward.toString(), c_SiftReward.toString()));
        this.reward = reward;
    }

    public Block source() {
        return f_source.get(reward);
    }

    public int sourceMeta() {
        return f_sourceMeta.get(reward);
    }

    public Item item() {
        return f_item.get(reward);
    }

    public int meta() {
        return f_meta.get(reward);
    }

    public int rarity() {
        return f_rarity.get(reward);
    }

    public static void init() throws ClassNotFoundException, NoSuchFieldException {
        c_SiftReward = Utils.getAndCheckClass("exnihilo.registries.helpers.SiftReward", Object.class);
        f_source = TypedField.from(c_SiftReward, "source", Block.class);
        f_sourceMeta = TypedField.from(c_SiftReward, "sourceMeta", Integer.class);
        f_item = TypedField.from(c_SiftReward, "item", Item.class);
        f_meta = TypedField.from(c_SiftReward, "meta", Integer.class);
        f_rarity = TypedField.from(c_SiftReward, "rarity", Integer.class);
    }
}
