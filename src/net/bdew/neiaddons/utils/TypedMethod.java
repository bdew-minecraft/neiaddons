/*
 * Copyright (c) bdew, 2013 - 2015
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.neiaddons.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TypedMethod<T> {
    private Method field;
    private Class<T> cls;

    public TypedMethod(Method method, Class<T> cls) {
        this.field = method;
        this.cls = cls;
    }

    @SuppressWarnings("unchecked")
    public T call(Object obj, Object... args) {
        try {
            Object res = field.invoke(obj, args);
            if (cls.isInstance(res))
                return (T) res;
            else
                throw new RuntimeException(String.format("Wrong return type. Expected %s, got %s", cls, res.getClass()));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static <R> TypedMethod<R> from(Class<?> cls, String name, Class<R> result) throws NoSuchMethodException {
        return new TypedMethod<R>(cls.getMethod(name), result);
    }
}
