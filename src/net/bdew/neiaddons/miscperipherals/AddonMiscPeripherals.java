/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/neiaddons/master/MMPL-1.0.txt
 */

package net.bdew.neiaddons.miscperipherals;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.bdew.neiaddons.BaseAddon;
import net.bdew.neiaddons.NEIAddons;
import net.bdew.neiaddons.Utils;
import net.bdew.neiaddons.network.ServerHandler;
import net.bdew.neiaddons.utils.SetRecipeCommandHandler;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

@Mod(modid = NEIAddons.modId + "|MiscPeripherals", name = "NEI Addons: Misc Peripherals", version = "NEIADDONS_VER", dependencies = "after:NEIAddons;after:MiscPeripherals")
public class AddonMiscPeripherals extends BaseAddon {

    public static boolean invertShift;

    public static Class<? extends GuiContainer> GuiCrafter;
    public static Class<? extends Container> ContainerCrafter;
    public static Class<? extends Slot> SlotRO;

    public static final String commandName = "SetCCCRecipe";

    @Instance(NEIAddons.modId + "|MiscPeripherals")
    public static AddonMiscPeripherals instance;

    @Override
    public String getName() {
        return "Misc Peripherals";
    }

    @Override
    public String[] getDependencies() {
        return new String[]{"MiscPeripherals"};
    }

    @Override
    @EventHandler
    public void preInit(FMLPreInitializationEvent ev) {
        doPreInit(ev);
    }

    @Override
    public void init(Side side) throws ClassNotFoundException {
        if (side == Side.CLIENT) {
            GuiCrafter = Utils.getAndCheckClass("miscperipherals.gui.GuiCrafter", GuiContainer.class);
            invertShift = NEIAddons.config.get(getName(), "Invert Shift", false, "If set to true will swap normal and shift click behavior").getBoolean(false);
        }

        ContainerCrafter = Utils.getAndCheckClass("miscperipherals.inventory.ContainerCrafter", Container.class);
        SlotRO = Utils.getAndCheckClass("miscperipherals.inventory.SlotRO", Slot.class);

        ServerHandler.registerHandler(commandName, new SetRecipeCommandHandler(ContainerCrafter, SlotRO));

        active = true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void loadClient() {
        AddonMiscPeripheralsClient.load();
    }
}
