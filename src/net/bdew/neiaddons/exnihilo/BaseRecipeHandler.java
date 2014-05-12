/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/neiaddons/master/MMPL-1.0.txt
 */

package net.bdew.neiaddons.exnihilo;

import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;
import net.bdew.neiaddons.utils.ItemStackWithTip;
import net.bdew.neiaddons.utils.PositionedStackWithTip;
import net.minecraft.item.ItemStack;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecipeHandler extends TemplateRecipeHandler {
    public abstract String getRecipeName();

    public abstract String getRecipeId();

    public abstract List<ItemStack> getTools();

    public abstract boolean isValidTool(ItemStack tool);

    public abstract boolean isPossibleInput(ItemStack tool);

    public abstract boolean isPossibleOutput(ItemStack tool);

    public abstract List<ItemStackWithTip> getProcessingResults(ItemStack from);

    public abstract List<ItemStack> getInputsFor(ItemStack result);

    public abstract List<ItemStack> getAllValidInputs();

    public class CachedExnihiloRecipe extends CachedRecipe {
        PositionedStack tool;
        PositionedStack input;
        List<PositionedStackWithTip> output;

        public CachedExnihiloRecipe(ItemStack source, List<ItemStackWithTip> drops) {
            tool = new PositionedStack(getTools(), 35 - 5, 24 - 11);
            input = new PositionedStack(source, 8 - 5, 35 - 11);
            output = new ArrayList<PositionedStackWithTip>();

            int pos = 0;
            for (ItemStackWithTip x : drops) {
                output.add(new PositionedStackWithTip(x, 62 - 5 + (pos % 6) * 18, 17 - 11 + (pos / 6) * 18));
                pos++;
            }
        }

        @Override
        public PositionedStack getIngredient() {
            return input;
        }

        @Override
        public ArrayList<PositionedStack> getOtherStacks() {
            ArrayList<PositionedStack> list = new ArrayList<PositionedStack>();
            if (output.size() > 1) {
                for (int i = 1; i < output.size(); i++) {
                    list.add(output.get(i));
                }
            }
            tool.setPermutationToRender(cycleticks / 20 % (getTools().size()));
            list.add(tool);
            return list;
        }

        @Override
        public PositionedStack getResult() {
            if (output.size() > 0) {
                return output.get(0);
            } else {
                return null;
            }
        }
    }

    private void addAllRecipes() {
        for (ItemStack source : getAllValidInputs())
            arecipes.add(new CachedExnihiloRecipe(source, getProcessingResults(source)));
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(getRecipeId())) addAllRecipes();
        super.loadCraftingRecipes(outputId, results);
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        if (!isPossibleOutput(result)) return;

        for (ItemStack source : getInputsFor(result))
            arecipes.add(new CachedExnihiloRecipe(source, getProcessingResults(source)));
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        if (isValidTool(ingredient)) addAllRecipes();
        if (!isPossibleInput(ingredient)) return;
        List<ItemStackWithTip> drops = getProcessingResults(ingredient);
        if (drops.size() > 0)
            arecipes.add(new CachedExnihiloRecipe(ingredient, drops));
    }

    @Override
    public void loadTransferRects() {
        transferRects.add(new RecipeTransferRect(new Rectangle(32 - 5, 42 - 11, 22, 15), getRecipeId()));
    }

    @Override
    public List<String> handleItemTooltip(GuiRecipe gui, ItemStack stack, List<String> currenttip, int recipe) {
        if (stack != null)
            for (PositionedStackWithTip x : ((CachedExnihiloRecipe) arecipes.get(recipe)).output)
                if (gui.isMouseOver(x, recipe))
                    currenttip.addAll(x.tip);
        return super.handleTooltip(gui, currenttip, recipe);
    }

    @Override
    public String getGuiTexture() {
        return "neiaddons:textures/gui/exnihilo.png";
    }
}
