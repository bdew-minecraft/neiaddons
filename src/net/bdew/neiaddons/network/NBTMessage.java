/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/neiaddons/master/MMPL-1.0.txt
 */

package net.bdew.neiaddons.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import net.bdew.neiaddons.NEIAddons;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class NBTMessage implements IMessage {
    public NBTTagCompound data;

    public NBTMessage() {
    }

    public NBTMessage(NBTTagCompound data) {
        this.data = data;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        try {
            DataInputStream reader = new DataInputStream(new ByteBufInputStream(buf));
            data = CompressedStreamTools.read(reader);
            reader.close();
        } catch (IOException e) {
            NEIAddons.log.warn("Network packet decoding error", e);
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        try {
            DataOutputStream writer = new DataOutputStream(new ByteBufOutputStream(buf));
            CompressedStreamTools.write(data, writer);
            writer.close();
        } catch (IOException e) {
            NEIAddons.log.warn("Network packet encoding error", e);
        }
    }
}
