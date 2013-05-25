/**
 * Copyright (c) bdew, 2013
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/neiaddons/master/MMPL-1.0.txt
 */

package net.bdew.neiaddons.extrabees;

import forestry.api.apiculture.EnumBeeChromosome;
import forestry.api.genetics.IAllele;

public class AlleleBeeChromosomePair {
    public final String allele;
    public final int chromosome;

    public AlleleBeeChromosomePair(IAllele allele, int chromosome) {
        this.allele = allele.getUID();
        this.chromosome = chromosome;
    }

    public AlleleBeeChromosomePair(String allele, int chromosome) {
        this.allele = allele;
        this.chromosome = chromosome;
    }
    
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof AlleleBeeChromosomePair)) return false;
        AlleleBeeChromosomePair other = (AlleleBeeChromosomePair) obj;
        return other.allele.equals(allele) && other.chromosome == chromosome;
    }

    @Override
    public String toString() {
        return String.format("{%s:%s}", EnumBeeChromosome.values()[chromosome].toString(), allele);
    }

    @Override
    public int hashCode() {
        return allele.hashCode() + chromosome;
    }

}
