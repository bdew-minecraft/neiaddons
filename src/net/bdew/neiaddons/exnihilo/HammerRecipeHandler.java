/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/neiaddons/master/MMPL-1.0.txt
 */

package net.bdew.neiaddons.exnihilo;

import net.bdew.neiaddons.Utils;
import net.bdew.neiaddons.exnihilo.proxies.HammerRegistryProxy;
import net.bdew.neiaddons.exnihilo.proxies.SmashableProxy;
import net.bdew.neiaddons.utils.ItemStackWithChance;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

public class HammerRecipeHandler extends BaseRecipeHandler {
    @Override
    public String getRecipeName() {
        return "ExNihilo Hammer";
    }

    @Override
    public String getRecipeId() {
        return "bdew.exnihilo.hammer";
    }

    @Override
    public boolean isPossibleInput(ItemStack stack) {
        return HammerRegistryProxy.sourceIds.contains(stack.itemID);
    }

    @Override
    public boolean isPossibleOutput(ItemStack stack) {
        return HammerRegistryProxy.dropIds.contains(stack.itemID);
    }

    @Override
    public List<ItemStack> getTools() {
        return HammerRegistryProxy.hammers;
    }

    @Override
    public boolean isValidTool(ItemStack tool) {
        return HammerRegistryProxy.clsBaseHammer.isInstance(tool.getItem());
    }

    @Override
    public List<ItemStackWithChance> getProcessingResults(ItemStack from) {
        Map<Pair<Integer, Integer>, Float> drops = new HashMap<Pair<Integer, Integer>, Float>();
        for (SmashableProxy x : HammerRegistryProxy.getRegistry()) {
            if (x.sourceID() == from.itemID && x.sourceMeta() == from.getItemDamage() && x.id() > 0) {
                Pair<Integer, Integer> idAndMeta = Pair.of(x.id(), x.meta());
                if (drops.containsKey(idAndMeta))
                    drops.put(idAndMeta, drops.get(idAndMeta) + x.chance());
                else
                    drops.put(idAndMeta, x.chance());
            }
        }

        ArrayList<ItemStackWithChance> dropsList = new ArrayList<ItemStackWithChance>();
        for (Map.Entry<Pair<Integer, Integer>, Float> x : drops.entrySet())
            dropsList.add(new ItemStackWithChance(new ItemStack(x.getKey().getLeft(), x.getValue() < 1 ? 1 : Math.round(x.getValue()), x.getKey().getRight()), x.getValue()));
        Utils.sortDropListByChance(dropsList);
        return dropsList;
    }

    @Override
    public List<ItemStack> getInputsFor(ItemStack result) {
        HashSet<Pair<Integer, Integer>> sources = new HashSet<Pair<Integer, Integer>>();
        for (SmashableProxy x : HammerRegistryProxy.getRegistry()) {
            if (x.id() == result.itemID && x.meta() == result.getItemDamage())
                sources.add(Pair.of(x.sourceID(), x.sourceMeta()));
        }
        ArrayList<ItemStack> res = new ArrayList<ItemStack>();
        for (Pair<Integer, Integer> p : sources)
            res.add(new ItemStack(p.getLeft(), 1, p.getRight()));
        return res;
    }

    @Override
    public List<ItemStack> getAllValidInputs() {
        HashSet<Pair<Integer, Integer>> sources = new HashSet<Pair<Integer, Integer>>();
        for (SmashableProxy x : HammerRegistryProxy.getRegistry())
            if (x.id() > 0)
                sources.add(Pair.of(x.sourceID(), x.sourceMeta()));

        ArrayList<ItemStack> res = new ArrayList<ItemStack>();
        for (Pair<Integer, Integer> p : sources)
            res.add(new ItemStack(p.getLeft(), 1, p.getRight()));
        return res;
    }
}
