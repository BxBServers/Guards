package com.bxbservers.Guards.listeners;

import java.util.LinkedList;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.bxbservers.Guards.Guards;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class RadioListener implements Listener {

	private Guards plugin;
	LinkedList< String > parentNames = new LinkedList< String >();
	LinkedList< String > regions = new LinkedList< String >(); 
	
	public RadioListener(Guards instance){
		this.plugin = instance;
	}
	
	@EventHandler
	public void equipmentClick(PlayerInteractEvent e) {
		regions.clear();
		Player player = e.getPlayer();
		Player user = e.getPlayer();
		ItemStack stack =  user.getItemInHand();
		int i = stack.getTypeId();
		int d = stack.getDurability();
		String data = plugin.getConfig().getString("radioId");
		String[] dataSplit = data.split(":") ;
		if (dataSplit.length == 1) {
			dataSplit[1]=Integer.toString(0);
		}
		if (plugin.onDuty.contains((user.getName())) && (i==(Integer.parseInt(dataSplit[0]))) && d==Integer.parseInt(dataSplit[1])) {
			if (e.getAction()==Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK){
				if (!(plugin.help.contains(user.getName()))){
				user.sendMessage(plugin.prefix+"Help Summoned");
				plugin.help.add(user.getName());
				
				if (plugin.WorldGuardEnabled) {
					RegionManager regionManager = plugin.WGPlugin.getRegionManager(player.getWorld());
					ApplicableRegionSet set = regionManager.getApplicableRegions(player.getLocation());
								
					for ( ProtectedRegion region : set ){
						regions.add(region.getId());
						ProtectedRegion parent = region.getParent();
					
						while ( parent != null ) {
							parentNames.add( parent.getId());
							parent = parent.getParent();
						}
					}
				
					for ( String name : parentNames )
						regions.remove( name );
				}
					
				int x = user.getLocation().getBlockX();
				int y = user.getLocation().getBlockY();
				int z = user.getLocation().getBlockZ();
				for(Player all: plugin.getServer().getOnlinePlayers()) {
	        		if (all != player && plugin.onDuty.contains(all.getName())){
	        			if (!(regions.isEmpty()) && (plugin.WorldGuardEnabled)){
	        			all.sendMessage(plugin.prefix + "Guard " + player.getName() + " needs help at " + regions.get(0));
	        			} else {
	        				all.sendMessage(plugin.prefix + "Guard " + player.getName() + " needs help at " + x +","+y+","+z);
	        			}
	        		}
	        	}
					
				} else {
					user.sendMessage(plugin.prefix+"You can only do this once per Life");
				}
			}
		}
	}
}