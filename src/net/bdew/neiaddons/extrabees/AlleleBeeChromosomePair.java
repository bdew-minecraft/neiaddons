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
