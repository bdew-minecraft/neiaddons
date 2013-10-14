package net.bdew.neiaddons.forestry.crafting;

import net.bdew.neiaddons.ServerHandler;
import net.bdew.neiaddons.Utils;
import net.bdew.neiaddons.forestry.AddonForestry;
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

    public static void init(Side side) {
        try {
            if (side == Side.CLIENT) {
                GuiWorktable = Utils.getAndCheckClass("forestry.factory.gui.GuiWorktable", GuiContainer.class);
            }
            ContainerWorktable = Utils.getAndCheckClass("forestry.factory.gui.ContainerWorktable", Container.class);
            SlotCraftMatrix = Utils.getAndCheckClass("forestry.factory.gui.SlotCraftMatrix", Slot.class);
            ServerHandler.registerHandler(SetWorktableRecipe.command, new SetWorktableRecipe());
            active = true;
        } catch (Throwable e) {
            AddonForestry.instance.logWarning("Failed to setup Worktable crafting overlay:");
            e.printStackTrace();
        }
    }

    public static void setup() {
        if (active) {
            API.registerGuiOverlayHandler(GuiWorktable, new WorktableHandler(), "crafting");
        }
    }
}
