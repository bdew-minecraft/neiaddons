/**
 * Copyright (c) bdew, 2013
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/neiaddons/master/MMPL-1.0.txt
 */

package net.bdew.neiaddons.extrabees;

import java.awt.Rectangle;

import net.bdew.neiaddons.forestry.GeneticsUtils;
import net.bdew.neiaddons.utils.LabeledPositionedStack;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import codechicken.nei.PositionedStack;
import codechicken.nei.forge.GuiContainerManager;
import codechicken.nei.recipe.TemplateRecipeHandler;
import forestry.api.apiculture.IAlleleBeeSpecies;
import forestry.api.genetics.IAllele;
import forestry.api.genetics.IAlleleSpecies;

public class IsolatorRecipeHandler extends TemplateRecipeHandler {

    public class CachedSerumRecipe extends CachedRecipe {
        LabeledPositionedStack bee;
        PositionedStack serum;

        public CachedSerumRecipe(IAlleleSpecies species, ItemStack serumStack) {
            bee = new LabeledPositionedStack(GeneticsUtils.stackFromSpecies(species, GeneticsUtils.RecipePosition.Offspring), 22, 21, species.getName(), 13);
            serum = new PositionedStack(serumStack, 129, 25);
        }

        @Override
        public PositionedStack getResult() {
            return serum;
        }

        @Override
        public PositionedStack getIngredient() {
            return bee;
        }
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals("item")) {
            loadCraftingRecipes((ItemStack) results[0]);
            return;
        }

        if (!outputId.equals("isolator")) { return; }

        for (IAlleleBeeSpecies species : AddonExtraBees.allBeeSpecies) {
            IAllele[] template = AddonExtraBeesClient.beeRoot.getTemplate(species.getUID());
            if (template==null) {
                AddonExtraBees.instance.logWarning("Template for %s is null, wtf?", species.getUID());
                continue;
            }
            for (int i = 0; i < template.length; i++) {
                if (template[i] != null) {
                    if (SerumUtils.shouldMakeSerum(template[i].getUID(), i)) {
                        ItemStack serum = SerumUtils.getSerum(template[i].getUID(), i);
                        arecipes.add(new CachedSerumRecipe(species, serum));
                    }
                }
            }
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        if (!SerumUtils.isSerum(result)) { return; }

        AlleleBeeChromosomePair pair = SerumUtils.getData(result);

        if (pair == null) { return; }

        for (IAlleleBeeSpecies species : AddonExtraBees.allBeeSpecies) {
            IAllele[] template = AddonExtraBeesClient.beeRoot.getTemplate(species.getUID());
            if (template==null) {
                AddonExtraBees.instance.logWarning("Template for %s is null, wtf?", species.getUID());
                continue;
            }
            if (template[pair.chromosome] == null) continue;
            if (template[pair.chromosome].getUID().equals(pair.allele)) {
                arecipes.add(new CachedSerumRecipe(species, result));
            }
        }
    }

    @Override
    public void loadTransferRects() {
        transferRects.add(new RecipeTransferRect(new Rectangle(60, 26, 46, 15), "isolator"));
    }

    @Override
    public void drawExtras(GuiContainerManager gui, int recipe) {
        CachedSerumRecipe rec = (CachedSerumRecipe) arecipes.get(recipe);
        rec.bee.drawLabel();
    }

    @Override
    public String getRecipeName() {
        return "Isolator";
    }

    @Override
    public String getGuiTexture() {
        return "/mods/neiaddons/textures/gui/isolator.png";
    }
}
