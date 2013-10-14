/**
 * Copyright (c) bdew, 2013
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/neiaddons/master/MMPL-1.0.txt
 */

package net.bdew.neiaddons.miscperipherals;

import net.bdew.neiaddons.utils.CustomOverlayHandler;
import codechicken.nei.api.API;

public class AddonMiscPeripheralsClient {

    public static void load() {
        CustomOverlayHandler handler = new CustomOverlayHandler(AddonMiscPeripherals.commandName, 5, 11, AddonMiscPeripherals.invertShift, AddonMiscPeripherals.SlotRO);
        API.registerGuiOverlayHandler(AddonMiscPeripherals.GuiCrafter, handler, "crafting");
    }

}
