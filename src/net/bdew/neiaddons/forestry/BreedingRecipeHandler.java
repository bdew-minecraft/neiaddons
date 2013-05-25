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
import forestry.api.genetics.IAlleleSpecies;
import forestry.api.genetics.IIndividual;
import forestry.api.genetics.IMutation;
import forestry.api.genetics.ISpeciesRoot;

public abstract class BreedingRecipeHandler extends TemplateRecipeHandler {

    private ISpeciesRoot speciesRoot;
    private String recipeName;
    private String recipeIdent;
    
    public BreedingRecipeHandler(ISpeciesRoot root, String recipeName, String recipeIdent) {
        this.speciesRoot = root;
        this.recipeName = recipeName;
        this.recipeIdent = recipeIdent;
    }
    
    public class CachedBreedingRecipe extends CachedRecipe {
        LabeledPositionedStack parrent1, parrent2, result;
        public float chance;

        public CachedBreedingRecipe(IMutation mutation) {

            ItemStack stackParent1 = BeeUtils.stackFromSecies((IAlleleSpecies) mutation.getAllele0(), BeeUtils.RecipePosition.Parent1);
            ItemStack stackParent2 = BeeUtils.stackFromSecies((IAlleleSpecies) mutation.getAllele1(), BeeUtils.RecipePosition.Parent2);
            ItemStack stackResult = BeeUtils.stackFromSecies((IAlleleSpecies) mutation.getTemplate()[0], BeeUtils.RecipePosition.Offspring);

            parrent1 = new LabeledPositionedStack(stackParent1, 22, 19, ((IAlleleSpecies) mutation.getAllele0()).getName(), 13);
            parrent2 = new LabeledPositionedStack(stackParent2, 75, 19, ((IAlleleSpecies) mutation.getAllele1()).getName(), 13);
            result = new LabeledPositionedStack(stackResult, 129, 19, ((IAlleleSpecies) mutation.getTemplate()[0]).getName(), 13);
            chance = mutation.getBaseChance();
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

        if (!outputId.equals(recipeIdent)) { return; }

        for (IMutation mutation : speciesRoot.getMutations(false)) {
            if (!mutation.isSecret() || AddonForestry.showSecret) {
                arecipes.add(new CachedBreedingRecipe(mutation));
            }
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        if (!speciesRoot.isMember(result)) { return; }
        IIndividual resultIndividual = speciesRoot.getMember(result);
        IAlleleSpecies species = resultIndividual.getGenome().getPrimary();

        for (IMutation mutation : speciesRoot.getMutations(false)) {
            if ((mutation.getTemplate()[0].equals(species))) {
                if (!mutation.isSecret() || AddonForestry.showSecret) {
                    arecipes.add(new CachedBreedingRecipe(mutation));
                }
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        if (!speciesRoot.isMember(ingredient)) { return; }
        IIndividual individual = speciesRoot.getMember(ingredient);
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
        transferRects.add(new RecipeTransferRect(new Rectangle(49, 26, 15, 15), recipeIdent));
        transferRects.add(new RecipeTransferRect(new Rectangle(98, 26, 21, 18), recipeIdent));
    }

    @Override
    public void drawExtras(GuiContainerManager gui, int recipe) {
        CachedBreedingRecipe rec = (CachedBreedingRecipe) arecipes.get(recipe);
        rec.result.drawLabel(gui.window.fontRenderer);
        rec.parrent1.drawLabel(gui.window.fontRenderer);
        rec.parrent2.drawLabel(gui.window.fontRenderer);
        Utils.drawCenteredString(gui.window.fontRenderer, String.format("%.0f%%", rec.chance), 108, 15, 0xFFFFFF);
    }

    @Override
    public String getRecipeName() {
        return recipeName;
    }

    @Override
    public String getGuiTexture() {
        return "/mods/neiaddons/textures/gui/breeding.png";
    }
}
