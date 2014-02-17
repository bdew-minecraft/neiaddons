/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/neiaddons/master/MMPL-1.0.txt
 */

package net.bdew.neiaddons.forestry;

import codechicken.core.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;
import forestry.api.genetics.IAlleleSpecies;
import forestry.api.genetics.IIndividual;
import forestry.api.genetics.IMutation;
import forestry.api.genetics.ISpeciesRoot;
import net.bdew.neiaddons.NEIAddons;
import net.bdew.neiaddons.Utils;
import net.bdew.neiaddons.utils.LabeledPositionedStack;
import net.minecraft.item.ItemStack;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class BaseBreedingRecipeHandler extends TemplateRecipeHandler {

    private final ISpeciesRoot speciesRoot;

    public BaseBreedingRecipeHandler(ISpeciesRoot root) {
        this.speciesRoot = root;
    }

    public class CachedBreedingRecipe extends CachedRecipe {
        LabeledPositionedStack parrent1, parrent2, result;
        public float chance;
        public Collection<String> requirements;

        public CachedBreedingRecipe(IMutation mutation) {
            ItemStack stackParent1 = GeneticsUtils.stackFromSpecies((IAlleleSpecies) mutation.getAllele0(), GeneticsUtils.RecipePosition.Parent1);
            ItemStack stackParent2 = GeneticsUtils.stackFromSpecies((IAlleleSpecies) mutation.getAllele1(), GeneticsUtils.RecipePosition.Parent2);
            ItemStack stackResult = GeneticsUtils.stackFromSpecies((IAlleleSpecies) mutation.getTemplate()[0], GeneticsUtils.RecipePosition.Offspring);

            parrent1 = new LabeledPositionedStack(stackParent1, 22, 19, ((IAlleleSpecies) mutation.getAllele0()).getName(), 13);
            parrent2 = new LabeledPositionedStack(stackParent2, 75, 19, ((IAlleleSpecies) mutation.getAllele1()).getName(), 13);
            result = new LabeledPositionedStack(stackResult, 129, 19, ((IAlleleSpecies) mutation.getTemplate()[0]).getName(), 13);
            chance = mutation.getBaseChance();

            requirements = mutation.getSpecialConditions();
        }

        @Override
        public PositionedStack getResult() {
            return result;
        }

        @Override
        public ArrayList<PositionedStack> getIngredients() {
            ArrayList<PositionedStack> list = new ArrayList<PositionedStack>();
            list.add(parrent1);
            list.add(parrent2);
            return list;
        }
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals("item")) {
            loadCraftingRecipes((ItemStack) results[0]);
            return;
        }

        if (!outputId.equals(getRecipeIdent())) {
            return;
        }

        for (IMutation mutation : speciesRoot.getMutations(false)) {
            if (!mutation.isSecret() || AddonForestry.showSecret) {
                arecipes.add(new CachedBreedingRecipe(mutation));
            }
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        if (NEIAddons.fakeItemsOn) {
            result = NEIAddons.fakeItem.getOriginal(result);
        }
        if (!speciesRoot.isMember(result)) {
            return;
        }
        IIndividual resultIndividual = speciesRoot.getMember(result);
        if (resultIndividual == null) {
            AddonForestry.instance.logWarning("IIndividual is null searching recipe for %s", result.toString());
            return;
        }
        if (resultIndividual.getGenome() == null) {
            AddonForestry.instance.logWarning("Genome is null when searching recipe for %s", result.toString());
            return;
        }
        if (resultIndividual.getGenome().getPrimary() == null) {
            AddonForestry.instance.logWarning("Species is null when searching recipe for %s", result.toString());
            return;
        }
        IAlleleSpecies species = resultIndividual.getGenome().getPrimary();

        for (IMutation mutation : speciesRoot.getMutations(false)) {
            if (mutation.getTemplate()[0].equals(species)) {
                if (!mutation.isSecret() || AddonForestry.showSecret) {
                    arecipes.add(new CachedBreedingRecipe(mutation));
                }
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        if (NEIAddons.fakeItemsOn) {
            ingredient = NEIAddons.fakeItem.getOriginal(ingredient);
        }
        if (!speciesRoot.isMember(ingredient)) {
            return;
        }
        IIndividual individual = speciesRoot.getMember(ingredient);
        if (individual == null) {
            AddonForestry.instance.logWarning("IIndividual is null searching recipe for %s", ingredient.toString());
            return;
        }
        if (individual.getGenome() == null) {
            AddonForestry.instance.logWarning("Genome is null when searching recipe for %s", ingredient.toString());
            return;
        }
        if (individual.getGenome().getPrimary() == null) {
            AddonForestry.instance.logWarning("Species is null when searching recipe for %s", ingredient.toString());
            return;
        }
        IAlleleSpecies species = individual.getGenome().getPrimary();

        for (IMutation mutation : speciesRoot.getMutations(false)) {
            if (mutation.getAllele0().equals(species) || mutation.getAllele1().equals(species)) {
                if (!mutation.isSecret() || AddonForestry.showSecret) {
                    arecipes.add(new CachedBreedingRecipe(mutation));
                }
            }
        }
    }

    @Override
    public void loadTransferRects() {
        transferRects.add(new RecipeTransferRect(new Rectangle(49, 26, 15, 15), getRecipeIdent()));
        transferRects.add(new RecipeTransferRect(new Rectangle(98, 26, 21, 18), getRecipeIdent()));
    }

    @Override
    public void drawExtras(int recipe) {
        CachedBreedingRecipe rec = (CachedBreedingRecipe) arecipes.get(recipe);
        rec.result.drawLabel();
        rec.parrent1.drawLabel();
        rec.parrent2.drawLabel();
        if (rec.requirements.size() > 0 && AddonForestry.showReqs) {
            Utils.drawCenteredString(String.format("[%.0f%%]", rec.chance), 108, 15, 0xFF0000);
        } else {
            Utils.drawCenteredString(String.format("%.0f%%", rec.chance), 108, 15, 0xFFFFFF);
        }
    }

    public abstract String getRecipeIdent();

    @Override
    public String getGuiTexture() {
        return "neiaddons:textures/gui/breeding.png";
    }

    @Override
    public List<String> handleTooltip(GuiRecipe gui, List<String> currenttip, int recipe) {
        CachedBreedingRecipe rec = (CachedBreedingRecipe) arecipes.get(recipe);
        if (AddonForestry.showReqs && rec.requirements.size() > 0 && gui.manager.shouldShowTooltip() && currenttip.size() == 0) {
            Point offset = gui.getRecipePosition(recipe);
            Point pos = GuiDraw.getMousePosition();
            Point relMouse = new Point(pos.x - gui.guiLeft - offset.x, pos.y - gui.guiTop - offset.y);
            Rectangle tiprect = new Rectangle(108 - 24, 15 - 2, 48, 12);
            if (tiprect.contains(relMouse)) {
                currenttip.addAll(rec.requirements);
                return currenttip;
            }
        }
        return super.handleTooltip(gui, currenttip, recipe);
    }
}
