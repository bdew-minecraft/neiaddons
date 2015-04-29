/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/neiaddons/master/MMPL-1.0.txt
 */

package net.bdew.neiaddons.api;

import cpw.mods.fml.relauncher.Side;

public interface NEIAddon {
    String getName();

    Boolean isActive();

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
