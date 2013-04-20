package com.bxbservers.Guards;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class KitArmour {

	private Guards plugin;
	
	public KitArmour(Guards instance){
		this.plugin = instance;
	}
	
	@SuppressWarnings("deprecation")
	public void giveArmourKit(Player player) {
		
		//Read Config File
		String helmet = plugin.getConfig().getString("kits.Guard.armour.helmet");
		String chestplate = plugin.getConfig().getString("kits.Guard.armour.chestplate");
		String leggings = plugin.getConfig().getString("kits.Guard.armour.leggings");
		String boots = plugin.getConfig().getString("kits.Guard.armour.boots");
		
		//Itemstacks to be given to player
		ItemStack helmetStack = null;
		ItemStack chestplateStack = null;
		ItemStack leggingsStack = null;
		ItemStack bootsStack = null;
		
		//Set Type of Armour
		if (helmet != null) {
			if (helmet.equals("leather")) {
				helmetStack = new ItemStack(Material.LEATHER_HELMET);
			}
			if (helmet.equals("iron")) {
				helmetStack = new ItemStack(Material.IRON_HELMET);
			}
			if (helmet.equals("gold")) {
				helmetStack = new ItemStack(Material.GOLD_HELMET);
			}
			if (helmet.equals("diamond")) {
				helmetStack = new ItemStack(Material.DIAMOND_HELMET);
			}
			if (helmet.equals("chainmail")) {
				helmetStack = new ItemStack(Material.CHAINMAIL_HELMET);
			}
		}

		if (chestplate != null) {
			if (chestplate.equals("leather")) {
				chestplateStack = new ItemStack(Material.LEATHER_CHESTPLATE);
			}
			if (chestplate.equals("iron")) {
				chestplateStack = new ItemStack(Material.IRON_CHESTPLATE);
			}
			if (chestplate.equals("gold")) {
				chestplateStack = new ItemStack(Material.GOLD_CHESTPLATE);
			}
			if (chestplate.equals("diamond")) {
				chestplateStack = new ItemStack(Material.DIAMOND_CHESTPLATE);
			}
			if (chestplate.equals("chainmail")) {
				chestplateStack = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
			}
		}

		if (leggings != null) {
			if (leggings.equals("leather")) {
				leggingsStack = new ItemStack(Material.LEATHER_LEGGINGS);
			}
			if (leggings.equals("iron")) {
				leggingsStack = new ItemStack(Material.IRON_LEGGINGS);
			}
			if (leggings.equals("gold")) {
				leggingsStack = new ItemStack(Material.GOLD_LEGGINGS);
			}
			if (leggings.equals("diamond")) {
				leggingsStack = new ItemStack(Material.DIAMOND_LEGGINGS);
			}
			if (leggings.equals("chainmail")) {
				leggingsStack = new ItemStack(Material.CHAINMAIL_LEGGINGS);
			}
		}

		if (boots != null) {
			if (boots.equals("leather")) {
				bootsStack = new ItemStack(Material.LEATHER_BOOTS);
			}
			if (boots.equals("iron")) {
				bootsStack = new ItemStack(Material.IRON_BOOTS);
			}
			if (boots.equals("gold")) {
				bootsStack = new ItemStack(Material.GOLD_BOOTS);
			}
			if (boots.equals("diamond")) {
				bootsStack = new ItemStack(Material.DIAMOND_BOOTS);
			}
			if (boots.equals("chainmail")) {
				bootsStack = new ItemStack(Material.CHAINMAIL_BOOTS);
			}
		}
		
		//Enchant Helmet
		if (plugin.getConfig().contains("kits.Guard.armour.helmetEnchant") && helmetStack != null) {
			for (String enchant :plugin.getConfig().getString("kits.Guard.armour.helmetEnchant").split(" ")){
				String[] enchantInfo = enchant.split(":");
				Enchantment enchantID = new EnchantmentWrapper(Integer.parseInt(enchantInfo[0]));
				helmetStack.addUnsafeEnchantment(enchantID,Integer.parseInt(enchantInfo[1]));
			}
		}
		
		//Enchant Chestplate
		if (plugin.getConfig().contains("kits.Guard.armour.chestplateEnchant") && chestplateStack != null) {
			for (String enchant :plugin.getConfig().getString("kits.Guard.armour.chestplateEnchant").split(" ")){
				String[] enchantInfo = enchant.split(":");
				Enchantment enchantID = new EnchantmentWrapper(Integer.parseInt(enchantInfo[0]));
				chestplateStack.addUnsafeEnchantment(enchantID,Integer.parseInt(enchantInfo[1]));
			}
		}
		
		//Enchant Leggings
		if (plugin.getConfig().contains("kits.Guard.armour.leggingsEnchant") && leggingsStack != null) {
			for (String enchant :plugin.getConfig().getString("kits.Guard.armour.leggingsEnchant").split(" ")){
				String[] enchantInfo = enchant.split(":");
				Enchantment enchantID = new EnchantmentWrapper(Integer.parseInt(enchantInfo[0]));
				leggingsStack.addUnsafeEnchantment(enchantID,Integer.parseInt(enchantInfo[1]));
			}
		}
		
		//Enchant Boots
		if (plugin.getConfig().contains("kits.Guard.armour.bootsEnchant") && bootsStack != null) {
			for (String enchant :plugin.getConfig().getString("kits.Guard.armour.bootsEnchant").split(" ")){
				String[] enchantInfo = enchant.split(":");
				Enchantment enchantID = new EnchantmentWrapper(Integer.parseInt(enchantInfo[0]));
				bootsStack.addUnsafeEnchantment(enchantID,Integer.parseInt(enchantInfo[1]));
			}
		}
		
		//Set Players Inventory
		if (helmetStack != null) { player.getInventory().setHelmet(helmetStack); }
		if (chestplateStack != null) { player.getInventory().setChestplate(chestplateStack); }
		if (leggingsStack != null) { player.getInventory().setLeggings(leggingsStack); }
		if (bootsStack != null) { player.getInventory().setBoots(bootsStack); }
		player.updateInventory();
	}
}
