/**
 * Copyright (c) bdew, 2013
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/neiaddons/master/MMPL-1.0.txt
 */

package net.bdew.neiaddons.forestry.trees;

import java.util.Collection;
import java.util.Map;

import net.bdew.neiaddons.forestry.BaseProduceRecipeHandler;
import forestry.api.arboriculture.IAlleleTreeSpecies;
import forestry.api.genetics.IAlleleSpecies;

public class TreeProduceHandler extends BaseProduceRecipeHandler {

    public TreeProduceHandler() {
        super(TreeHelper.root);
    }

    @Override
    public String getRecipeName() {
        return "Tree Produce";
    }

    @Override
    public String getRecipeIdent() {
        return "treeproduce";
    }

    @Override
    public Collection<IAlleleTreeSpecies> getAllSpecies() {
        return TreeHelper.allSpecies;
    }

    @Override
    public Map<Integer, Collection<IAlleleSpecies>> getProduceCache() {
        return TreeHelper.productsCache;
    }

}
