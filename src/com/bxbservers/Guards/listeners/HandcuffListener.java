package com.bxbservers.Guards.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import com.bxbservers.Guards.Guards;

public class HandcuffListener implements Listener {
	
	private Guards plugin;
	
	public HandcuffListener(Guards instance){
		this.plugin = instance;
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
    			ItemStack stack =  attacker.getItemInHand();
    			int i = stack.getTypeId();
    			int d = stack.getDurability();
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
        }
	}
}