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
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.bdew.neiaddons.NEIAddons;

public class ClientPacketHandler implements IMessageHandler<NBTMessage, IMessage> {
    @Override
    public IMessage onMessage(NBTMessage message, MessageContext ctx) {
        NEIAddons.clientHandler.processCommand(message.data.getString("cmd"), message.data.getCompoundTag("data"));
        return null;
    }
}
