package net.bdew.neiaddons;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class ClientHandler implements IPacketHandler, ITickHandler {
	public static Set<String> enabledCommands = new HashSet<String>();
	private WorldClient oldworld;

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		WorldClient world = Minecraft.getMinecraft().theWorld;
		if ((world!=null) && (world!=oldworld)) {
			reset();
		}
	}

	private void reset() {
		NEIAddons.logInfo("World changed, resetting");
		oldworld = Minecraft.getMinecraft().theWorld;
		enabledCommands.clear();
	}
	
	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.WORLD);
	}

	@Override
	public String getLabel() {
		return "NEI Addons";
	}

	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
        try {
            NBTTagCompound data = CompressedStreamTools.decompress(packet.data);
            String cmd = data.getString("cmd");
            if (cmd.equals("hello")) {
            	reset();
            	if (data.getCompoundTag("data").getInteger("version") != NEIAddons.netVersion) {
            		NEIAddons.logWarning("Client/Server version mismatch! client=%d server=%d", data.getCompoundTag("data").getInteger("version"), NEIAddons.netVersion);
            		return;
            	}
            	String cmds = data.getCompoundTag("data").getString("commands");
            	NEIAddons.logInfo("Available server commands: %s", cmds);
            	enabledCommands.addAll(Arrays.asList(StringUtils.split(cmds, ';')));
            } else {
                NEIAddons.logWarning("Uknown packet from server: %s", cmd);
            }
        } catch (Throwable e) {
            NEIAddons.logWarning("Error handling packet from server");
            e.printStackTrace();
        }
	}
}
