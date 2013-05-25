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

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import forestry.api.apiculture.BeeManager;
import forestry.api.apiculture.EnumBeeType;
import forestry.api.apiculture.IBee;
import forestry.api.apiculture.IBeeGenome;
import forestry.api.genetics.IAllele;
import forestry.api.genetics.IIndividual;
import forestry.api.genetics.IMutation;

public class FakeBeeRoot extends FakeSpeciesRoot {
    @Override
    public IIndividual getMember(ItemStack result) {
        return BeeManager.beeInterface.getBee(result);
    }

    @Override
    public ItemStack getMemberStack(IIndividual individual, int type) {
        return BeeManager.beeInterface.getBeeStack((IBee) individual, EnumBeeType.values()[type]);
    }

    @Override
    public Collection<? extends IMutation> getMutations(boolean b) {
        return BeeManager.breedingManager.getMutations(b);
    }

    @Override
    public IAllele[] getTemplate(String uid) {
        return BeeManager.breedingManager.getBeeTemplate(uid);
    }

    @Override
    public IIndividual templateAsIndividual(IAllele[] template) {
        IBeeGenome gen = BeeManager.beeInterface.templateAsGenome(template);
        return BeeManager.beeInterface.getBee(Minecraft.getMinecraft().theWorld, gen);
    }

    @Override
    public boolean isMember(ItemStack ingredient) {
        return BeeManager.beeInterface.isBee(ingredient);
    }
}
