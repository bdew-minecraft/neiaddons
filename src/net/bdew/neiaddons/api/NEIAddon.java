/*
 * Copyright (c) bdew, 2013 - 2015
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.neiaddons.api;

import cpw.mods.fml.relauncher.Side;

public interface NEIAddon {
    String getName();

    boolean isActive();

    boolean isEnabledByDefault();

    /**
     * Called from FMLPreInitializationEvent
     *
     * @throws Exception
     */
    @SuppressWarnings("RedundantThrows")
    void init(Side side) throws Exception;

    /**
     * Called from NEI loadConfig
     */
    void loadClient();
}
