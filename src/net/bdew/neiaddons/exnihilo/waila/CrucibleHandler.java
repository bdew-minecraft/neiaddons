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
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import java.text.DecimalFormat;
import java.util.List;

public class CrucibleHandler implements IWailaDataProvider {
    private final DecimalFormat dec;

    public CrucibleHandler() {
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
        float solidVolume = tag.getFloat("solidVolume");
        float fluidVolume = tag.getFloat("fluidVolume");
        String content = tag.getString("content");
        int contentMeta = tag.getInteger("contentMeta");
        Fluid fluid = FluidRegistry.getFluid(tag.getShort("fluid"));

        if (fluid != null && fluidVolume > 0)
            currenttip.add(String.format("Fluid: %s %s mB", fluid.getLocalizedName(), dec.format(fluidVolume)));

        if (!content.isEmpty() && solidVolume > 0) {
            ItemStack stack = new ItemStack((Block) Block.blockRegistry.getObject(content), 1, contentMeta);
            currenttip.add(String.format("Solid: %s %s", stack.getDisplayName(), dec.format(solidVolume)));
        }

        return currenttip;
    }

    @Override
    public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return currenttip;
    }
}
