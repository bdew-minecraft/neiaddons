/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/neiaddons/master/MMPL-1.0.txt
 */

package net.bdew.neiaddons.forestry;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import forestry.api.genetics.IIndividual;
import net.bdew.neiaddons.NEIAddons;
import net.bdew.neiaddons.Utils;
import net.bdew.neiaddons.utils.LabeledPositionedStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import codechicken.nei.NEIClientUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import forestry.api.genetics.IAlleleSpecies;
import forestry.api.genetics.ISpeciesRoot;

public abstract class BaseProduceRecipeHandler extends TemplateRecipeHandler {

    private final ISpeciesRoot speciesRoot;
    private final Map<Integer, Collection<IAlleleSpecies>> cache;

    public BaseProduceRecipeHandler(ISpeciesRoot root) {
        this.speciesRoot = root;
        cache = getProduceCache();
    }

    public class CachedProduceRecipe extends CachedRecipe {
        private LabeledPositionedStack producer;
        private ArrayList<LabeledPositionedStack> products;

        public CachedProduceRecipe(IAlleleSpecies species) {
            ItemStack producerStack = GeneticsUtils.stackFromSpecies(species, GeneticsUtils.RecipePosition.Producer);
            if (producerStack==null) {
                AddonForestry.instance.logWarning("Producer is null... wtf? species = %s", species.getUID());
            } else {
                producer = new LabeledPositionedStack(producerStack, 22, 19, species.getName(), 13);
            }

            products = new ArrayList<LabeledPositionedStack>();

            int i = 0;
            for (Entry<ItemStack, Integer> product : Utils.mergeStacks(GeneticsUtils.getProduceFromSpecies(species)).entrySet()) {
                String label = String.format("%d%%", product.getValue());
                products.add(new LabeledPositionedStack(product.getKey(), 96 + 22 * i++, 8, label, 10));
            }

            i = 0;
            for (Entry<ItemStack, Integer> product : Utils.mergeStacks(GeneticsUtils.getSpecialtyFromSpecies(species)).entrySet()) {
                String label = String.format("%d%%", product.getValue());
                products.add(new LabeledPositionedStack(product.getKey(), 96 + 22 * i++, 36, label, 10));
            }
        }

        public boolean isNoOutput() {
            return products.size()==0;
        }

        @Override
        public ArrayList<PositionedStack> getIngredients() {
            ArrayList<PositionedStack> list = new ArrayList<PositionedStack>();
            list.add(producer);
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

        if (!outputId.equals(getRecipeIdent())) { return; }

        for (IAlleleSpecies species : getAllSpecies()) {
            CachedProduceRecipe rec = new CachedProduceRecipe(species);
            if (!rec.isNoOutput()) {
                arecipes.add(rec);
            }
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        if (cache == null) return;
        if (result == null) {
            AddonForestry.instance.logWarning("loadCraftingRecipes() called with null, something is FUBAR.");
            return;
        }
        if (NEIAddons.fakeItemsOn) {
            result = NEIAddons.fakeItem.getOriginal(result);
            if (result == null) return;
        }
        if (!cache.containsKey(result.itemID)) { return; }
        for (IAlleleSpecies species : cache.get(result.itemID)) {
            CachedProduceRecipe recipe = new CachedProduceRecipe(species);
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
        if (NEIAddons.fakeItemsOn) {
            ingredient = NEIAddons.fakeItem.getOriginal(ingredient);
        }
        if (!speciesRoot.isMember(ingredient)) { return; }
        IIndividual member = speciesRoot.getMember(ingredient);
        if (member == null || member.getGenome() == null || member.getGenome().getPrimary() == null) {
            AddonForestry.instance.logWarning("Individual or genome is null searching recipe for %s",ingredient.toString());
            return;
        }
        arecipes.add(new CachedProduceRecipe(member.getGenome().getPrimary()));
    }

    @Override
    public void loadTransferRects() {
        transferRects.add(new RecipeTransferRect(new Rectangle(48, 22, 21, 15), getRecipeIdent()));
    }

    @Override
    public void drawExtras(int recipe) {
        CachedProduceRecipe rec = (CachedProduceRecipe) arecipes.get(recipe);
        rec.producer.drawLabel();
        for (LabeledPositionedStack stack : rec.products) {
            stack.drawLabel();
        }
        FontRenderer f = Minecraft.getMinecraft().fontRenderer;
        f.drawString("Prod:", 65, 8 + 4, 0xFFFFFF);
        f.drawString("Spec:", 65, 36 + 4, 0xFFF200);
    }

    public abstract String getRecipeIdent();

    public abstract Collection<? extends IAlleleSpecies> getAllSpecies();

    public abstract Map<Integer, Collection<IAlleleSpecies>> getProduceCache();

    @Override
    public String getGuiTexture() {
        return "neiaddons:textures/gui/products.png";
    }
}
