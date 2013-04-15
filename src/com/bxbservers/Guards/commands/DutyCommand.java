package com.bxbservers.Guards.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.kitteh.tag.TagAPI;

import com.bxbservers.Guards.Guards;
import com.bxbservers.Guards.GuardsListener;

public class DutyCommand implements CommandExecutor{

	  private Guards plugin;
	  public GuardsListener listener;
	  public CommandManager CmdManager;
	  public KitCommand kit;
	  public PromoteCommand promote;
	
	public DutyCommand(Guards plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	if(cmd.getName().equalsIgnoreCase("duty")){
    		if (!(sender instanceof Player)) { //if not sent by player fail
    			sender.sendMessage("This command can Only be run by a player");
    			return false;
    		} else {   
        		if (args.length == 1) { //if has arguement
        			if (args[0].equalsIgnoreCase("silent")) { //check arguement is silent
        				if (sender.hasPermission("guards.duty.silent")){ //if user has permission
        					plugin.silent=true; //set silent to true
        				}
        			}
        		}
        		//If there is no arguement
    			Player player = (Player) sender; //cast sender to player 
    			String name = player.getName(); //get player name
    			if (player.hasPermission("guards.duty")) { //if player has perms
    				if (!plugin.onDuty.contains(name)) { //If they are not on duty
    		              setOnDuty(player, plugin.silent); //set onduty
    		              plugin.onDuty.add(name); //add to list
    		              return true;
    		            } else if (plugin.onDuty.contains(name)) { //if they are on duty
    		              setOffDuty(player, plugin.silent); //set off Duty
    		              plugin.onDuty.remove(name); //remove name from list
    		              return true;
    		            }
    				}
    			}
    		}
		return false; 
	}
	
	private void setOnDuty(Player player, Boolean silent){
		
		TagAPI.refreshPlayer(player); //refresh players tag. Makes it go red
		
		//Save Inventory Section
        plugin.getCustomConfig().set(player.getName() + ".inventory", player.getInventory().getContents());
        plugin.getCustomConfig().set(player.getName() + ".armor", player.getInventory().getArmorContents());
        //End of Section
        
        //Clear Inventory and Potion effects
		player.getInventory().clear();
		player.getInventory().setArmorContents(null);
		for (PotionEffect effect : player.getActivePotionEffects())
	        player.removePotionEffect(effect.getType());
        //End of Section
		
		
        //Give Kit
		plugin.giveKit(player);
		plugin.logger.info("I get here and player name is" + player.getName());
		plugin.kitPotionEffect(player);
		player.sendMessage(plugin.prefix + "Your Guard Kit has been Issued. Visit the Guard room to restock");
        //End of Section
        
        //Set Perm Group
        Guards.perms.playerAddGroup(player, "Guard");
        //End of Section
        
        //Announce
        if (silent) {
            player.sendMessage(plugin.prefix+"You Silently come on duty");
			plugin.silent =false;
        } else {
        	player.sendMessage(plugin.prefix+"You are now on Duty");
        	for(Player all: plugin.getServer().getOnlinePlayers()) {
        		if (all != player){
        			all.sendMessage(plugin.prefix + "Guard " + player.getName() + " is now On Duty");
        		}
        	}
		}
        //End of Section
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void setOffDuty(Player player, Boolean silent){
		
		TagAPI.refreshPlayer(player); //refresh players tag. Makes it go red.
		
		//Clear Inventory and Potion Effects
		player.getInventory().clear();
		player.getInventory().setArmorContents(null);
		for (PotionEffect effect : player.getActivePotionEffects())
	        player.removePotionEffect(effect.getType());
        //End of Section
		
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
        Guards.perms.playerRemoveGroup(player, "guard");
        //End of Section
		
		//Announce
        if (silent) {
            player.sendMessage(plugin.prefix+"You Silently come off duty");
			plugin.silent =false;
        } else {
        	player.sendMessage(plugin.prefix+"You are now off Duty");
        	for(Player all: plugin.getServer().getOnlinePlayers()) {
        		if (all != player){
        			all.sendMessage(plugin.prefix + "Guard " + player.getName() + " is now Off Duty");
        		}
        	}
		}
        //End of Section
	}
	
	
}
