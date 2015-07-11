/*
 * Copyright (c) bdew, 2013 - 2015
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.neiaddons.crafting;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.bdew.neiaddons.BaseAddon;
import net.bdew.neiaddons.NEIAddons;
import net.bdew.neiaddons.Utils;
import net.minecraft.client.gui.inventory.GuiContainer;

import java.util.ArrayList;
import java.util.Collection;

@Mod(modid = NEIAddons.modId + "|CraftingTables", name = "NEI Addons: Crafting Tables", version = "NEIADDONS_VER", dependencies = "after:NEIAddons")
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
            logWarningExc(e, "Registering %s failed", humanName);
        }
    }

    @Override
    public void init(Side side) throws Exception {
        craftingTables = new ArrayList<Class<? extends GuiContainer>>();

        tryLoadTableClass("EE3", "com.pahimar.ee3.client.gui.inventory.GuiPortableCrafting", "EE3 Minium Stone");
        tryLoadTableClass("BuildCraft|Factory", "buildcraft.factory.gui.GuiAutoCrafting", "BC Autorcrafting Table");
        tryLoadTableClass("powersuitaddons", "com.qmxtech.powersuitaddons.client.PortableCraftingGui", "MPSA In-Place Assembler");
        tryLoadTableClass("ProjectE", "moze_intel.projecte.gameObjs.gui.GUIPhilosStone", "ProjectE Philosopher Stone");
        tryLoadTableClass("BiblioCraft", "jds.bibliocraft.gui.GuiFancyWorkbench", "BiblioCraft Fancy Workbench");
        tryLoadTableClass("RIO", "remoteio.client.gui.GuiIntelligentWorkbench", "RemoteIO Intelligent Workbench");
        tryLoadTableClass("Railcraft", "mods.railcraft.client.gui.GuiCartWork", "Railcraft Work Cart");


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
