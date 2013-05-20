/**
 * Copyright (c) bdew, 2013
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/neiaddons/master/MMPL-1.0.txt
 */

package net.bdew.neiaddons;

import net.minecraft.client.gui.FontRenderer;

public class Utils {
    @SuppressWarnings("unchecked")
    public static <T> Class<? extends T> getAndCheckClass(String cls, Class<? extends T> sup) throws ClassNotFoundException {
        Class<?> c = Class.forName(cls);
        if (c != null) {
            if (sup.isAssignableFrom(c)) {
                return (Class<? extends T>) c;
            } else {
                throw new RuntimeException(cls + " doesn't extend " + sup.getName());
            }
        } else {
            throw new RuntimeException("Can't get " + cls);
        }
    }

    public static void drawCenteredString(FontRenderer f, String s, int x, int y, int color) {
        f.drawString(s, x - f.getStringWidth(s) / 2, y, color);
    }
}
