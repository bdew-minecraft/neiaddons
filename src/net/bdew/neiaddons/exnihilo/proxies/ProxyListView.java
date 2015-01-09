/*
 * Copyright (c) bdew, 2013 - 2015
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/neiaddons/master/MMPL-1.0.txt
 */

package net.bdew.neiaddons.exnihilo.proxies;

import com.google.common.base.Throwables;
import net.bdew.neiaddons.exnihilo.AddonExnihilo;

import java.lang.reflect.Constructor;
import java.util.AbstractList;
import java.util.List;

public class ProxyListView<T> extends AbstractList<T> {
    private List<Object> registry;
    private Constructor<T> proxy;

    public ProxyListView(List<Object> registry, Class<T> proxy) {
        try {
            this.proxy = proxy.getConstructor(Object.class);
            this.registry = registry;
        } catch (NoSuchMethodException e) {
            Throwables.propagate(e);
        }
    }

    @Override
    public T get(int index) {
        if (registry.get(index) != null) {
            try {
                return proxy.newInstance(registry.get(index));
            } catch (Throwable e) {
                AddonExnihilo.instance.logWarningExc(e, "Error instantiating proxy %s", proxy.getName());
            }
        }
        return null;
    }

    @Override
    public int size() {
        return registry.size();
    }
}
