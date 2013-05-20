/**
 * Copyright (c) bdew, 2013
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/neiaddons/master/MMPL-1.0.txt
 */

package net.bdew.neiaddons.forestry;

import java.awt.Rectangle;
import java.util.ArrayList;

import net.bdew.neiaddons.Utils;
import net.bdew.neiaddons.utils.LabeledPositionedStack;
import net.minecraft.item.ItemStack;
import codechicken.nei.PositionedStack;
import codechicken.nei.forge.GuiContainerManager;
import codechicken.nei.recipe.TemplateRecipeHandler;
import forestry.api.apiculture.BeeManager;
import forestry.api.apiculture.EnumBeeType;
import forestry.api.apiculture.IBee;
import forestry.api.apiculture.IBeeMutation;
import forestry.api.genetics.IAlleleSpecies;
import forestry.api.genetics.IMutation;

public class BeeBreedingRecipeHandler extends TemplateRecipeHandler {

    public class CachedBeeBreedingRecipe extends CachedRecipe {
        LabeledPositionedStack bee1, bee2, result;
        public float chance;

        public CachedBeeBreedingRecipe(IMutation mutation) {

            bee1 = new LabeledPositionedStack(BeeUtils.stackFromAllele(mutation.getAllele0(), EnumBeeType.PRINCESS), 22, 19, ((IAlleleSpecies) mutation.getAllele0()).getName(), 13);
            bee2 = new LabeledPositionedStack(BeeUtils.stackFromAllele(mutation.getAllele1(), EnumBeeType.DRONE), 75, 19, ((IAlleleSpecies) mutation.getAllele1()).getName(), 13);
            result = new LabeledPositionedStack(BeeUtils.stackFromAllele(mutation.getTemplate()[0], EnumBeeType.QUEEN), 129, 19, ((IAlleleSpecies) mutation.getTemplate()[0]).getName(), 13);
            chance = mutation.getBaseChance();
        }

        @Override
        public PositionedStack getResult() {
            return result;
        }

        @Override
        public ArrayList<PositionedStack> getIngredients() {
            ArrayList<PositionedStack> list = new ArrayList<PositionedStack>();
            list.add(bee1);
            list.add(bee2);
            return list;
        }
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals("item")) {
            loadCraftingRecipes((ItemStack) results[0]);
            return;
        }

        if (!outputId.equals("beebreeding")) { return; }

        for (IBeeMutation mutation : BeeManager.breedingManager.getMutations(false)) {
            if (!mutation.isSecret() || AddonForestry.showSecret) {
                arecipes.add(new CachedBeeBreedingRecipe(mutation));
            }
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        if (!BeeManager.beeInterface.isBee(result)) { return; }
        IBee resultbee = BeeManager.beeInterface.getBee(result);
        IAlleleSpecies species = resultbee.getGenome().getPrimary();

        for (IBeeMutation mutation : BeeManager.breedingManager.getMutations(false)) {
            if ((IAlleleSpecies) mutation.getTemplate()[0] == species) {
                if (!mutation.isSecret() || AddonForestry.showSecret) {
                    arecipes.add(new CachedBeeBreedingRecipe(mutation));
                }
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        if (!BeeManager.beeInterface.isBee(ingredient)) { return; }
        IBee resultbee = BeeManager.beeInterface.getBee(ingredient);
        IAlleleSpecies species = resultbee.getGenome().getPrimary();

        for (IBeeMutation mutation : BeeManager.breedingManager.getMutations(false)) {
            if ((IAlleleSpecies) mutation.getAllele0() == species || (IAlleleSpecies) mutation.getAllele1() == species) {
                if (!mutation.isSecret() || AddonForestry.showSecret) {
                    arecipes.add(new CachedBeeBreedingRecipe(mutation));
                }
            }
        }
    }

    @Override
    public void loadTransferRects() {
        transferRects.add(new RecipeTransferRect(new Rectangle(49, 26, 15, 15), "beebreeding"));
        transferRects.add(new RecipeTransferRect(new Rectangle(98, 26, 21, 18), "beebreeding"));
    }

    @Override
    public void drawExtras(GuiContainerManager gui, int recipe) {
        CachedBeeBreedingRecipe rec = (CachedBeeBreedingRecipe) arecipes.get(recipe);
        rec.result.drawLabel(gui.window.fontRenderer);
        rec.bee1.drawLabel(gui.window.fontRenderer);
        rec.bee2.drawLabel(gui.window.fontRenderer);
        Utils.drawCenteredString(gui.window.fontRenderer, String.format("%.0f%%", rec.chance), 108, 15, 0xFFFFFF);
    }

    @Override
    public String getRecipeName() {
        return "Bee Breeding";
    }

    @Override
    public String getGuiTexture() {
        return "/mods/neiaddons/textures/gui/breeding.png";
    }
}
