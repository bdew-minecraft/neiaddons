/**
 * Copyright (c) bdew, 2013
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/neiaddons/master/MMPL-1.0.txt
 */

package net.bdew.neiaddons.crafting;

import java.util.ArrayList;
import java.util.Collection;

import net.bdew.neiaddons.BaseAddon;
import net.bdew.neiaddons.NEIAddons;
import net.bdew.neiaddons.Utils;
import net.minecraft.client.gui.inventory.GuiContainer;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid = NEIAddons.modid + "|CraftingTables", name = "NEI Addons: Crafting Tables", version = "@@VERSION@@", dependencies = "after:NEIAddons")
public class AddonCraftingTables extends BaseAddon {

    public static Collection<Class<? extends GuiContainer>> craftingTables;

    @Override
    public String getName() {
        return "Crafting Tables";
    }

    @Override
    public boolean checkSide(Side side) {
        return side.isClient();
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent ev) {
        doPreInit(ev);
    }

    public void tryLoadTableClass(String modId, String className, String humanName) {
        try {
            if (verifyModVersion(modId)) {
                craftingTables.add(Utils.getAndCheckClass(className, GuiContainer.class));
                logInfo("Registered %s", humanName);
            } else {
                logInfo("Not registering %s", humanName);
            }
        } catch (Throwable e) {
            logWarning("Registering %s failed: %s", humanName, e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void init(Side side) throws Exception {
        craftingTables = new ArrayList<Class<? extends GuiContainer>>();
        
        tryLoadTableClass("EE3", "com.pahimar.ee3.client.gui.inventory.GuiPortableCrafting", "EE3 Minium Stone");
        tryLoadTableClass("BuildCraft|Factory", "buildcraft.factory.gui.GuiAutoCrafting", "BC Autorcrafting Table");
        tryLoadTableClass("powersuitaddons", "andrew.powersuits.client.PortableCraftingGui", "MPSA In-Place Assembler");
        tryLoadTableClass("TConstruct", "tconstruct.client.gui.CraftingStationGui", "TC Crafting Station");
        
        if (craftingTables.size() > 0) {
            logInfo("%d crafting tables registered", craftingTables.size());
            active = true;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void loadClient() {
        AddonCraftingTablesClient.load();
    }
}
