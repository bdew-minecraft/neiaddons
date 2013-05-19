/**
 * Copyright (c) bdew, 2013
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/neiaddons/master/MMPL-1.0.txt
 */

package net.bdew.neiaddons.ee3;

import codechicken.nei.api.API;
import codechicken.nei.recipe.DefaultOverlayHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.bdew.neiaddons.NEIAddons;
import net.bdew.neiaddons.Utils;
import net.bdew.neiaddons.api.NEIAddon;
import net.minecraft.client.gui.inventory.GuiContainer;

public class AddonEE3 implements NEIAddon {
    private Boolean active = false;
    private Class<? extends GuiContainer> GuiPortableCrafting;

    @Override
    public String getName() {
        return "Equivalent Exchange 3";
    }

    @Override
    public Boolean isActive() {
        return active;
    }

    @Override
    public void init(Side side) throws Exception {
        if (!Loader.isModLoaded("EE3")) {
            NEIAddons.log.info("Equivalent Exchange 3 not installed, skipping");
            return;
        }
        
        if (side==Side.CLIENT) {
            GuiPortableCrafting = Utils.getAndCheckClass("com.pahimar.ee3.client.gui.inventory.GuiPortableCrafting", GuiContainer.class);
            active = true;
        } else {
            NEIAddons.log.info("Equivalent Exchange 3 Addon is client-side only, skipping");
        }
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    public void loadClient() {
        API.registerGuiOverlay(GuiPortableCrafting, "crafting");
        API.registerGuiOverlayHandler(GuiPortableCrafting, new DefaultOverlayHandler(), "crafting");
    }

}
