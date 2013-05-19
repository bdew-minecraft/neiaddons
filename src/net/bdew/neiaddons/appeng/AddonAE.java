/**
 * Copyright (c) bdew, 2013
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/neiaddons/master/MMPL-1.0.txt
 */

package net.bdew.neiaddons.appeng;

import net.bdew.neiaddons.NEIAddons;
import net.bdew.neiaddons.Utils;
import net.bdew.neiaddons.api.NEIAddon;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import codechicken.nei.api.API;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class AddonAE implements NEIAddon {
    private Boolean active = false;

    public static boolean invertShift;

    public static Class<? extends GuiContainer> GuiPatternEncoder;
    public static Class<? extends Container> ContainerPatternEncoder;
    public static Class<? extends Slot> SlotFake;

    public static final String channel = "neiaddons.ae";

    @Override
    public String getName() {
        return "Applied Energistics";
    }

    @Override
    public Boolean isActive() {
        return active;
    }

    @Override
    public void init(Side side) throws ClassNotFoundException {
        if (!Loader.isModLoaded("AppliedEnergistics")) {
            NEIAddons.log.info("Applied Energistics not installed, skipping");
            return;
        }
        
        if (side==Side.CLIENT) {
            GuiPatternEncoder = Utils.getAndCheckClass("appeng.me.gui.GuiPatternEncoder", GuiContainer.class);
        };
        
        ContainerPatternEncoder = Utils.getAndCheckClass("appeng.me.container.ContainerPatternEncoder", Container.class);
        SlotFake = Utils.getAndCheckClass("appeng.slot.SlotFake", Slot.class);

        NetworkRegistry.instance().registerChannel(new ServerHandler(), channel, Side.SERVER);
        
        invertShift = NEIAddons.config.get(getName(), "Invert Shift", false, "If set to true will swap normal and shift click behavior").getBoolean(false);
        
        active = true;
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    public void loadClient() {
        API.registerGuiOverlayHandler(GuiPatternEncoder, new PatternEncoderHandler(), "crafting");
    }

}
