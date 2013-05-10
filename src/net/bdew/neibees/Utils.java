package net.bdew.neibees;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import forestry.api.apiculture.BeeManager;
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
        IAllele[] template = BeeManager.breedingManager.getBeeTemplate(species.getUID());
        IBeeGenome genome = BeeManager.beeInterface.templateAsGenome(template);
        IBee bee = BeeManager.beeInterface.getBee(Minecraft.getMinecraft().theWorld, genome);
        bee.analyze();
        return BeeManager.beeInterface.getBeeStack(bee, type);
    }
}
