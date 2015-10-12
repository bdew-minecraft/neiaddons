/*
 * Copyright (c) bdew, 2013 - 2015
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.neiaddons.forestry;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.bdew.neiaddons.BaseAddon;
import net.bdew.neiaddons.NEIAddons;
import net.bdew.neiaddons.forestry.bees.BeeHelper;
import net.bdew.neiaddons.forestry.butterflies.ButterflyHelper;
import net.bdew.neiaddons.forestry.trees.TreeHelper;
import net.minecraft.client.resources.I18n;

@Mod(modid = NEIAddons.modId + "|Forestry", name = "NEI Addons: Forestry", version = "NEIADDONS_VER", dependencies = "after:NEIAddons;after:Forestry")
public class AddonForestry extends BaseAddon {
    public static boolean showSecret;
    public static boolean addBees;
    public static boolean addCombs;
    public static boolean addSaplings;
    public static boolean addPollen;
    public static boolean loadBlacklisted;
    public static boolean showBeeMutations;
    public static boolean showBeeProducts;
    public static boolean showTreeMutations;
    public static boolean showTreeProducts;
    public static boolean showButterflyMutations;
    public static boolean showReqs;

    @Instance(NEIAddons.modId + "|Forestry")
    public static AddonForestry instance;

    @Override
    public String getName() {
        return "Forestry";
    }

    @Override
    public String[] getDependencies() {
        return new String[]{"Forestry@[4.0.8.36,)"};
    }

    @Override
    @EventHandler
    public void preInit(FMLPreInitializationEvent ev) {
        doPreInit(ev);
    }

    @Override
    public void init(Side side) throws Exception {
        showBeeMutations = NEIAddons.config.get(getName(), "Show Bee Mutations", true, "Set to false to disable bee mutations browsing").getBoolean(false);
        showBeeProducts = NEIAddons.config.get(getName(), "Show Bee Products", true, "Set to false to disable bee products browsing").getBoolean(false);

        showTreeMutations = NEIAddons.config.get(getName(), "Show Tree Mutations", true, "Set to false to disable tree mutations browsing").getBoolean(false);
        showTreeProducts = NEIAddons.config.get(getName(), "Show Tree Products", true, "Set to false to disable tree products browsing").getBoolean(false);

        showButterflyMutations = NEIAddons.config.get(getName(), "Show Butterfly Mutations", true, "Set to false to disable butterfly mutations browsing").getBoolean(false);
        //showButterflyProducts = NEIAddons.config.get(getName(), "Show Butterfly Products", true, "Set to false to disable butterfly products browsing").getBoolean(false);

        showSecret = NEIAddons.config.get(getName(), "Show Secret Mutations", false, "Set to true to show secret mutations").getBoolean(false);

        showReqs = NEIAddons.config.get(getName(), "Show Mutation Requirements", true, "Set to false disable display of mutation requirements").getBoolean(false);

        addBees = NEIAddons.config.get(getName(), "Add Bees to Search", true, "Set to true to add all bees to NEI search").getBoolean(false);
        addCombs = NEIAddons.config.get(getName(), "Add Combs to Search", false, "Set to true to add all combs that are produced by bees to NEI search").getBoolean(false);
        addSaplings = NEIAddons.config.get(getName(), "Add Saplings to Search", true, "Set to true to add all saplings to NEI search").getBoolean(false);
        addPollen = NEIAddons.config.get(getName(), "Add Pollen to Search", true, "Set to true to add all pollen types to NEI search").getBoolean(false);

        loadBlacklisted = NEIAddons.config.get(getName(), "Load blacklisted", false, "Set to true to load blacklisted species and alleles, it's dangerous and (mostly) useless").getBoolean(false);

        active = true;
    }

    public void registerWithNEIPlugins(String name, String id) {
        FMLInterModComms.sendRuntimeMessage(this, "NEIPlugins", "register-crafting-handler", String.format("%s@%s@%s", I18n.format("bdew.neiaddons.genetics"), name, id));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void loadClient() {
        BeeHelper.setup();
        TreeHelper.setup();
        ButterflyHelper.setup();
    }
}
