/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/neiaddons/master/MMPL-1.0.txt
 */

package net.bdew.neiaddons;

import codechicken.nei.api.IConfigureNEI;
import net.bdew.neiaddons.api.NEIAddon;

public class NEIAddonsConfig implements IConfigureNEI {

    @Override
    public void loadConfig() {
        for (NEIAddon addon : NEIAddons.addons) {
            if (addon.isActive()) {
                try {
                    addon.loadClient();
                } catch (Throwable e) {
                    NEIAddons.log.warning(String.format("Addon %s failed client initialization: %s", addon.getName(), e.toString()));
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public String getName() {
        return "NEI Addons";
    }

    @Override
    public String getVersion() {
        return "NEIADDONS_VER";
    }
}
