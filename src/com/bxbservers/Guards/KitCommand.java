package com.bxbservers.Guards;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class KitCommand implements CommandExecutor{
	
	private Guards plugin;
	
	public KitCommand(Guards plugin) {
		this.plugin = plugin;
	}

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
	
    	if (cmd.getName().equalsIgnoreCase("kit")){ 
			if (!(sender instanceof Player)) {
				sender.sendMessage("This command can Only be run by a player");
				return false;
			} else {
				Player player = (Player) sender;
				if (Guards.perms.has(player, "guards.kit")) {
					player.getInventory().clear();
					player.getInventory().setArmorContents(null);
					for (PotionEffect effect : player.getActivePotionEffects()) {
				        player.removePotionEffect(effect.getType());
					}
					plugin.giveKit(player);
					plugin.kitPotionEffect(player);
					player.sendMessage(plugin.prefix + "Your Guard Kit has been Issued. Visit the Guard room to restock");
					return true;
				}
			}
    	}
	return false;
    }
	
}
