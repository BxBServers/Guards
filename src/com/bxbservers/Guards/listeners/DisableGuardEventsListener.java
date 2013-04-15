package com.bxbservers.Guards.listeners;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import com.bxbservers.Guards.Guards;

public class DisableGuardEventsListener implements Listener{

	private Guards plugin;
	
	public DisableGuardEventsListener(Guards instance){
		this.plugin = instance;
	}
	
	//Stop Guards Moving Items in inventory
	@EventHandler
	public void onClick(InventoryClickEvent e){
	    if(e.getWhoClicked() instanceof Player){
	        Player player = (Player) e.getWhoClicked(); 
	        if (player.getGameMode()== GameMode.CREATIVE){
	        	return;
	        }
	        String name = player.getName();
	        if (plugin.onDuty.contains(name)) {
	        	e.setCancelled(true);
	        	e.setCursor(new ItemStack(Material.AIR));
	        	player.closeInventory();
	        	player.sendMessage(plugin.prefix +"You Cannot use your Inventory while on Duty");
	        }
	    }
	}
	
	//Stop Guards Breaking Blocks
	@EventHandler
	public void onBreak(BlockBreakEvent e){
		Player player = e.getPlayer();    
		String name = player.getName();
	        if (plugin.onDuty.contains(name)) {
	        	e.setCancelled(true);
	        	player.sendMessage(plugin.prefix +"You Cannot Break Blocks while on Duty");
	        }
	}
	
	//Stop Guards Placing Blocks
	@EventHandler
	public void onPlace(BlockPlaceEvent e){
		Player player = e.getPlayer();    
		String name = player.getName();
	        if (plugin.onDuty.contains(name)) {
	        	e.setCancelled(true);
	        	player.sendMessage(plugin.prefix +"You Cannot Place Blocks while on Duty");
	        }
	}
	
	//Stop Guards Dropping Blocks
	@EventHandler
	public void onDrop(PlayerDropItemEvent e){
	    Player player = e.getPlayer();    
		String name = player.getName();
	        if (plugin.onDuty.contains(name)) {
	        		if (!(e.getItemDrop().getEntityId() == 144)){
	        				e.setCancelled(true);
	        				player.sendMessage(plugin.prefix +"You Cannot Drop Items while on Duty");
	        		}
	        }
	}
	
	//Stop Guards Picking up Items
	@EventHandler
	public void onPickup(PlayerPickupItemEvent e){
		Player player = e.getPlayer();    
		String name = player.getName();
	        if (plugin.onDuty.contains(name)) {
   				e.setCancelled(true);
	        }
	        
	}
	
}
