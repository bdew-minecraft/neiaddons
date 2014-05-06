/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/neiaddons/master/MMPL-1.0.txt
 */

package net.bdew.neiaddons.exnihilo.proxies;

import java.util.AbstractList;
import java.util.List;

public class SieveListView extends AbstractList<SiftRewardProxy> {
    private List<Object> registry;

    public SieveListView(List<Object> registry) {
        this.registry = registry;
    }

    @Override
    public SiftRewardProxy get(int index) {
        return new SiftRewardProxy(registry.get(index));
    }

    @Override
    public int size() {
        return registry.size();
    }
}

