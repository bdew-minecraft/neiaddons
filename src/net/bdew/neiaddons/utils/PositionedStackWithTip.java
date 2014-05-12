/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/neiaddons/master/MMPL-1.0.txt
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
