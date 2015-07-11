/*
 * Copyright (c) bdew, 2013 - 2015
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.neiaddons.appeng;

import net.minecraft.inventory.Slot;

public class SlotHelper {
    public static boolean isSlotEnabled(Slot s) {
        if (AddonAppeng.clsSlotFake.isInstance(s)) {
            try {
                return (Boolean) AddonAppeng.mSlotFakeIsEnabled.invoke(s);
            } catch (Throwable e) {
                return false;
            }
        } else {
            return false;
        }
    }
}
