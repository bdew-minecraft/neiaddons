/*
 * Copyright (c) bdew, 2013 - 2015
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.neiaddons.forestry.trees;

import net.bdew.neiaddons.forestry.BaseBreedingRecipeHandler;

public class TreeBreedingHandler extends BaseBreedingRecipeHandler {

    public TreeBreedingHandler() {
        super(TreeHelper.root);
    }

    @Override
    public String getRecipeIdent() {
        return "treebreeding";
    }
}
