package net.bdew.neiaddons.forestry;

import java.util.Map.Entry;

import net.bdew.neiaddons.NEIAddons;
import net.bdew.neiaddons.api.NEIAddon;
import codechicken.nei.api.API;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import forestry.api.apiculture.EnumBeeType;
import forestry.api.apiculture.IAlleleBeeSpecies;
import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.IAllele;

@Mod(modid = NEIAddons.modid + "|Forestry", name = "NEI Addons: Forestry", version = "@@VERSION@@", dependencies = "after:NEIAddons;after:Forestry")
public class AddonForestry implements NEIAddon {
    private Boolean active = false;
    private BeeBreedingRecipeHandler beeBreedingRecipeHandler;
    private BeeProductsRecipeHandler beeProductsRecipeHandler;

    public static boolean showSecret;
    public static boolean addSearch;

    @Override
    public String getName() {
        return "Forestry";
    }

    @Override
    public Boolean isActive() {
        return active;
    }

    @PreInit
    public void preInit(FMLPreInitializationEvent ev) {
        if (!Loader.isModLoaded("Forestry")) {
            NEIAddons.log.info("Forestry is not installed, skipping");
            return;
        }

        if (ev.getSide() != Side.CLIENT) {
            NEIAddons.log.info("Forestry Addon is client-side only, skipping");
            return;
        }

        NEIAddons.register(this);
    }

    @Override
    public void init(Side side) throws Exception {
        showSecret = NEIAddons.config.get(getName(), "Show Secret Mutations", false, "Set to true to show secret mutations").getBoolean(false);
        addSearch = NEIAddons.config.get(getName(), "Add Bees to Search", true, "Set to true to add ALL bees to NEI search").getBoolean(true);
        active = true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void loadClient() {
        beeBreedingRecipeHandler = new BeeBreedingRecipeHandler();
        API.registerRecipeHandler(beeBreedingRecipeHandler);
        API.registerUsageHandler(beeBreedingRecipeHandler);

        beeProductsRecipeHandler = new BeeProductsRecipeHandler();
        API.registerRecipeHandler(beeProductsRecipeHandler);
        API.registerUsageHandler(beeProductsRecipeHandler);

        if (addSearch) {
            for (Entry<String, IAllele> entry : AlleleManager.alleleRegistry.getRegisteredAlleles().entrySet()) {
                if (entry.getValue() instanceof IAlleleBeeSpecies) {

                    IAlleleBeeSpecies species = (IAlleleBeeSpecies) entry.getValue();

                    API.addNBTItem(BeeUtils.stackFromAllele(species, EnumBeeType.QUEEN));
                    API.addNBTItem(BeeUtils.stackFromAllele(species, EnumBeeType.DRONE));
                    API.addNBTItem(BeeUtils.stackFromAllele(species, EnumBeeType.PRINCESS));
                }
            }
        }
        FMLInterModComms.sendRuntimeMessage(this, "NEIPlugins", "register-crafting-handler", "Forestry Bees@Bee Products@beeproducts");
        FMLInterModComms.sendRuntimeMessage(this, "NEIPlugins", "register-crafting-handler", "Forestry Bees@Bee Breeding@beebreeding");
    }
}
