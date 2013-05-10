package net.bdew.neibees;

import net.minecraft.client.gui.FontRenderer;
import codechicken.nei.PositionedStack;

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

    public void drawLabel(FontRenderer f) {
        Utils.drawCenteredString(f, label, relx + 8, rely + 8 + yoffs, 0xFFFFFF);
    }

}
