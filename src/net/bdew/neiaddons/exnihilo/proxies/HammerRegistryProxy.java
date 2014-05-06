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

import java.util.ArrayList;
import java.util.List;

public class HammerRegistryProxy {
    private static TypedField<List> f_rewards;

    @SuppressWarnings("unchecked")
    public static List<SmashableProxy> getRegistry() {
        List<Object> list = f_rewards.get(null);
        ArrayList<SmashableProxy> result = new ArrayList<SmashableProxy>();
        for (Object ob : list)
            result.add(new SmashableProxy(ob));
        return result;
    }

    public static void init() throws ClassNotFoundException, NoSuchFieldException {
        Class<?> c_HammerRegistry = Utils.getAndCheckClass("exnihilo.registries.HammerRegistry", Object.class);
        f_rewards = TypedField.from(c_HammerRegistry, "rewards", List.class);
    }
}
