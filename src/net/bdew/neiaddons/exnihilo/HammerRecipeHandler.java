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
import net.bdew.neiaddons.utils.ItemStackWithChance;
import net.bdew.neiaddons.utils.PositionedStackWithChance;
import net.minecraft.item.ItemStack;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class HammerRecipeHandler extends TemplateRecipeHandler {

    public class CachedHammerRecipe extends CachedRecipe {
        PositionedStack hammer;
        PositionedStack input;
        List<PositionedStackWithChance> output;

        public CachedHammerRecipe(ItemStack source, List<ItemStackWithChance> drops) {
            hammer = new PositionedStack(AddonExnihilo.hammers, 65 - 5, 24 - 11);
            input = new PositionedStack(source, 34 - 5, 35 - 11);
            output = new ArrayList<PositionedStackWithChance>();

            int pos = 0;
            for (ItemStackWithChance x : drops) {
                output.add(new PositionedStackWithChance(x, 92 - 5 + (pos % 3) * 18, 17 - 11 + (pos / 3) * 18));
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
            list.add(hammer);
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
        for (ItemStack source : ExnihiloUtils.getAllHammerSources())
            arecipes.add(new CachedHammerRecipe(source, ExnihiloUtils.getHammerDrops(source)));
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(getRecipeIdent())) addAllRecipes();
        super.loadCraftingRecipes(outputId, results);
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        if (!AddonExnihiloClient.hammerDropIds.contains(result.itemID)) return;

        for (ItemStack source : ExnihiloUtils.getHammerSourcesFor(result))
            arecipes.add(new CachedHammerRecipe(source, ExnihiloUtils.getHammerDrops(source)));
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        if (AddonExnihilo.clsBaseHammer.isInstance(ingredient.getItem())) addAllRecipes();
        if (!AddonExnihiloClient.hammerSourceIds.contains(ingredient.itemID)) return;
        List<ItemStackWithChance> drops = ExnihiloUtils.getHammerDrops(ingredient);
        if (drops.size() > 0)
            arecipes.add(new CachedHammerRecipe(ingredient, drops));
    }

    @Override
    public void loadTransferRects() {
        transferRects.add(new RecipeTransferRect(new Rectangle(62 - 5, 42 - 11, 22, 15), getRecipeIdent()));
    }

    @Override
    public List<String> handleItemTooltip(GuiRecipe gui, ItemStack stack, List<String> currenttip, int recipe) {
        if (stack != null)
            for (PositionedStackWithChance x : ((CachedHammerRecipe) arecipes.get(recipe)).output)
                if (gui.isMouseOver(x, recipe))
                    currenttip.add(String.format("Drop chance: %.0f%%", x.chance * 100));
        return super.handleTooltip(gui, currenttip, recipe);
    }

    @Override
    public String getRecipeName() {
        return "ExNihilo Hammer";
    }

    public String getRecipeIdent() {
        return "ExNihiloHammer";
    }

    @Override
    public String getGuiTexture() {
        return "neiaddons:textures/gui/exnihilo.png";
    }
}
