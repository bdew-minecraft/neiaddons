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
import net.bdew.neiaddons.Utils;

public class LabeledPositionedStack extends PositionedStack {

    private final String label;
    private final int yoffs;

    /**
     * @param stack
     * @param x
     * @param y
     * @param label
     * @param yoffs
     */
    public LabeledPositionedStack(Object stack, int x, int y, String label, int yoffs) {
        super(stack, x, y);
        this.label = label;
        this.yoffs = yoffs;
    }

    public void drawLabel() {
        if (label.contains(" ")) {
            String[] parts = label.split(" ");
            for (int i = 0; i < parts.length; i++) {
                Utils.drawCenteredString(parts[i], relx + 8, rely + 8 + yoffs + 9 * i, 0xFFFFFF);
            }
        } else {
            Utils.drawCenteredString(label, relx + 8, rely + 8 + yoffs, 0xFFFFFF);
        }
    }
}
