/*
 * Copyright (c) bdew, 2013 - 2015
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.neiaddons.forestry

import java.io.File

import codechicken.nei.config.ArrayDumper
import forestry.api.genetics.{IMutation, ISpeciesRoot}
import net.minecraft.util.{ChatComponentTranslation, IChatComponent}

class MutationDumper(root: ISpeciesRoot, suffix: String) extends ArrayDumper[IMutation]("tools.dump.neiaddons." + suffix) {

  import scala.collection.JavaConversions._

  override def header() = Array("UID", "Name", "Allele0", "Allele1", "isSecret", "baseChance", "conditions")

  override def array(): Array[IMutation] = root.getMutations(false).toArray(Array.empty)

  override def dump(mutation: IMutation, id: Int) = {
    val speciesKey = root.getKaryotypeKey.ordinal()
    Array(
      mutation.getTemplate()(speciesKey).getUID,
      mutation.getTemplate()(speciesKey).getName,
      mutation.getAllele0.getUID,
      mutation.getAllele1.getUID,
      mutation.isSecret.toString,
      "%.1f".format(mutation.getBaseChance),
      Option(mutation.getSpecialConditions) map (x => x.mkString("|")) getOrElse ""
    )
  }

  override def dumpMessage(file: File): IChatComponent =
    new ChatComponentTranslation("nei.options.tools.dump.neiaddons.dumped", translateN(name + "s"), "dumps/" + file.getName)

  override def modeCount() = 0
}

