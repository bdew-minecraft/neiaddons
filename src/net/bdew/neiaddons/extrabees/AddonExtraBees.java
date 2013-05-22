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

@Mod(modid = NEIAddons.modid + "|ExtraBees", name = "NEI Addons: Extra Bees", version = "@@VERSION@@", dependencies = "after:NEIAddons;after:ExtraBees")
public class AddonExtraBees extends BaseAddon {

    private Item itemSerum;

    @Override
    public String getName() {
        return "Extra Bees";
    }

    @PreInit
    @Override
    public void preInit(FMLPreInitializationEvent ev) {
        super.preInit(ev);

        if (ev.getSide() != Side.CLIENT) {
            logInfo("ExtraBees Addon is client-side only, skipping");
            return;
        }

        if (!verifyModVersion("ExtraBees"))
            return;

        if (!verifyModVersion("Forestry@[2.2.0.0,2.2.3.0)"))
            return;

        NEIAddons.register(this);
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
