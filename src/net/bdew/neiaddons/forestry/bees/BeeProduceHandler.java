/**
 * Copyright (c) bdew, 2013
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/neiaddons/master/MMPL-1.0.txt
 */

package net.bdew.neiaddons.forestry.bees;

import java.util.Collection;
import java.util.Map;

import net.bdew.neiaddons.forestry.BaseProduceRecipeHandler;
import forestry.api.apiculture.IAlleleBeeSpecies;
import forestry.api.genetics.IAlleleSpecies;

public class BeeProduceHandler extends BaseProduceRecipeHandler {

    public BeeProduceHandler() {
        super(BeeHelper.root);
    }

    @Override
    public String getRecipeName() {
        return "Bee Produce";
    }

    @Override
    public String getRecipeIdent() {
        return "beeproduce";
    }

    @Override
    public Collection<IAlleleBeeSpecies> getAllSpecies() {
        return BeeHelper.allSpecies;
    }

    @Override
    public Map<Integer, Collection<IAlleleSpecies>> getProduceCache() {
        return BeeHelper.productsCache;
    }

}
