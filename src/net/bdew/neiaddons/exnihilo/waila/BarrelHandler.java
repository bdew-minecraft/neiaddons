/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/neiaddons/master/MMPL-1.0.txt
 */

package net.bdew.neiaddons.exnihilo.waila;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.bdew.neiaddons.exnihilo.WailaHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import java.text.DecimalFormat;
import java.util.List;

public class BarrelHandler implements IWailaDataProvider {
    private final DecimalFormat dec;
    private static final int MAX_COMPOSTING_TIME = 1000;
    private static final int MAX_FLUID = 1000;

    public BarrelHandler() {
        dec = new DecimalFormat("#,##0");
    }

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return null;
    }

    @Override
    public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return currenttip;
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        NBTTagCompound tag = accessor.getNBTData();

        int modeId = tag.getInteger("mode");
        float volume = tag.getFloat("volume");
        int timer = tag.getInteger("timer");
        Fluid fluid = FluidRegistry.getFluid(tag.getShort("fluid"));

        String mode = WailaHandler.valuesBarrelMode[modeId].name();

        if (mode.equals("EMPTY")) {
            currenttip.add("Empty");
        } else if (mode.equals("FLUID")) {
            currenttip.add(String.format("%s %s mB", fluid.getLocalizedName(), dec.format(volume * MAX_FLUID)));
            MovingObjectPosition pos = accessor.getPosition();
            if (fluid.getID() == FluidRegistry.LAVA.getID() && (accessor.getWorld().getBlockMaterial(pos.blockX, pos.blockY, pos.blockZ).getCanBurn())) {
                currenttip.add(String.format("%sWill burn in %.1f seconds!!!%s",
                        EnumChatFormatting.RED, (400F - timer) / 20, EnumChatFormatting.RESET));
            }
        } else if (mode.equals("COMPOST")) {
            if (volume < 1)
                currenttip.add(String.format("Add compostable items - %.0f%%", 100 * volume));
            else
                currenttip.add(String.format("Composting - %.0f%%", 100F * timer / MAX_COMPOSTING_TIME));
        } else if (mode.equals("DIRT")) {
            currenttip.add("Ready: Dirt");
        } else if (mode.equals("CLAY")) {
            currenttip.add("Ready: Clay");
        } else if (mode.equals("SPORED")) {
            currenttip.add(String.format("Making: Witch Water - %.0f%%", 100F * timer / MAX_COMPOSTING_TIME));
        } else if (mode.equals("SLIME")) {
            currenttip.add("Ready: Slime");
        } else if (mode.equals("NETHERRACK")) {
            currenttip.add("Ready: Netherrack");
        } else if (mode.equals("ENDSTONE")) {
            currenttip.add("Ready: End Stone");
        } else if (mode.equals("MILKED")) {
            currenttip.add(String.format("Making: Slime - %.0f%%", 100F * timer / MAX_COMPOSTING_TIME));
        } else if (mode.equals("SOULSAND")) {
            currenttip.add("Ready: Soul Sand");
        } else if (mode.equals("BEETRAP")) {
            currenttip.add("Ready: Artificial Hive");
        } else if (mode.equals("OBSIDIAN")) {
            currenttip.add("Ready: Obsidian");
        } else if (mode.equals("COBBLESTONE")) {
            currenttip.add("Ready: Cobblestone");
        } else if (mode.equals("BLAZE_COOKING")) {
            currenttip.add(String.format("Making: Blaze - %.0f%%", 100F * timer / MAX_COMPOSTING_TIME));
        } else if (mode.equals("BLAZE")) {
            currenttip.add("Ready: Blaze Rod");
        } else if (mode.equals("ENDER_COOKING")) {
            currenttip.add(String.format("Making: Enderman - %.0f%%", 100F * timer / MAX_COMPOSTING_TIME));
        } else if (mode.equals("ENDER")) {
            currenttip.add("Ready: Ender Pearl");
        } else {
            currenttip.add("Unknown Mode: " + mode);
        }

        return currenttip;
    }

    @Override
    public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return currenttip;
    }
}
