/**
 * Copyright (c) bdew, 2013
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/neiaddons/master/MMPL-1.0.txt
 */

package net.bdew.neiaddons.extrabees;

import java.util.Collection;

import net.bdew.neiaddons.BaseAddon;
import net.bdew.neiaddons.NEIAddons;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import forestry.api.apiculture.IAlleleBeeSpecies;

@Mod(modid = NEIAddons.modid + "|ExtraBees", name = "NEI Addons: Extra Bees", version = "@@VERSION@@", dependencies = "after:NEIAddons;after:ExtraBees;after:Forestry")
public class AddonExtraBees extends BaseAddon {

    public static boolean loadBlacklisted;
    public static boolean dumpSerums;
    public static Collection<IAlleleBeeSpecies> allBeeSpecies;

    @Instance(NEIAddons.modid + "|ExtraBees")
    public static AddonExtraBees instance;

    @Override
    public String getName() {
        return "Extra Bees";
    }

    @Override
    public String[] getDependencies() {
        return new String[] { "ExtraBees@[1.6-pre15,)", "Forestry@[2.2.6.0,)" };
    }

    @Override
    public boolean checkSide(Side side) {
        return side.isClient();
    }

    @PreInit
    public void preInit(FMLPreInitializationEvent ev) {
        doPreInit(ev);
    }

    @Override
    public void init(Side side) throws Exception {
        loadBlacklisted = NEIAddons.config.get(getName(), "Load blacklisted", false, "Set to true to load blacklisted species and alleles, it's dangerous and (mostly) useless").getBoolean(false);
        dumpSerums = NEIAddons.config.get(getName(), "Dump serum list", false, "Set to true to dump all serums on startup for debug purposes").getBoolean(false);
        active = true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void loadClient() {
        AddonExtraBeesClient.load();
    }
}
