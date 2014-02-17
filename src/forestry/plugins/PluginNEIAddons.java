/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/neiaddons/master/MMPL-1.0.txt
 */

package forestry.plugins;

import forestry.api.core.IPlugin;
import forestry.api.core.PluginInfo;
import net.bdew.neiaddons.forestry.AddonForestry;

@PluginInfo(name = "NEI Addons Plugin", pluginID = "neiaddons", version = "NEIADDONS_VER")
public class PluginNEIAddons implements IPlugin {
    public boolean isAvailable() {
        return true;
    }

    public void preInit() {
    }

    public void doInit() {
        AddonForestry.instance.logInfo("Forestry Plugin loaded");
    }

    public void postInit() {
    }

    public static void modLoaded() {
    }
}