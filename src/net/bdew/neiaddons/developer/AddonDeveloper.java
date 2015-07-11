/*
 * Copyright (c) bdew, 2013 - 2015
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.neiaddons.developer;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.bdew.neiaddons.BaseAddon;
import net.bdew.neiaddons.NEIAddons;

@Mod(modid = NEIAddons.modId + "|Developer", name = "NEI Addons: Developer Tools", version = "NEIADDONS_VER", dependencies = "after:NEIAddons")
public class AddonDeveloper extends BaseAddon {

    @Instance(NEIAddons.modId + "|Developer")
    public static AddonDeveloper instance;

    @Override
    public String getName() {
        return "Developer Tools";
    }

    @Override
    public boolean checkSide(Side side) {
        return side.isClient();
    }

    @Override
    public boolean isEnabledByDefault() {
        return false;
    }

    @Override
    @EventHandler
    public void preInit(FMLPreInitializationEvent ev) {
        doPreInit(ev);
    }

    @Override
    public void init(Side side) throws Exception {
        active = true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void loadClient() {
        DeveloperHelper.init();
    }
}
