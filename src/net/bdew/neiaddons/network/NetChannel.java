/*
 * Copyright (c) bdew, 2013 - 2015
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.neiaddons.network;

import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import io.netty.channel.ChannelHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

import java.util.EnumMap;

public class NetChannel {
    public EnumMap<Side, FMLEmbeddedChannel> channels;

    public NetChannel(String name) {
        channels = NetworkRegistry.INSTANCE.newChannel(name, new NBTMessageCodec());
    }

    public void addHandler(Side side, ChannelHandler handler) {
        FMLEmbeddedChannel ch = channels.get(side);
        String name = ch.findChannelHandlerNameForType(NBTMessageCodec.class);
        ch.pipeline().addAfter(name, side + "Handler", handler);
    }

    public void sendToAll(NBTTagCompound message) {
        FMLEmbeddedChannel chan = this.channels.get(Side.SERVER);
        chan.attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALL);
        chan.writeAndFlush(message);
    }

    public void sendTo(NBTTagCompound message, EntityPlayerMP player) {
        FMLEmbeddedChannel chan = this.channels.get(Side.SERVER);
        chan.attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
        chan.attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
        chan.writeAndFlush(message);
    }

    public void sendToAllAround(NBTTagCompound message, NetworkRegistry.TargetPoint point) {
        FMLEmbeddedChannel chan = this.channels.get(Side.SERVER);
        chan.attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
        chan.attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(point);
        chan.writeAndFlush(message);
    }

    public void sendToDimension(NBTTagCompound message, int dimensionId) {
        FMLEmbeddedChannel chan = this.channels.get(Side.SERVER);
        chan.attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.DIMENSION);
        chan.attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(dimensionId);
        chan.writeAndFlush(message);
    }

    public void sendToServer(NBTTagCompound message) {
        FMLEmbeddedChannel chan = this.channels.get(Side.CLIENT);
        chan.attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
        chan.writeAndFlush(message);
    }
}
