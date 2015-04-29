/*
 * Copyright (c) bdew, 2013 - 2015
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.neiaddons.crafting;

import codechicken.nei.api.API;
import codechicken.nei.recipe.DefaultOverlayHandler;
import net.minecraft.client.gui.inventory.GuiContainer;

class AddonCraftingTablesClient {
    public static void load() {
        for (Class<? extends GuiContainer> tableClass : AddonCraftingTables.craftingTables) {
            API.registerGuiOverlay(tableClass, "crafting");
            API.registerGuiOverlayHandler(tableClass, new DefaultOverlayHandler(), "crafting");
        }
    }
}
