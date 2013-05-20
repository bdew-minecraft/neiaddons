/**
 * Copyright (c) bdew, 2013
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/neiaddons/master/MMPL-1.0.txt
 */

package forestry.plugins;

import java.util.Random;

import net.bdew.neiaddons.NEIAddons;
import net.minecraft.command.ICommand;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;
import forestry.api.core.IOreDictionaryHandler;
import forestry.api.core.IPacketHandler;
import forestry.api.core.IPickupHandler;
import forestry.api.core.IPlugin;
import forestry.api.core.IResupplyHandler;
import forestry.api.core.ISaveEventHandler;
import forestry.api.core.PluginInfo;

@PluginInfo(name = "NEI Addons Plugin", pluginID = "neiaddons", version = "@@VERSION@@")
public class PluginNEIAddons implements IPlugin {
    public boolean isAvailable() {
        return true;
    }

    public void preInit() {
    }

    public void doInit() {
        NEIAddons.log.fine("Forestry Plugin loaded");
    }

    public void postInit() {
    }

    public static void modLoaded() {
    }

    @Override
    public String getDescription() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void generateSurface(World world, Random rand, int chunkX, int chunkZ) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public IGuiHandler getGuiHandler() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IPacketHandler getPacketHandler() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IPickupHandler getPickupHandler() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IResupplyHandler getResupplyHandler() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ISaveEventHandler getSaveEventHandler() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IOreDictionaryHandler getDictionaryHandler() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ICommand[] getConsoleCommands() {
        // TODO Auto-generated method stub
        return null;
    }
}