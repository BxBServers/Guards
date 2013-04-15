package com.bxbservers.Guards;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.kitteh.tag.TagAPI;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class DutyCommand implements CommandExecutor{

	private Guards plugin;
	
	public DutyCommand(Guards plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	if(cmd.getName().equalsIgnoreCase("duty")){
    		// If the player typed /basic then do the following...
    		if (!(sender instanceof Player)) {
    			sender.sendMessage("This command can Only be run by a player");
    			return false;
    		} else {   
        		if (args.length == 1) {
        			if (args[0].equalsIgnoreCase("silent")) {
        				if (sender.hasPermission("guards.duty.silent")){
        					plugin.silent=true;
        					//logger.info("Silently going on duty");
        				}
        			}
        		}
    			Player player = (Player) sender;
    			String name = player.getName();
    			if (player.hasPermission("guards.duty")) {
    				if (!plugin.onDuty.contains(name)) {
    		              setOnDuty(player, plugin.silent);
    		              plugin.onDuty.add(name);
    		              return true;
    		            } else if (plugin.onDuty.contains(name)) {
    		              setOffDuty(player, plugin.silent);
    		              plugin.onDuty.remove(name);
    		              return true;
    		            }
    				}
    			}
    		}
		return false; 
	}
	
	private void setOnDuty(Player player, Boolean silent){
		
		TagAPI.refreshPlayer(player);
		//Save Inventory Section
        plugin.getCustomConfig().set(player.getName() + ".inventory", player.getInventory().getContents());
        plugin.getCustomConfig().set(player.getName() + ".armor", player.getInventory().getArmorContents());
        //End of Section
        
        //Clear Inventory and Potion effects
		player.getInventory().clear();
		player.getInventory().setArmorContents(null);
		for (PotionEffect effect : player.getActivePotionEffects())
	        player.removePotionEffect(effect.getType());
        
        //Give Kit
		plugin.giveKit(player);
		player.sendMessage(plugin.prefix + "Your Guard Kit has been Issued. Visit the Guard room to restock");
        
        //Set Perm Group
        PermissionUser user = PermissionsEx.getUser(player);
        user.addGroup("Guard");
        
        //Announce
        if (silent) {
            player.sendMessage(plugin.prefix+"You Silently come on duty");
        } else {
        	player.sendMessage(plugin.prefix+"You are now on Duty");
        	for(Player all: plugin.getServer().getOnlinePlayers()) {
        		if (all != player){
        			all.sendMessage(plugin.prefix + "Guard " + player.getName() + " is now On Duty");
        		}
        	}
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void setOffDuty(Player player, Boolean silent){
		
		TagAPI.refreshPlayer(player);
		
		//Clear Inventory and Potion Effects
		player.getInventory().clear();
		player.getInventory().setArmorContents(null);
		for (PotionEffect effect : player.getActivePotionEffects())
	        player.removePotionEffect(effect.getType());
		
		//Load Inventory Section
		Object a = plugin.getCustomConfig().get(player.getName() + ".inventory");
        Object b = plugin.getCustomConfig().get(player.getName() + ".armor");
        if(a == null || b == null){
            player.sendMessage(plugin.prefix +"No saved inventory to load");
            return;
        }
        ItemStack[] inventory = null;
        ItemStack[] armor = null;
        if (a instanceof ItemStack[]){
              inventory = (ItemStack[]) a;
        } else if (a instanceof List){
                List lista = (List) a;
                inventory = (ItemStack[]) lista.toArray(new ItemStack[0]);
        }
        if (b instanceof ItemStack[]){
                armor = (ItemStack[]) b;
          } else if (b instanceof List){
              List listb = (List) b;
              armor = (ItemStack[]) listb.toArray(new ItemStack[0]);
          }
        player.getInventory().clear();
        player.getInventory().setContents(inventory);
        player.getInventory().setArmorContents(armor);
        player.sendMessage(plugin.prefix+"Your Inventory was loaded");
        //End of Section
        
		//Set Perm Group
        PermissionUser user = PermissionsEx.getUser(player);
        user.removeGroup("Guard");
		
		//Announce
        if (silent) {
            player.sendMessage(plugin.prefix+"You Silently come off duty");
        } else {
        	player.sendMessage(plugin.prefix+"You are now off Duty");
        	for(Player all: plugin.getServer().getOnlinePlayers()) {
        		if (all != player){
        			all.sendMessage(plugin.prefix + "Guard " + player.getName() + " is now Off Duty");
        		}
        	}
		}
	}
	
	
}
