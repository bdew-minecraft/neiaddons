package net.bdew.neiaddons.crafting;

import net.minecraft.client.gui.inventory.GuiContainer;
import codechicken.nei.api.API;
import codechicken.nei.recipe.DefaultOverlayHandler;

public class AddonCraftingTablesClient {
    public static void load() {
        for (Class<? extends GuiContainer> tableClass : AddonCraftingTables.craftingTables) {
            API.registerGuiOverlay(tableClass, "crafting");
            API.registerGuiOverlayHandler(tableClass, new DefaultOverlayHandler(), "crafting");
        }        
    }
}
