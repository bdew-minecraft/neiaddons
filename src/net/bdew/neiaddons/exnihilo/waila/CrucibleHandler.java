/*
 * Copyright (c) bdew, 2013 - 2015
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.neiaddons.exnihilo.waila;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.text.DecimalFormat;
import java.util.List;

public class CrucibleHandler implements IWailaDataProvider {
    private final DecimalFormat dec = new DecimalFormat("#,##0");

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, int x, int y, int z) {
        return tag;
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
            currenttip.add(I18n.format("bdew.exnihilo.crucible.fluid", fluid.getLocalizedName(new FluidStack(fluid, 1)), dec.format(fluidVolume)));

        if (!content.isEmpty() && solidVolume > 0) {
            ItemStack stack = new ItemStack((Block) Block.blockRegistry.getObject(content), 1, contentMeta);
            currenttip.add(I18n.format("bdew.exnihilo.crucible.solid", stack.getDisplayName(), dec.format(solidVolume)));
        }

        return currenttip;
    }

    @Override
    public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return currenttip;
    }
}
