/*
 * Copyright (c) bdew, 2013 - 2015
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.neiaddons.forestry;

import codechicken.nei.api.API;
import net.bdew.neiaddons.utils.CustomOverlayHandler;

class CraftingOverlayHelper {
    public static void setup() {
        CustomOverlayHandler handler = new CustomOverlayHandler(AddonForestry.commandName, -14, 14, false, AddonForestry.SlotCraftMatrix);
        API.registerGuiOverlayHandler(AddonForestry.GuiWorktable, handler, "crafting");
    }
}
