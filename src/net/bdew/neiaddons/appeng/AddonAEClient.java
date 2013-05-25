package net.bdew.neiaddons.appeng;

import codechicken.nei.api.API;

public class AddonAEClient {

    public static void load() {
        API.registerGuiOverlayHandler(AddonAE.GuiPatternEncoder, new PatternEncoderHandler(), "crafting");
   }

}
