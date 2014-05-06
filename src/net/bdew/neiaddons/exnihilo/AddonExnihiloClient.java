/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/neiaddons/master/MMPL-1.0.txt
 */

package net.bdew.neiaddons.exnihilo;

import codechicken.nei.api.API;
import cpw.mods.fml.common.event.FMLInterModComms;
import net.bdew.neiaddons.exnihilo.proxies.HammerRegistryProxy;
import net.bdew.neiaddons.exnihilo.proxies.SmashableProxy;

import java.util.HashSet;
import java.util.Set;

class AddonExnihiloClient {
    public static Set<Integer> hammerSourceIds = new HashSet<Integer>();
    public static Set<Integer> hammerDropIds = new HashSet<Integer>();

    public static void load() {
        for (SmashableProxy x : HammerRegistryProxy.getRegistry()) {
            hammerSourceIds.add(x.sourceID());
            hammerDropIds.add(x.id());
            AddonExnihilo.instance.logInfo("Smashable %d@%d -> %d@%d %.2f%% (+%.2f)", x.sourceID(), x.sourceMeta(), x.id(), x.meta(), x.chance() * 100, x.luckMultiplier());
        }

        HammerRecipeHandler hammerRecipeHandler = new HammerRecipeHandler();
        API.registerRecipeHandler(hammerRecipeHandler);
        API.registerUsageHandler(hammerRecipeHandler);
        FMLInterModComms.sendRuntimeMessage(AddonExnihilo.instance, "NEIPlugins", "register-crafting-handler", String.format("Forestry Genetics@%s@%s", hammerRecipeHandler.getRecipeName(), hammerRecipeHandler.getRecipeIdent()));
    }
}
