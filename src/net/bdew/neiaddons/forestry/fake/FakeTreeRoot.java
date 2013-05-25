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
import forestry.api.arboriculture.EnumGermlingType;
import forestry.api.arboriculture.ITree;
import forestry.api.arboriculture.ITreeGenome;
import forestry.api.arboriculture.TreeManager;
import forestry.api.genetics.IAllele;
import forestry.api.genetics.IIndividual;
import forestry.api.genetics.IMutation;

public class FakeTreeRoot extends FakeSpeciesRoot {
    @Override
    public IIndividual getMember(ItemStack result) {
        return TreeManager.treeInterface.getTree(result);
    }

    @Override
    public ItemStack getMemberStack(IIndividual individual, int type) {
        return TreeManager.treeInterface.getGermlingStack((ITree) individual, EnumGermlingType.values()[type]);
    }

    @Override
    public Collection<? extends IMutation> getMutations(boolean b) {
        return TreeManager.treeMutations;
    }

    @Override
    public IAllele[] getTemplate(String uid) {
        return TreeManager.breedingManager.getTreeTemplate(uid);
    }

    @Override
    public IIndividual templateAsIndividual(IAllele[] template) {
        ITreeGenome gen = TreeManager.treeInterface.templateAsGenome(template);
        return TreeManager.treeInterface.getTree(Minecraft.getMinecraft().theWorld, gen);
    }

    @Override
    public boolean isMember(ItemStack ingredient) {
        return TreeManager.treeInterface.isGermling(ingredient);
    }
}
