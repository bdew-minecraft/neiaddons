/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/neiaddons/master/MMPL-1.0.txt
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
import net.bdew.neiaddons.Utils;
import net.bdew.neiaddons.forestry.bees.BeeHelper;
import net.bdew.neiaddons.forestry.butterflies.ButterflyHelper;
import net.bdew.neiaddons.forestry.trees.TreeHelper;
import net.bdew.neiaddons.network.ServerHandler;
import net.bdew.neiaddons.utils.SetRecipeCommandHandler;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

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

    public static Class<? extends GuiContainer> GuiWorktable;
    public static Class<? extends Container> ContainerWorktable;
    public static Class<? extends Slot> SlotCraftMatrix;
    public static boolean craftingActive = false;

    public static final String commandName = "SetForestryWorktableRecipe";

    @Instance(NEIAddons.modId + "|Forestry")
    public static AddonForestry instance;

    @Override
    public String getName() {
        return "Forestry";
    }

    @Override
    public String[] getDependencies() {
        return new String[]{"Forestry@[2.2.9.0,)"};
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

        if (this.verifyModVersion("Forestry@[2.3.0.5,)")) {
            try {
                if (side == Side.CLIENT) {
                    GuiWorktable = Utils.getAndCheckClass("forestry.factory.gui.GuiWorktable", GuiContainer.class);
                }
                ContainerWorktable = Utils.getAndCheckClass("forestry.factory.gui.ContainerWorktable", Container.class);

                if (this.verifyModVersion("Forestry@[3.5.0.0,)")) {
                    SlotCraftMatrix = Utils.getAndCheckClass("forestry.core.gui.slots.SlotCraftMatrix", Slot.class);
                } else {
                    SlotCraftMatrix = Utils.getAndCheckClass("forestry.factory.gui.SlotCraftMatrix", Slot.class);
                }

                ServerHandler.registerHandler(commandName, new SetRecipeCommandHandler(ContainerWorktable, SlotCraftMatrix));
                craftingActive = true;
            } catch (Throwable e) {
                AddonForestry.instance.logWarningExc(e, "Failed to setup Worktable crafting overlay");
            }
        } else {
            this.logInfo("Forestry Worktable support not loaded");
        }

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
        if (craftingActive) {
            CraftingOverlayHelper.setup();
        }
    }
}
