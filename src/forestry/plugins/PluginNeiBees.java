/**
 * Copyright (c) bdew, 2013
 * https://github.com/bdew/neibees
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/neibees/master/MMPL-1.0.txt
 */

package forestry.plugins;

import net.bdew.neibees.NeiBees;
import forestry.api.core.IPlugin;
import forestry.api.core.PluginInfo;

@PluginInfo(name = "NEI Bees Plugin", pluginID = "neibees", version = "@@VERSION@@")
public class PluginNeiBees implements IPlugin {
    public boolean isAvailable() {
        return true;
    }

    public void preInit() {
    }

    public void doInit() {
        NeiBees.log.fine("NEI Bees Plugin Loaded!");
    }

    public void postInit() {
    }

    public static void modLoaded() {
    }
}