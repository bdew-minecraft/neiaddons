/*
 * Copyright (c) bdew, 2013 - 2015
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.neiaddons.exnihilo;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.bdew.neiaddons.BaseAddon;
import net.bdew.neiaddons.NEIAddons;

@Mod(modid = NEIAddons.modId + "|ExNihilo", name = "NEI Addons: Ex Nihilo", version = "NEIADDONS_VER", dependencies = "after:exnihilo")
public class AddonExnihilo extends BaseAddon {
    @Mod.Instance(NEIAddons.modId + "|ExNihilo")
    public static AddonExnihilo instance;

    @Override
    public String getName() {
        return "Ex Nihilo";
    }

    @Override
    public boolean checkSide(Side side) {
        return side.isClient();
    }

    @Override
    public String[] getDependencies() {
        return new String[]{"exnihilo", "Waila"};
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent ev) {
        doPreInit(ev);
    }

    @Override
    public void init(Side side) throws Exception {
        active = true;
        FMLInterModComms.sendMessage("Waila", "register", "net.bdew.neiaddons.exnihilo.WailaHandler.loadCallback");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void loadClient() {
        // Currently this addon includes only some WAILA handlers
    }
}
