/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/neiaddons
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/neiaddons/master/MMPL-1.0.txt
 */

package net.bdew.neiaddons.exnihilo

import net.bdew.neiaddons.exnihilo.proxies.SieveRegistryProxy
import net.minecraft.item.{Item, ItemStack}
import java.util.{List => jList}
import net.bdew.neiaddons.utils.ItemStackWithTip
import net.minecraft.util.EnumChatFormatting

class SieveRecipeHandler extends BaseRecipeHandler {
  override val getRecipeName = "ExNihilo Sieve"
  override def getRecipeId = "bdew.exnihilo.sieve"
  override val getTools = SieveRegistryProxy.sieves

  override def isPossibleInput(stack: ItemStack) = SieveRegistryProxy.sourceIds.contains(stack.getItem)
  override def isPossibleOutput(stack: ItemStack) = SieveRegistryProxy.dropIds.contains(stack.getItem)

  override def isValidTool(tool: ItemStack) = tool.getItem == Item.getItemFromBlock(SieveRegistryProxy.sieveBlock)

  import scala.collection.JavaConversions._

  override def getProcessingResults(from: ItemStack): jList[ItemStackWithTip] = {
    case class SieveResult(id: Item, meta: Int)

    // Count how many times every drop variant shows up
    val drops = collection.mutable.Map.empty[SieveResult, List[Float]].withDefaultValue(List.empty)
    for (x <- SieveRegistryProxy.getRegistry if Item.getItemFromBlock(x.source) == from.getItem && x.sourceMeta == from.getItemDamage) {
      val drop = SieveResult(x.item, x.meta)
      drops(drop) :+= 1F / x.rarity
    }

    drops.toList sortBy { case (drop, chances) => -chances.sum } map { case (drop, chances) =>
      // And convert to itemstacks
      val stack = new ItemStack(drop.id, if (chances.size > 1) chances.size else 1, drop.meta)
      val tip = if (chances.size > 1)
        List("%s%d possible drops:%s".format(EnumChatFormatting.UNDERLINE, chances.size, EnumChatFormatting.RESET)) ++
          chances.sortBy(-_).map(x => "* %.0f%%".format(x * 100))
      else
        List(
          "%sDrop chance: %.0f%%%s".format(EnumChatFormatting.WHITE, chances(0) * 100, EnumChatFormatting.RESET)
        )
      new ItemStackWithTip(stack, tip)
    }
  }

  override def getInputsFor(result: ItemStack): jList[ItemStack] =
    SieveRegistryProxy.getRegistry
      .filter(x => x.item == result.getItem && x.meta == result.getItemDamage)
      .map(x => (x.source, x.sourceMeta))
      .distinct
      .map({ case (block, meta) => new ItemStack(block, 1, meta) })

  override def getAllValidInputs: jList[ItemStack] =
    SieveRegistryProxy.getRegistry
      .filter(x => x.item != null)
      .map(x => (x.source, x.sourceMeta))
      .distinct
      .map({ case (block, meta) => new ItemStack(block, 1, meta) })
}