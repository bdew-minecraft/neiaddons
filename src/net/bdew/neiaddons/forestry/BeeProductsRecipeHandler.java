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
import java.util.Map.Entry;

import net.bdew.neiaddons.utils.LabeledPositionedStack;
import net.minecraft.item.ItemStack;
import codechicken.nei.NEIClientUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.forge.GuiContainerManager;
import codechicken.nei.recipe.TemplateRecipeHandler;
import forestry.api.apiculture.EnumBeeType;
import forestry.api.apiculture.IAlleleBeeSpecies;

public class BeeProductsRecipeHandler extends TemplateRecipeHandler {

    public class CachedBeeProductRecipe extends CachedRecipe {
        private final LabeledPositionedStack bee;
        private final ArrayList<LabeledPositionedStack> products;

        public String beeName;

        public CachedBeeProductRecipe(IAlleleBeeSpecies species) {
            bee = new LabeledPositionedStack(BeeUtils.stackFromAllele(species, EnumBeeType.QUEEN), 22, 19, species.getName(), 13);

            products = new ArrayList<LabeledPositionedStack>();

            int i = 0;
            for (Entry<ItemStack, Integer> product : species.getProducts().entrySet()) {
                String label = String.format("%d%%", product.getValue());
                products.add(new LabeledPositionedStack(product.getKey(), 96 + 22 * i++, 8, label, 10));
            }

            i = 0;
            for (Entry<ItemStack, Integer> product : species.getSpecialty().entrySet()) {
                String label = String.format("%d%%", product.getValue());
                products.add(new LabeledPositionedStack(product.getKey(), 96 + 22 * i++, 36, label, 10));
            }

            if (products.size() == 0) {
                AddonForestry.instance.logWarning("%s doesn't produce anthing?",species.getUID());
            }
        }

        @Override
        public ArrayList<PositionedStack> getIngredients() {
            ArrayList<PositionedStack> list = new ArrayList<PositionedStack>();
            list.add(bee);
            return list;
        }

        @Override
        public ArrayList<PositionedStack> getOtherStacks() {
            ArrayList<PositionedStack> list = new ArrayList<PositionedStack>();
            if (products.size() > 1) {
                for (int i = 1; i < products.size(); i++) {
                    list.add(products.get(i));
                }
            }
            return list;
        }

        @Override
        public PositionedStack getResult() {
            if (products.size() > 0) {
                return products.get(0);
            } else {
                return null;
            }
        }
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals("item")) {
            loadCraftingRecipes((ItemStack) results[0]);
            return;
        }

        if (!outputId.equals("beeproducts")) { return; }

        for (IAlleleBeeSpecies species : AddonForestry.allBeeSpecies) {
            arecipes.add(new CachedBeeProductRecipe(species));
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        if (!AddonForestry.productsCache.containsKey(result.itemID)) {
            return;
        }
        for (IAlleleBeeSpecies species : AddonForestry.productsCache.get(result.itemID)) {
            CachedBeeProductRecipe recipe = new CachedBeeProductRecipe(species);
            for (LabeledPositionedStack stack : recipe.products) {
                if (NEIClientUtils.areStacksSameTypeCrafting(stack.item, result)) {
                    arecipes.add(recipe);
                    break;
                }
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        if (!AddonForestry.beeRoot.isMember(ingredient)) { return; }
        arecipes.add(new CachedBeeProductRecipe(AddonForestry.beeRoot.getMember(ingredient).getGenome().getPrimary()));
    }

    @Override
    public void loadTransferRects() {
        transferRects.add(new RecipeTransferRect(new Rectangle(48, 22, 21, 15), "beeproducts"));
    }

    @Override
    public void drawExtras(GuiContainerManager gui, int recipe) {
        CachedBeeProductRecipe rec = (CachedBeeProductRecipe) arecipes.get(recipe);
        rec.bee.drawLabel(gui.window.fontRenderer);
        for (LabeledPositionedStack stack : rec.products) {
            stack.drawLabel(gui.window.fontRenderer);
        }
        gui.window.fontRenderer.drawString("Prod:", 65, 8 + 4, 0xFFFFFF);
        gui.window.fontRenderer.drawString("Spec:", 65, 36 + 4, 0xFFF200);
    }

    @Override
    public String getRecipeName() {
        return "Bee Products";
    }

    @Override
    public String getGuiTexture() {
        return "/mods/neiaddons/textures/gui/products.png";
    }
}
