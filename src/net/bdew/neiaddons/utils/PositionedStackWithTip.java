/*
 * Copyright (c) bdew, 2013 - 2015
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.neiaddons.utils;

import codechicken.nei.PositionedStack;

import java.util.List;

public class PositionedStackWithTip extends PositionedStack {
    public List<String> tip;

    public PositionedStackWithTip(ItemStackWithTip stack, int x, int y) {
        super(stack.itemStack, x, y);
        this.tip = stack.tip;
    }
}
