package com.bxbservers.Guards;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PromoteCommand implements CommandExecutor {

	private Guards plugin;
	
	public PromoteCommand(Guards plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("promoteguard")){
    		if (!(sender instanceof Player)) { // If sender isn't a player send message and fail
    			sender.sendMessage("This command can Only be run by a player");
    			return false;
    		} else {
    			Player player = (Player) sender; // cast sender to player
    			if (args.length != 1) { // if not /<command> <arguement> then fail
    		           return false;
    		        }
    			if (Guards.perms.has(player, "guards.promote")) { // If user has permission
    				Player target = (plugin.getServer().getPlayer(args[0])); // Get Target
    				if (target==null){ //If target is offline
    					OfflinePlayer offlineTarget	 = plugin.getServer().getOfflinePlayer(args[0]); //get Offline target
    					Guards.perms.playerAdd( (String)null ,offlineTarget.getName(), "guards.duty"); //add permission
    					player.sendMessage(plugin.prefix + offlineTarget.getName()+" has been promoted"); //send Confirmation Message
    					return true;
    				}else {
    					Guards.perms.playerAdd(target, "guards.duty"); //add permission
    					target.sendMessage(plugin.prefix + "Congratulations on promotion to guard. Do /duty to get started"); //send notification to target so they can celebrate
    					player.sendMessage(plugin.prefix + target.getName()+" has been promoted"); //send confirmation message
    					return true;
    				}
    			} 
    		}
       	}
		return false; 
	}

}
