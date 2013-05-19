/**
 * Copyright (c) bdew, 2013
 * https://github.com/bdew/neibees
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/neibees/master/MMPL-1.0.txt
 */

package net.bdew.neibees;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import forestry.api.apiculture.EnumBeeType;
import forestry.api.apiculture.IBee;
import forestry.api.apiculture.IBeeGenome;
import forestry.api.genetics.IAllele;
import forestry.api.genetics.IAlleleSpecies;

public final class Utils {
    public static void drawCenteredString(FontRenderer f, String s, int x, int y, int color) {
        f.drawString(s, x - f.getStringWidth(s) / 2, y, color);
    }

    public static ItemStack stackFromAllele(IAllele allele, EnumBeeType type) {
        assert allele instanceof IAlleleSpecies;
        IAlleleSpecies species = (IAlleleSpecies) allele;
        IAllele[] template = NEIBeesConfig.beeRoot.getTemplate(species.getUID());
        IBeeGenome genome = NEIBeesConfig.beeRoot.templateAsGenome(template);
        IBee bee = NEIBeesConfig.beeRoot.getBee(Minecraft.getMinecraft().theWorld, genome);
        bee.analyze();
        return NEIBeesConfig.beeRoot.getMemberStack(bee, type.ordinal());
    }
}
