package com.bxbservers.Guards;

import org.bukkit.Bukkit;
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
		if (cmd.getName().equalsIgnoreCase("guard")){
    		if (!(sender instanceof Player)) {
    			sender.sendMessage("This command can Only be run by a player");
    			return false;
    		} else {
    			Player player = (Player) sender;
    			if (args.length != 1) {
    		           return false;
    		        }
    			if (Guards.perms.has(player, "guards.guard")) {
    				Player target = (Bukkit.getServer().getPlayer(args[0]));
    				Guards.perms.playerAdd(target, "guards.duty");
    				target.sendMessage(plugin.prefix + "Congratulations on promotion to guard");
    				player.sendMessage(plugin.prefix + target.getName()+" has been promoted");
    				return true;
    			} 
    		}
       	}
		return false;
	}

}
