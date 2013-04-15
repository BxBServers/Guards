package com.bxbservers.Guards;

import org.bukkit.Bukkit;
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
    		if (!(sender instanceof Player)) {
    			sender.sendMessage("This command can Only be run by a player");
    			return false;
    		} else {
    			Player player = (Player) sender;
    			if (args.length != 1) {
    		           return false;
    		        }
    			if (Guards.perms.has(player, "guards.promote")) {
    				Player target = (plugin.getServer().getPlayer(args[0]));
    				if (target==null){
    					OfflinePlayer offlineTarget	 = plugin.getServer().getOfflinePlayer(args[0]);
    					Guards.perms.playerAdd( (String)null ,offlineTarget.getName(), "guards.promote");
    					player.sendMessage(plugin.prefix + offlineTarget.getName()+" has been promoted");
    					return true;
    				}else {
    					Guards.perms.playerAdd(target, "guards.duty");
    					target.sendMessage(plugin.prefix + "Congratulations on promotion to guard");
    					player.sendMessage(plugin.prefix + target.getName()+" has been promoted");
    					return true;
    				}
    			} 
    		}
       	}
		return false;
	}

}
