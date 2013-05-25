/**
 * Copyright (c) bdew, 2013
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/neiaddons/master/MMPL-1.0.txt
 */

package net.bdew.neiaddons.forestry.fake;

import java.util.Collection;

import net.minecraft.item.ItemStack;
import forestry.api.genetics.IAllele;
import forestry.api.genetics.IIndividual;
import forestry.api.genetics.IMutation;

public abstract class FakeSpeciesRoot {

    public abstract IIndividual getMember(ItemStack result);

    public abstract ItemStack getMemberStack(IIndividual individual, int type);

    public abstract Collection<? extends IMutation> getMutations(boolean b);

    public abstract IAllele[] getTemplate(String uid);

    public abstract IIndividual templateAsIndividual(IAllele[] template);

    public abstract boolean isMember(ItemStack ingredient);

}
