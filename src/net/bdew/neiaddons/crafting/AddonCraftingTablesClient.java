/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/neiaddons/master/MMPL-1.0.txt
 */

package net.bdew.neiaddons.crafting;

import net.minecraft.client.gui.inventory.GuiContainer;
import codechicken.nei.api.API;
import codechicken.nei.recipe.DefaultOverlayHandler;

public class AddonCraftingTablesClient {
    public static void load() {
        for (Class<? extends GuiContainer> tableClass : AddonCraftingTables.craftingTables) {
            API.registerGuiOverlay(tableClass, "crafting");
            API.registerGuiOverlayHandler(tableClass, new DefaultOverlayHandler(), "crafting");
        }        
    }
}
