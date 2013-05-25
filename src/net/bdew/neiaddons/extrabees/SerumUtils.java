/**
 * Copyright (c) bdew, 2013
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/neiaddons/master/MMPL-1.0.txt
 */

package net.bdew.neiaddons.extrabees;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import forestry.api.apiculture.EnumBeeChromosome;

public class SerumUtils {
    public static Item itemSerum;

    public static ItemStack getSerum(AlleleBeeChromosomePair pair) {
        return getSerum(pair.allele, pair.chromosome);
    }

    public static void setup() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, ClassNotFoundException {
        itemSerum = (Item) Class.forName("binnie.extrabees.core.ExtraBeeItem").getField("serum").get(null);
    }

    public static boolean isSerum(ItemStack stack) {
        return stack.getItem().itemID == itemSerum.itemID;
    }

    public static boolean shouldMakeSerum(String allele, int chromosome) {
        if (allele.equals("forestry.boolFalse"))
            return false;
        return true;
    }

    @SuppressWarnings("deprecation")
    public static AlleleBeeChromosomePair getData(ItemStack serum) {
        NBTTagCompound nbt = serum.getTagCompound();
        if (nbt.hasKey("chromosome") && nbt.hasKey("uid")) {
            int chromosome = nbt.getInteger("chromosome");
            String allele = nbt.getString("uid");
            if (chromosome >= EnumBeeChromosome.HUMIDITY.ordinal()) {
                chromosome = chromosome + 1;
            }
            return new AlleleBeeChromosomePair(allele, chromosome);
        }
        return null;
    }

    @SuppressWarnings("deprecation")
    public static ItemStack getSerum(String allele, int chromosome) {
        ItemStack serum = new ItemStack(itemSerum);
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("uid", allele);
        if (chromosome >= EnumBeeChromosome.HUMIDITY.ordinal()) {
            nbt.setInteger("chromosome", chromosome - 1);
        } else {
            nbt.setInteger("chromosome", chromosome);
        }
        nbt.setInteger("quality", 10);
        serum.setTagCompound(nbt);
        return serum;
    }
}
