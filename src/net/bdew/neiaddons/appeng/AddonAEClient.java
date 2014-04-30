/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/neiaddons/master/MMPL-1.0.txt
 */

package net.bdew.neiaddons.appeng;

import codechicken.nei.api.API;
import net.bdew.neiaddons.utils.CustomOverlayHandler;

class AddonAEClient {
    public static void load() {
        CustomOverlayHandler handler = new CustomOverlayHandler(AddonAE.commandName, 5, 17, AddonAE.invertShift, AddonAE.SlotFake);
        API.registerGuiOverlayHandler(AddonAE.GuiPatternEncoder, handler, "crafting");
    }
}
