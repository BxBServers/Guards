package com.bxbservers.Guards.listeners;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.bxbservers.Guards.Guards;

public class EntityDamageListener implements Listener {

	private Guards plugin;
	
	public EntityDamageListener(Guards instance){
		this.plugin = instance;
	}
	
	@SuppressWarnings({ "incomplete-switch" })
	@EventHandler (priority = EventPriority.LOWEST)
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
    			
    			String data = plugin.getConfig().getString("pepperSprayId");
    			String[] dataSplit = data.split(":") ;
    			if (dataSplit.length == 1) {
    				dataSplit[1]=Integer.toString(0);
    			}

    			if (plugin.onDuty.contains((attacker.getName())) && (i==(Integer.parseInt(dataSplit[0]))) && d==Integer.parseInt(dataSplit[1])) {
    				
    				if ((player.getHealth() + player.getLastDamage())<=20){
    				player.setHealth(player.getHealth() + player.getLastDamage());
    				}
    				player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,300,0));
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
