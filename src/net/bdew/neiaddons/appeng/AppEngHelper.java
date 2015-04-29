package net.bdew.neiaddons.appeng;

import codechicken.nei.api.API;

public class AppEngHelper {
    public static void init() {
        API.registerNEIGuiHandler(new AppEngGuiHandler());
    }
}
