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