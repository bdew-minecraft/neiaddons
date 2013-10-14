/**
 * Copyright (c) bdew, 2013
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/neiaddons/master/MMPL-1.0.txt
 */

package net.bdew.neiaddons.forestry;

import net.bdew.neiaddons.ServerHandler;
import net.bdew.neiaddons.Utils;
import net.bdew.neiaddons.utils.CustomOverlayHandler;
import net.bdew.neiaddons.utils.SetRecipeCommandHandler;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import codechicken.nei.api.API;
import cpw.mods.fml.relauncher.Side;

public class CraftingOverlayHelper {
    public static Class<? extends GuiContainer> GuiWorktable;
    public static Class<? extends Container> ContainerWorktable;
    public static Class<? extends Slot> SlotCraftMatrix;
    public static boolean active = false;

    public static final String commandName = "SetForestryWorktableRecipe"; 

    public static void init(Side side) {
        try {
            if (side == Side.CLIENT) {
                GuiWorktable = Utils.getAndCheckClass("forestry.factory.gui.GuiWorktable", GuiContainer.class);
            }
            ContainerWorktable = Utils.getAndCheckClass("forestry.factory.gui.ContainerWorktable", Container.class);
            SlotCraftMatrix = Utils.getAndCheckClass("forestry.factory.gui.SlotCraftMatrix", Slot.class);
            ServerHandler.registerHandler(commandName, new SetRecipeCommandHandler(ContainerWorktable, SlotCraftMatrix));
            active = true;
        } catch (Throwable e) {
            AddonForestry.instance.logWarning("Failed to setup Worktable crafting overlay:");
            e.printStackTrace();
        }
    }

    public static void setup() {
        if (active) {
            CustomOverlayHandler handler = new CustomOverlayHandler(commandName, -14, 14, false);
            API.registerGuiOverlayHandler(GuiWorktable, handler, "crafting");
        }
    }
}
