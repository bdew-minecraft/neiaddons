package net.bdew.neiaddons.extrabees;

import java.util.HashSet;
import java.util.Set;

import forestry.api.apiculture.BeeManager;
import forestry.api.apiculture.EnumBeeChromosome;
import forestry.api.apiculture.IAlleleBeeSpecies;
import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.IAllele;
import java.util.Map.Entry;

public class AlleleBeeChromosomePair {
    public final String allele;
    public final int chromosome;

    public static Set<AlleleBeeChromosomePair> getAllPairs() {
        Set<AlleleBeeChromosomePair> res = new HashSet<AlleleBeeChromosomePair>();
        for (Entry<String, IAllele> entry : AlleleManager.alleleRegistry.getRegisteredAlleles().entrySet()) {
            if (entry.getValue() instanceof IAlleleBeeSpecies) {
                IAllele[] template = BeeManager.breedingManager.getBeeTemplate(entry.getValue().getUID());
                for (int i = 0; i<template.length; i++) {
                    if (template[i]!=null) {
                        System.out.println("Added: "+new AlleleBeeChromosomePair(template[i], i).toString());
                        res.add(new AlleleBeeChromosomePair(template[i], i));
                    }
                }
            }
        }
        return res;
    }
    
    public AlleleBeeChromosomePair(IAllele allele, int chromosome) {
        this.allele = allele.getUID();
        this.chromosome = chromosome;
    }

    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!(obj instanceof AlleleBeeChromosomePair))
            return false;
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
