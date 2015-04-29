/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/neiaddons/master/MMPL-1.0.txt
 */

package net.bdew.neiaddons.botany;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.bdew.neiaddons.BaseAddon;
import net.bdew.neiaddons.NEIAddons;
import net.bdew.neiaddons.botany.flowers.FlowerHelper;
import net.minecraft.client.resources.I18n;

@Mod(modid = NEIAddons.modId + "|Botany", name = "NEI Addons: Botany", version = "NEIADDONS_VER", dependencies = "after:NEIAddons;after:Botany")
public class AddonBotany extends BaseAddon {
    public static boolean showSecret;
    public static boolean showFlowerMutations;
    public static boolean loadBlacklisted;
    public static boolean showReqs;

    @Instance(NEIAddons.modId + "|Botany")
    public static AddonBotany instance;

    @Override
    public boolean checkSide(Side side) {
        return side.isClient();
    }

    @Override
    public String getName() {
        return "Botany";
    }

    @Override
    public String[] getDependencies() {
        return new String[]{"Botany"};
    }

    @Override
    @EventHandler
    public void preInit(FMLPreInitializationEvent ev) {
        doPreInit(ev);
    }

    @Override
    public void init(Side side) throws Exception {
        showFlowerMutations = NEIAddons.config.get(getName(), "Show Flower Mutations", true, "Set to false to disable flower mutations browsing").getBoolean(false);
        showSecret = NEIAddons.config.get(getName(), "Show Secret Mutations", false, "Set to true to show secret mutations").getBoolean(false);
        showReqs = NEIAddons.config.get(getName(), "Show Mutation Requirements", true, "Set to false disable display of mutation requirements").getBoolean(false);

        loadBlacklisted = NEIAddons.config.get(getName(), "Load blacklisted", false, "Set to true to load blacklisted species and alleles, it's dangerous and (mostly) useless").getBoolean(false);

        active = true;
    }

    public void registerWithNEIPlugins(String name, String id) {
        FMLInterModComms.sendRuntimeMessage(this, "NEIPlugins", "register-crafting-handler", String.format("%s@%s@%s", I18n.format("bdew.neiaddons.genetics"), name, id));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void loadClient() {
        FlowerHelper.setup();
    }
}
