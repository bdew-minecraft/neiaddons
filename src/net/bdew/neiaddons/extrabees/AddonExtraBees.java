package net.bdew.neiaddons.extrabees;

import net.bdew.neiaddons.BaseAddon;
import net.bdew.neiaddons.NEIAddons;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import codechicken.nei.api.API;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = NEIAddons.modid + "|ExtraBees", name = "NEI Addons: Extra Bees", version = "@@VERSION@@", dependencies = "after:NEIAddons;after:ExtraBees;after:Forestry")
public class AddonExtraBees extends BaseAddon {

    private Item itemSerum;

    @Override
    public String getName() {
        return "Extra Bees";
    }

    @Override
    public String[] getDependencies() {
        return new String[]{"ExtraBees","Forestry@[2.2.0.0,2.2.3.0)"};
    }
    
    @Override
    public boolean checkSide(Side side) {
        return side.isClient();
    }
    
    @PreInit
    public void preInit(FMLPreInitializationEvent ev) {
        doPreInit(ev);
    }

    @Override
    public void init(Side side) throws Exception {
        Object tmp = Class.forName("binnie.extrabees.core.ExtraBeeItem").getField("serum").get(null);
        if (tmp instanceof Item) {
            itemSerum = (Item) tmp;
        } else {
            logWarning("Failed to get serum item, got %s instead", tmp.toString());
            return;
        }

        active = true;
    }

    @Override
    public void loadClient() {
        for (AlleleBeeChromosomePair pair : AlleleBeeChromosomePair.getAllPairs()) {
            ItemStack serum = new ItemStack(itemSerum);
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setString("uid", pair.allele);
            nbt.setInteger("chromosome", pair.chromosome);
            serum.setTagCompound(nbt);
            API.addNBTItem(serum);
        }
    }
}
