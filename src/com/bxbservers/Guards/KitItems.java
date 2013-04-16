package com.bxbservers.Guards;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class KitItems {

	private Guards plugin;
	
	public KitItems(Guards instance){
		this.plugin = instance;
	}
	
		public void giveItemKit(Player player) {

		plugin.help.remove(player.getName());
		plugin.getLogger().info("Giving kit to " + player.getName());
		int slot;
		String className = "Guard";

		// Item Section
		for (slot = 0; slot<=35; slot++) {
			ItemStack i = new ItemStack(0);
			String getSlot = plugin.getConfig().getString("kits." + className + ".items." + slot);
		    if (plugin.getConfig().contains("kits." + className + ".items." + slot) && !(plugin.getConfig().getString("kits." + className + ".items." + slot).equals("0")) && !(plugin.getConfig().getString("kits." + className + ".items." + slot).equals(""))) {
		    	String[] s = getSlot.split(" ");
		    	String[] item = s[0].split(":");

		    	//Sets the block/item
		    	i.setTypeId(Integer.parseInt(item[0]));

		    	//Sets the amount and durability
		    	if (item.length > 1) {
		    		i.setAmount(Integer.parseInt(item[1]));

		    		if (item.length > 2) {
		    			i.setDurability((short)Integer.parseInt(item[2]));
		    		}

		    	} else {
		    		i.setAmount(1); //Default amount is 1
		    	}

		    	if (plugin.getConfig().contains("kits." + className + ".items" + ".names." + slot) ) {
		    		//get item name
		    		String name = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("kits." + className + ".items" + ".names." + slot));
		    		if (name.equalsIgnoreCase("<username>")){
		    			name=player.getName();
		    		}
		    		ItemMeta im = i.getItemMeta();
		    		if (name.equals(ChatColor.RESET + "" + ChatColor.RESET)) {
		    			im.setDisplayName(name + im.getDisplayName());
		    		} else {
		    			im.setDisplayName(name);
		    		}

		    		i.setItemMeta(im);
		    	}

		    	// Sets the enchantments and level
		    	Boolean first = true;

		    	if (s.length > 1) {
		    		for (String a : s) {
		    			if (!first) {
		    				String[] enchant = a.split(":");
		    				Enchantment enchantmentInt = new EnchantmentWrapper(Integer.parseInt(enchant[0]));
		    				int levelInt = Integer.parseInt(enchant[1]);
		    				i.addUnsafeEnchantment(enchantmentInt,levelInt);
		    			}
		    			first = false;
		    		}
		    	}	
		    	player.getInventory().setItem(slot, i);
		    }
		}
	}
}
