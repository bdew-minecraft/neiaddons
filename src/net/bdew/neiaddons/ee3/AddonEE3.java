/**
 * Copyright (c) bdew, 2013
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/neiaddons/master/MMPL-1.0.txt
 */

package net.bdew.neiaddons.ee3;

import net.bdew.neiaddons.BaseAddon;
import net.bdew.neiaddons.NEIAddons;
import net.bdew.neiaddons.Utils;
import net.minecraft.client.gui.inventory.GuiContainer;
import codechicken.nei.api.API;
import codechicken.nei.recipe.DefaultOverlayHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid = NEIAddons.modid + "|EE3", name = "NEI Addons: Equivalent Exchange 3", version = "@@VERSION@@", dependencies = "after:NEIAddons;after:EE3")
public class AddonEE3 extends BaseAddon {

    private Class<? extends GuiContainer> GuiPortableCrafting;

    @Override
    public String getName() {
        return "Equivalent Exchange 3";
    }

    @PreInit
    public void preInit(FMLPreInitializationEvent ev) {
        super.preInit(ev);

        if (ev.getSide() != Side.CLIENT) {
            NEIAddons.log.info("Equivalent Exchange 3 Addon is client-side only, skipping");
            return;
        }

        if (!verifyModVersion("EE3"))
            return;

        NEIAddons.register(this);
    }

    @Override
    public void init(Side side) throws Exception {
        GuiPortableCrafting = Utils.getAndCheckClass("com.pahimar.ee3.client.gui.inventory.GuiPortableCrafting", GuiContainer.class);
        active = true;
    }

    @Override
    @SideOnly(value = Side.CLIENT)
    public void loadClient() {
        API.registerGuiOverlay(GuiPortableCrafting, "crafting");
        API.registerGuiOverlayHandler(GuiPortableCrafting, new DefaultOverlayHandler(), "crafting");
    }
}
