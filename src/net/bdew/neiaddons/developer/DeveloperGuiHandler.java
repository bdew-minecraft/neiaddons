/*
 * Copyright (c) bdew, 2013 - 2015
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.neiaddons.developer;

import codechicken.nei.guihook.IContainerTooltipHandler;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

public class DeveloperGuiHandler implements IContainerTooltipHandler {
    @Override
    public List<String> handleTooltip(GuiContainer gui, int x, int y, List<String> tip) {
        int slotNum = -1;
        Slot slot = null;
        for (int k = 0; k < gui.inventorySlots.inventorySlots.size(); k++) {
            slot = (Slot) gui.inventorySlots.inventorySlots.get(k);
            if (isMouseOverSlot(gui, slot, x, y)) {
                slotNum = k;
                break;
            }
        }
        if (slotNum > 0) {
            tip.add(String.format("%sSlot #%d", EnumChatFormatting.UNDERLINE, slotNum));
            tip.add("G: " + gui.getClass().getSimpleName());
            tip.add("C: " + gui.inventorySlots.getClass().getSimpleName());
            tip.add("S: " + slot.getClass().getSimpleName());
            if (slot.inventory != null)
                tip.add(String.format("I: %s (#%d)", slot.inventory.getClass().getSimpleName(), slot.getSlotIndex()));
            else
                tip.add(String.format("%sI: NULL", EnumChatFormatting.RED));
            tip.add(String.format("%d/%d", slot.xDisplayPosition, slot.yDisplayPosition));
        }
        return tip;
    }

    @Override
    public List<String> handleItemDisplayName(GuiContainer guiContainer, ItemStack itemStack, List<String> list) {
        return list;
    }

    @Override
    public List<String> handleItemTooltip(GuiContainer guiContainer, ItemStack itemStack, int i, int i1, List<String> list) {
        return list;
    }

    private boolean isMouseOverSlot(GuiContainer gui, Slot slot, int mouseX, int mouseY) {
        int slotX = slot.xDisplayPosition;
        int slotY = slot.yDisplayPosition;
        int slotW = 16;
        int slotH = 16;
        mouseX -= gui.guiLeft;
        mouseY -= gui.guiTop;
        return mouseX >= slotX - 1 && mouseX < slotX + slotW + 1 && mouseY >= slotY - 1 && mouseY < slotY + slotH + 1;
    }
}
