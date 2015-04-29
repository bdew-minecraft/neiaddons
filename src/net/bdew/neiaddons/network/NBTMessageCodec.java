/*
 * Copyright (c) bdew, 2013 - 2015
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.neiaddons.network;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import net.bdew.neiaddons.NEIAddons;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.List;

@ChannelHandler.Sharable
public class NBTMessageCodec extends MessageToMessageCodec<FMLProxyPacket, NBTTagCompound> {
    @Override
    protected void encode(ChannelHandlerContext ctx, NBTTagCompound msg, List<Object> out) throws Exception {
        ByteBuf buff = Unpooled.buffer();
        DataOutputStream writer = new DataOutputStream(new ByteBufOutputStream(buff));
        CompressedStreamTools.write(msg, writer);
        out.add(new FMLProxyPacket(buff, ctx.channel().attr(NetworkRegistry.FML_CHANNEL).get()));
        writer.close();
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, FMLProxyPacket msg, List<Object> out) throws Exception {
        DataInputStream stream = new DataInputStream(new ByteBufInputStream(msg.payload()));
        try {
            out.add(CompressedStreamTools.read(stream));
        } catch (Throwable e) {
            NEIAddons.logSevereExc(e, "Error decoding packet");
        } finally {
            stream.close();
        }
    }
}
