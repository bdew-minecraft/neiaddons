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
import com.google.common.base.Throwables;
import cpw.mods.fml.common.event.FMLInterModComms;
import net.bdew.neiaddons.exnihilo.proxies.HammerRegistryProxy;
import net.bdew.neiaddons.exnihilo.proxies.SieveRegistryProxy;
import net.bdew.neiaddons.exnihilo.proxies.SiftRewardProxy;
import net.bdew.neiaddons.exnihilo.proxies.SmashableProxy;
import net.minecraft.client.resources.I18n;

class AddonExnihiloClient {
    private static void registerRecipeHandler(BaseRecipeHandler handler) {
        API.registerRecipeHandler(handler);
        API.registerUsageHandler(handler);
        FMLInterModComms.sendRuntimeMessage(AddonExnihilo.instance, "NEIPlugins", "register-crafting-handler",
                String.format("%s@%s@%s", I18n.format("bdew.neiaddons.exnihilo"), handler.getRecipeName(), handler.getRecipeId()));
    }

    public static void load() {
        try {
            SmashableProxy.init();
            HammerRegistryProxy.init();
            SiftRewardProxy.init();
            SieveRegistryProxy.init();
        } catch (Throwable t) {
            Throwables.propagate(t);
        }
        registerRecipeHandler(new HammerRecipeHandler());
        registerRecipeHandler(new SieveRecipeHandler());
    }
}
