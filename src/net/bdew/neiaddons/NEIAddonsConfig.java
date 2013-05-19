/**
 * Copyright (c) bdew, 2013
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/neiaddons/master/MMPL-1.0.txt
 */

package net.bdew.neiaddons;

import net.bdew.neiaddons.api.NEIAddon;
import codechicken.nei.api.IConfigureNEI;

public class NEIAddonsConfig implements IConfigureNEI {

    @Override
    public void loadConfig() {
        for (NEIAddon addon : NEIAddons.addons) {
            if (addon.isActive()) {
                addon.loadClient();
            }
        }
    }

    @Override
    public String getName() {
        return "NEI Addons";
    }

    @Override
    public String getVersion() {
        return "@@VERSION@@";
    }
}
