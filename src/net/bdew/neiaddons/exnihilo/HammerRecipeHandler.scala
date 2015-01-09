/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/neiaddons/master/MMPL-1.0.txt
 */

package net.bdew.neiaddons.exnihilo

import java.util.{List => jList}

import net.bdew.neiaddons.exnihilo.proxies.HammerRegistryProxy
import net.bdew.neiaddons.utils.ItemStackWithTip
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.util.EnumChatFormatting

class HammerRecipeHandler extends BaseRecipeHandler {
  override val getRecipeName = "ExNihilo Hammer"
  override def getRecipeId = "bdew.exnihilo.hammer"
  override val getTools = HammerRegistryProxy.hammers

  override def isPossibleInput(stack: ItemStack) = HammerRegistryProxy.sourceIds.contains(stack.getItem)
  override def isPossibleOutput(stack: ItemStack) = HammerRegistryProxy.dropIds.contains(stack.getItem)

  override def isValidTool(tool: ItemStack) = HammerRegistryProxy.clsBaseHammer.isInstance(tool.getItem)

  import scala.collection.JavaConversions._

  override def getProcessingResults(from: ItemStack): jList[ItemStackWithTip] = {
    case class HammerResult(item: Item, meta: Int, chance: Float, luckBonus: Float)

    // Count how many times every drop variant shows up
    val drops = collection.mutable.Map.empty[HammerResult, Int].withDefaultValue(0)
    for (x <- HammerRegistryProxy.getRegistry if x != null && Item.getItemFromBlock(x.source) == from.getItem && x.sourceMeta == from.getItemDamage) {
      val drop = HammerResult(x.item, x.meta, x.chance, x.luckMultiplier)
      drops(drop) += 1
    }

    drops.toList sortWith { case ((d1, n1), (d2, n2)) =>
      // Sort the list by drop number then chance
      if (n1 == n2)
        d1.chance > d2.chance
      else
        n1 > n2
    } map { case (drop, num) =>
      // And convert to itemstacks
      val stack = new ItemStack(drop.item, num, drop.meta)
      val tip = List(
        "%sDrop chance: %.0f%%%s".format(EnumChatFormatting.WHITE, drop.chance * 100, EnumChatFormatting.RESET),
        "%sFortune bonus: %.0f%%%s".format(EnumChatFormatting.BLUE, drop.luckBonus * 100, EnumChatFormatting.RESET)
      )
      new ItemStackWithTip(stack, tip)
    }
  }

  override def getInputsFor(result: ItemStack): jList[ItemStack] =
    HammerRegistryProxy.getRegistry
      .filterNot(_ == null)
      .filter(x => x.item == result.getItem && x.meta == result.getItemDamage)
      .map(x => (x.source, x.sourceMeta))
      .distinct
      .map({ case (block, meta) => new ItemStack(block, 1, meta) })

  override def getAllValidInputs: jList[ItemStack] =
    HammerRegistryProxy.getRegistry
      .filterNot(_ == null)
      .filter(x => x.item != null)
      .map(x => (x.source, x.sourceMeta))
      .distinct
      .map({ case (block, meta) => new ItemStack(block, 1, meta) })
}