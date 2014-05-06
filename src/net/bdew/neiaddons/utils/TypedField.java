/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/neiaddons/master/MMPL-1.0.txt
 */

package net.bdew.neiaddons.utils;

import java.lang.reflect.Field;

public class TypedField<T> {
    private Field field;
    private Class<T> cls;

    public TypedField(Field field, Class<T> cls) {
        this.field = field;
        this.cls = cls;
    }

    @SuppressWarnings("unchecked")
    public T get(Object obj) {
        try {
            Object res = field.get(obj);
            if (cls.isInstance(res))
                return (T) res;
            else
                throw new RuntimeException(String.format("Wrong field type. Expected %s, got %s", cls, res.getClass()));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static <R> TypedField<R> from(Class<?> cls, String name, Class<R> result) throws NoSuchFieldException {
        return new TypedField<R>(cls.getField(name), result);
    }
}
