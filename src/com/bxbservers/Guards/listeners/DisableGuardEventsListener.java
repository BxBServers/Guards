package com.bxbservers.Guards.listeners;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import com.bxbservers.Guards.Guards;

public class DisableGuardEventsListener implements Listener{

	private Guards plugin;
	
	public DisableGuardEventsListener(Guards instance)
	{
		this.plugin = instance;
	}
	
	//Stop Guards Moving Items in inventory
	@EventHandler
	public void onClick(InventoryClickEvent e){
	    if(e.getWhoClicked() instanceof Player)
	    {
	    	//Get Player who clicked
	        Player player = (Player) e.getWhoClicked();
	        String name = player.getName();
	        
	        //If user is in Creative return
	        if (player.getGameMode()== GameMode.CREATIVE)
	        {
	        	return;
	        }

	        //If User is on Duty
	        if (plugin.onDuty.contains(name)) {
	        	
	        	//Cancel Event
	        	e.setCancelled(true);
	        	e.setCursor(new ItemStack(Material.AIR));
	        	
	        	//Close Inventory and Notify User
	        	player.closeInventory();
	        	player.sendMessage(plugin.prefix +"You Cannot use your Inventory while on Duty");
	        }
	    }
	}
	
	//Stop Guards Breaking Blocks
	@EventHandler
	public void onBreak(BlockBreakEvent e)
	{
		//Get Player Info
		Player player = e.getPlayer();    
		String name = player.getName();
		
		//If player on Duty
	        if (plugin.onDuty.contains(name)) 
	        {
	        	//Cancel event and Notify User
	        	e.setCancelled(true);
	        	player.sendMessage(plugin.prefix +"You Cannot Break Blocks while on Duty");
	        }
	}
	
	//Stop Guards Placing Blocks
	@EventHandler
	public void onPlace(BlockPlaceEvent e)
	{
		//Get Player Info
		Player player = e.getPlayer();    
		String name = player.getName();
		
		//If player on Duty
	        if (plugin.onDuty.contains(name))
	        {
	        	//Cancel event and Notify user
	        	e.setCancelled(true);
	        	player.sendMessage(plugin.prefix +"You Cannot Place Blocks while on Duty");
	        }
	}
	
	//Stop Guards Dropping Blocks
	@EventHandler
	public void onDrop(PlayerDropItemEvent e)
	{
		//Get Player Info
		Player player = e.getPlayer();    
		String name = player.getName();
		
		//If Player On duty
        if (plugin.onDuty.contains(name))
        {
        //If it is not a player head	
        	if (!(e.getItemDrop().getEntityId() == 144))
        	{
       			//cancel event and notify user
        		e.setCancelled(true);
        		player.sendMessage(plugin.prefix +"You Cannot Drop Items while on Duty");
        	}
        }
	}
	
	//Stop Guards Picking up Items
	@EventHandler
	public void onPickup(PlayerPickupItemEvent e)
	{
		//Get Player Info
		Player player = e.getPlayer();    
		String name = player.getName();
		
		//If Player on Duty
	        if (plugin.onDuty.contains(name)) 
	        {
	        	//Cancel Event
   				e.setCancelled(true);
	        }
	        
	}
	
	//Stop Guards Right Clicking with Equipment
	@EventHandler
	public void equipmentClick(PlayerInteractEvent e) 
	{
		//Get Player Info
		Player player = e.getPlayer();
		
		//If a right Click action
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR)
		{
			//get Item Stack
			ItemStack stack = player.getItemInHand();
			
			//If a Special Item
			if (stack.getTypeId()==397 || stack.getTypeId() == 384 )
			{
				//Cancel Event and return Item
				e.setCancelled(true);
				stack.setAmount(1);
				player.getInventory().setItemInHand(stack);
				return;
			}
	
		}
	}
}
