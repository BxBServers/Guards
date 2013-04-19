package com.bxbservers.Guards.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import com.bxbservers.Guards.Guards;

public class KitCommand implements CommandExecutor{
	
	private Guards plugin;
	
	public KitCommand(Guards instance) {
		plugin = instance;
	}

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
	
    	if (cmd.getName().equalsIgnoreCase("kit")){ 
			if (!(sender instanceof Player)) { // If sender isn't a player fail
				sender.sendMessage("This command can Only be run by a player");
				return false;
			} else {
				Player player = (Player) sender; //cast sender to player
				if (Guards.perms.has(player, "guards.kit")) { //if player has perms
					player.getInventory().clear(); //clear inventory
					player.getInventory().setArmorContents(null); //clear armour
					for (PotionEffect effect : player.getActivePotionEffects()) { //remove all potion effects
				        player.removePotionEffect(effect.getType());
					}
					plugin.kitItems.giveItemKit(player);
					plugin.kitArmour.giveArmourKit(player);
					plugin.kitPotion.kitPotionEffect(player); //give Potion effects
					player.sendMessage(plugin.prefix + "Your Guard Kit has been Issued. Visit the Guard room to restock"); //send Confirmation
					return true;
				}
			}
    	}
	return false;
    }
    

	
}
