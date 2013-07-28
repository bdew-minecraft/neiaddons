/**
 * Copyright (c) bdew, 2013
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/neiaddons/master/MMPL-1.0.txt
 */

package net.bdew.neiaddons.appeng;

import net.bdew.neiaddons.BaseAddon;
import net.bdew.neiaddons.NEIAddons;
import net.bdew.neiaddons.ServerHandler;
import net.bdew.neiaddons.Utils;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid = NEIAddons.modid + "|AE", name = "NEI Addons: Applied Energistics", version = "@@VERSION@@", dependencies = "after:NEIAddons;after:AppliedEnergistics")
public class AddonAE extends BaseAddon {

    public static boolean invertShift;

    public static Class<? extends GuiContainer> GuiPatternEncoder;
    public static Class<? extends Container> ContainerPatternEncoder;
    public static Class<? extends Slot> SlotFake;

    @Instance(NEIAddons.modid + "|AE")
    public static AddonAE instance;
    
    @Override
    public String getName() {
        return "Applied Energistics";
    }

    @Override
    public String[] getDependencies() {
        return new String[]{"AppliedEnergistics"};
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent ev) {
        doPreInit(ev);
    }
    
    @Override
    public void init(Side side) throws ClassNotFoundException {
        if (side == Side.CLIENT) {
            GuiPatternEncoder = Utils.getAndCheckClass("appeng.me.gui.GuiPatternEncoder", GuiContainer.class);
            invertShift = NEIAddons.config.get(getName(), "Invert Shift", false, "If set to true will swap normal and shift click behavior").getBoolean(false);
        }

        ContainerPatternEncoder = Utils.getAndCheckClass("appeng.me.container.ContainerPatternEncoder", Container.class);
        SlotFake = Utils.getAndCheckClass("appeng.slot.SlotFake", Slot.class);

        ServerHandler.registerHandler(SetPatternEncoderRecipe.command, new SetPatternEncoderRecipe());
        
        active = true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void loadClient() {
        AddonAEClient.load();
    }

}
