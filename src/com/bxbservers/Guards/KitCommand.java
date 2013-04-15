package com.bxbservers.Guards;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
				plugin.giveKit(player);
				player.sendMessage(plugin.prefix + "Your Guard Kit has been Issued. Visit the Guard room to restock");
				return true;
			}
		}
	}
	
	return false;
	
}
	
}
