package net.bdew.neiaddons.extrabees;

import java.util.HashSet;
import java.util.Set;

import net.bdew.neiaddons.forestry.GeneticsUtils;
import net.bdew.neiaddons.forestry.fake.FakeBeeRoot;
import codechicken.nei.api.API;
import cpw.mods.fml.common.event.FMLInterModComms;
import forestry.api.apiculture.EnumBeeChromosome;
import forestry.api.apiculture.IAlleleBeeSpecies;
import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.IAllele;

public class AddonExtraBeesClient {

    public static FakeBeeRoot beeRoot;

    public static void registerSerums() {
        Set<AlleleBeeChromosomePair> res = new HashSet<AlleleBeeChromosomePair>();

        beeRoot = new FakeBeeRoot();
        
        for (IAlleleBeeSpecies species : AddonExtraBees.allBeeSpecies) {
            IAllele[] template =  beeRoot.getTemplate(species.getUID());
            if (template==null) {
                AddonExtraBees.instance.logWarning("Template for %s is null, wtf?", species.getUID());
                continue;
            }
            for (int i = 0; i < template.length; i++) {
                if (template[i] != null) {
                    if ((!AddonExtraBees.loadBlacklisted) && AlleleManager.alleleRegistry.isBlacklisted(template[i].getUID())) {
                        if (AddonExtraBees.dumpSerums) {
                            AddonExtraBees.instance.logInfo("Skipping blacklisted allele: %s", template[i].getUID());
                        }
                        continue;
                    }
                    if (SerumUtils.shouldMakeSerum(template[i].getUID(),i)) {
                        res.add(new AlleleBeeChromosomePair(template[i], i));
                    }
                }
            }
        }

        if ( AddonExtraBees.dumpSerums) {
            AddonExtraBees.instance.logInfo("==== Serum dump ====");
            for (EnumBeeChromosome chromosome : EnumBeeChromosome.values()) {
                AddonExtraBees.instance.logInfo("%s:",chromosome.toString());
                for (AlleleBeeChromosomePair pair : res) {
                    if (pair.chromosome == chromosome.ordinal()) {
                        AlleleManager.alleleRegistry.getAllele(pair.allele);
                        AddonExtraBees.instance.logInfo(" * %s -> %s",pair.allele,SerumUtils.getSerum(pair).getDisplayName());
                    }
                }
                AddonExtraBees.instance.logInfo("===================================");
            }
        }

        for (AlleleBeeChromosomePair pair : res) {
            API.addNBTItem(SerumUtils.getSerum(pair));
        }
    }
    
    
    public static void load() {
        try {
            SerumUtils.setup();
        } catch (Throwable e) {
            AddonExtraBees.instance.logWarning("Failed to get serum item:");
            e.printStackTrace();
            return;
        }        

        AddonExtraBees.allBeeSpecies = GeneticsUtils.getAllBeeSpecies( AddonExtraBees.loadBlacklisted);
        registerSerums();
        API.registerRecipeHandler(new IsolatorRecipeHandler());
        
        FMLInterModComms.sendRuntimeMessage(AddonExtraBees.instance, "NEIPlugins", "register-crafting-handler", "Exta Bees@Isolator@isolator");
    }
}
