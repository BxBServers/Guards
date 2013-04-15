package com.bxbservers.Guards;

import java.util.LinkedList;
import java.util.Random;

import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.kitteh.tag.PlayerReceiveNameTagEvent;

import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class GuardsListener implements Listener{

	private Guards plugin;
	Random generator = new Random();
	
	
	public GuardsListener(Guards plugin) {
		this.plugin = plugin;
	}
	


	
	@EventHandler
	public void equipmentClick(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR){
			ItemStack stack = player.getItemInHand();
			if (stack.getTypeId()==397 || stack.getTypeId() == 384 ){
				e.setCancelled(true);
				stack.setAmount(1);
				player.getInventory().setItemInHand(stack);
				return;
			}
				
			
		}
		Player user = e.getPlayer();
		ItemStack stack =  user.getItemInHand();
		int i = stack.getTypeId();
		int d = stack.getDurability();
		if (plugin.onDuty.contains((user.getName())) && (i==(397)) && d==0) {
			if (e.getAction()==Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK){
				if (!(plugin.help.contains(user.getName()))){
				user.sendMessage(plugin.prefix+"Help Summoned");
				plugin.help.add(user.getName());
				
				RegionManager regionManager = plugin.getWorldGuard().getRegionManager(player.getWorld());
				ApplicableRegionSet set = regionManager.getApplicableRegions(player.getLocation());
				
				LinkedList< String > parentNames = new LinkedList< String >();
				LinkedList< String > regions = new LinkedList< String >();
				
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
					
				
				
				int x = user.getLocation().getBlockX();
				int y = user.getLocation().getBlockY();
				int z = user.getLocation().getBlockZ();
				for(Player all: plugin.getServer().getOnlinePlayers()) {
	        		if (all != player && plugin.onDuty.contains(all.getName())){
	        			if (!(regions.isEmpty())){
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
	
	
	
	@SuppressWarnings({ "incomplete-switch", "deprecation" })
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        if ((event.getDamager() instanceof Player)) {
        	switch (event.getEntityType()) {
    		case PLAYER:
    			
    			Player attacker =(Player) event.getDamager();
    			Player player = (Player) event.getEntity();
    			if (plugin.onDuty.contains(attacker.getName()) && plugin.onDuty.contains(player.getName())) {
    				event.setCancelled(true);
    				return;
    			}
    			ItemStack stack =  attacker.getItemInHand();
    			int i = stack.getTypeId();
    			int d = stack.getDurability();
    			/*
    			if (i==397){
    				attacker.sendMessage("Durability is "+String.valueOf(d));
    			}
    			*/
    			if (plugin.onDuty.contains((attacker.getName())) && (i==(397)) && d==4) {
    				//attacker.sendMessage("Hit wih hoe");
    				
    				if ((player.getHealth() + player.getLastDamage())<=20){
    				player.setHealth(player.getHealth() + player.getLastDamage());
    				}
    				player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,300,0));
    			}
    			if (plugin.onDuty.contains((attacker.getName())) && (i==(397)) && d==1) {
    				if ((player.getHealth() + player.getLastDamage())<=20){
    				player.setHealth(player.getHealth() + player.getLastDamage());
    				}
    			}
    			if (plugin.onDuty.contains((attacker.getName())) && (i==(384))) {
    				attacker.sendMessage(plugin.prefix+"Player Jailed");
    			    String playerName = player.getName();
    			    int time = 18;
    			    String reason = "3 min Default Jail";
    			    plugin.jail.jailPlayer(playerName, time, null, reason);
    			    plugin.jail.getPrisoner(player.getName());

                    //plugin.jail.jailPlayer(playerName, time, null, reason);
    			        return;
    			}
        	
        	} 
        } else if(event.getDamager() instanceof Projectile){
        	Projectile arrow = (Projectile)event.getDamager();
            if (arrow.getShooter() instanceof Player){
            	Player attacker = (Player) arrow.getShooter();
            	Player player = (Player) event.getEntity();
            	if (plugin.onDuty.contains(attacker.getName()) && plugin.onDuty.contains(player.getName())) {
       				event.setCancelled(true);
       				return;
    	        }
            } else {
            	return;
            }
        }
	}
	
        
	
	

	

}
