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

public class SmashableProxy {
    private static Class<?> c_Smashable;
    private static TypedField<Integer> f_sourceID;
    private static TypedField<Integer> f_sourceMeta;
    private static TypedField<Integer> f_id;
    private static TypedField<Integer> f_meta;
    private static TypedField<Float> f_chance;
    private static TypedField<Float> f_luckMultiplier;

    private Object smashable;

    public SmashableProxy(Object smashable) {
        if (!c_Smashable.isInstance(smashable))
            throw new RuntimeException(String.format("Invalid object (%s) passed to proxy (%s)", smashable.toString(), c_Smashable.toString()));
        this.smashable = smashable;
    }

    public int sourceID() {
        return f_sourceID.get(smashable);
    }

    public int sourceMeta() {
        return f_sourceMeta.get(smashable);
    }

    public int id() {
        return f_id.get(smashable);
    }

    public int meta() {
        return f_meta.get(smashable);
    }

    public float chance() {
        return f_chance.get(smashable);
    }

    public float luckMultiplier() {
        return f_luckMultiplier.get(smashable);
    }

    public static void init() throws ClassNotFoundException, NoSuchFieldException {
        c_Smashable = Utils.getAndCheckClass("exnihilo.registries.helpers.Smashable", Object.class);
        f_sourceID = TypedField.from(c_Smashable, "sourceID", Integer.class);
        f_sourceMeta = TypedField.from(c_Smashable, "sourceMeta", Integer.class);
        f_id = TypedField.from(c_Smashable, "id", Integer.class);
        f_meta = TypedField.from(c_Smashable, "meta", Integer.class);
        f_chance = TypedField.from(c_Smashable, "chance", Float.class);
        f_luckMultiplier = TypedField.from(c_Smashable, "luckMultiplier", Float.class);
    }
}
