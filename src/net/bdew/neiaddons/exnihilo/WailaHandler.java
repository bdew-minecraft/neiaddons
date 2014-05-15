/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/neiaddons/master/MMPL-1.0.txt
 */

package net.bdew.neiaddons.exnihilo;

import mcp.mobius.waila.api.IWailaRegistrar;
import net.bdew.neiaddons.Utils;
import net.bdew.neiaddons.exnihilo.waila.BarrelHandler;
import net.bdew.neiaddons.exnihilo.waila.BeeTrapHandler;
import net.bdew.neiaddons.exnihilo.waila.CrucibleHandler;
import net.minecraft.tileentity.TileEntity;

public class WailaHandler {
    static public Class<? extends TileEntity> clsTeBarrel;
    static public Class<? extends TileEntity> clsTeCrucible;
    static public Class<? extends TileEntity> clsTeBeeTrap;

    static private void loadClasses() throws ClassNotFoundException, NoSuchMethodException {
        clsTeBarrel = Utils.getAndCheckClass("exnihilo.blocks.tileentities.TileEntityBarrel", TileEntity.class);
        clsTeCrucible = Utils.getAndCheckClass("exnihilo.blocks.tileentities.TileEntityCrucible", TileEntity.class);
        clsTeBeeTrap = Utils.getAndCheckClass("exnihilo.blocks.tileentities.TileEntityBeeTrap", TileEntity.class);
    }

    static public void loadCallback(IWailaRegistrar reg) {
        try {
            loadClasses();
            reg.registerSyncedNBTKey("*", clsTeBarrel);
            reg.registerSyncedNBTKey("*", clsTeCrucible);
            reg.registerSyncedNBTKey("*", clsTeBeeTrap);

            reg.registerBodyProvider(new CrucibleHandler(), clsTeCrucible);
            reg.registerBodyProvider(new BarrelHandler(), clsTeBarrel);
            reg.registerBodyProvider(new BeeTrapHandler(), clsTeBeeTrap);
        } catch (Throwable t) {
            AddonExnihilo.instance.logWarning("WAILA support load failed: %s", t.toString());
        }
    }
}
