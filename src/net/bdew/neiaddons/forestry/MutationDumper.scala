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
    val conditions = try {
      Option(mutation.getSpecialConditions) map (x => x.mkString("|")) getOrElse ""
    } catch {
      case t: Throwable =>
        AddonForestry.instance.logSevereExc(t, "Error in mutation.getSpecialConditions for mutation %s + %s -> %s",
          mutation.getAllele0.getUID, mutation.getAllele1.getUID, mutation.getTemplate()(0).getUID)
        "[Error]"
    }
    Array(
      mutation.getTemplate()(speciesKey).getUID,
      mutation.getTemplate()(speciesKey).getName,
      mutation.getAllele0.getUID,
      mutation.getAllele1.getUID,
      mutation.isSecret.toString,
      "%.1f".format(mutation.getBaseChance),
      conditions
    )
  }

  override def dumpMessage(file: File): IChatComponent =
    new ChatComponentTranslation("nei.options.tools.dump.neiaddons.dumped", translateN(name + "s"), "dumps/" + file.getName)

  override def modeCount() = 0
}

