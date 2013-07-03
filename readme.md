NEI Addons
==========

* [Forestry](http://www.minecraftforum.net/topic/700588-) (2.2.6.0+)
 * Bee breeding and products as recipes (optionally includes secret mutations too)
 * Tree breeding and products
 * Supports bees added by other mods (Thaumic Bees, Extra Bees, etc.)
 * Adds all bees, combs, saplings and pollen types to NEI search
 * Adds item subsets for NEI menu for bees, saplings, combs, pollen, etc. if NEIPlugins is not installed
* [Extra Bees](http://www.minecraftforum.net/topic/1324321-)
 * Adds all serums to NEI search
 * Adds item subsets for NEI menu for all items
 * Isolator recipes - shows what bees can be used to get any serum
* [Applied Energistics](http://www.minecraftforum.net/topic/1625015-)
 * [?] button support for the Pattern Encoder
 * Ability to create patterns directly from recipes, even if you don't have the items on you
* [Misc Peripherals](http://www.computercraft.info/forums2/index.php?/topic/4587-)
 * [?] button support for the Computer Controlled Crafter
 * Can set the recipe even if you don't have the items on you
* Various crafting tables - adds [?] button support for alternate crafting tables from many mods
 * [BuildCraft](http://www.mod-buildcraft.com/) Autocrafting Table
 * [Equivalent Exchange 3](http://www.minecraftforum.net/topic/1540010-) Minium Stone
 * [Andrew2448's addon for MachineMuse's Modular Powersuits](https://github.com/Andrew2448/Andrew2448PowersuitAddons/) In-Place Assembler
 
***
Changelog
=========

###v1.8.1 - Released 03/07/2013
* Added option to disable various mutation/products display
* Product stacks will be merged to prevent ExtraTrees crazyness
* Removed workbench backpacks [?] support, it was added in the mod itself

###v1.8.0 - Released 20/06/2013
* Updated to Forestry API 2.2.8.x (should still work with 2.2.6.x)  
* Updated to Extra Bees 1.6-pre15 (incompatible with earlier versions)
* Hidden combs from Extra/Magic Bees will be registered in NEI if "Add Combs to Search"
* Added Butterfly preliminary mutations display 

###v1.7.2 - Released 14/06/2013
* Fixed MagicBees class names / modid (forestry combs subset)
* Build against forge 7.8.0.727 (required by changes in NEI)  

###v1.7.1 - Released 01/06/2013
* Fixed Extra Bees addon incorrectly trying to work with older versions of forestry (2.2.6.0+ required)
* Fixed Extra Bees addon not loading if errors happen when dumping serums list

###v1.7 - Released 30/05/2013
* Updated for Forestry 2.2.6.0, will not work with earlier versions
* Added module for Misc Peripherals
 * Can change the recipe in a Computer Controlled Crafter directly from NEI
* Added [?] button support for Backpacks mod Workbench Backpacks
* Added item subsets for Extra Bees (everything)
* Added item subsets for Forestry bees, saplings, etc. - only if NEIPlugins is not installed

###v1.6.1 - Released 29/05/2013
* Fixed various weird NPE crashes in Forestry Addon
 
###v1.6 - Released 25/05/2013
* Added module for Extra Bees
 * Adds all serums to NEI search
 * Isolator recipes - shows what bees can be used to get any serum
* Forestry tree breeding and products
* Added module for various crafting tables
 * EE3 Minium Stone, BC Autocrafting Table, MPSA In-Place Assembler
* Performance improvements
 * Slowdowns in unrelated item lookups should be severely reduced, this will be more noticeable with many added bees from addons

###1.5 - Released 21/05/2013
* Updated for forestry 2.2.4.0
 * Added version checking, so it won't crash with older versions

###1.4 - Released 20/05/2013
* Mod renamed
* Added AE and EE3 addons
* Added option to register all combs with NEI
* Config file renamed, old config will be ignored, sorry

###1.3 - Released 18/05/2013
* Added NEIPlugins support (thanks mistaqur)

###1.2 - Released 11/05/2013
* Added all bees to the NEI search list

###1.1 - Released 10/05/2013
* Fixed crash on bees that don't produce anything

###1.0 - Released 10/05/2013
* Initial public release

***
### [Downloads](http://bit.ly/189xpv6)
### [Minecraft Forums thread](http://www.minecraftforum.net/topic/1803460-)
***
![Bee Breeding window](http://i.imgur.com/ENCP9He.png) ![Bee Products window](http://i.imgur.com/Jwv4n0Q.png)
***

This mod is distributed under the terms of the Minecraft Mod Public License 1.0, or MMPL. 

Please check the contents of the license located in https://raw.github.com/bdew/neiaddons/master/MMPL-1.0.txt
