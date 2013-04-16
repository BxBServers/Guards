package com.bxbservers.Guards.listeners;

import java.util.Random;

import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.bxbservers.Guards.Guards;

public class DeathListener implements Listener {

	private Guards plugin;
	Random generator = new Random();
	
	public DeathListener(Guards instance){
		this.plugin = instance;
	}
	
	//Remove Death Messages
	@EventHandler
	public void death(PlayerDeathEvent e){
			e.setDeathMessage("");
	}
	
	@SuppressWarnings("incomplete-switch")
	@EventHandler
	public void onDeath(EntityDeathEvent e) {		
		Player killer = e.getEntity().getKiller();
		if (killer == null) {
			switch (e.getEntityType()) {
			case PLAYER:
				Player victim = (Player)e.getEntity();
				if (plugin.onDuty.contains(victim.getName())) {
					e.getDrops().clear();
				}
			}
			return;
		}
		switch (e.getEntityType()) {
		case PLAYER:
			Player victim = (Player)e.getEntity();
			if (plugin.onDuty.contains(victim.getName())) {
				e.getDrops().clear();
				if (plugin.getConfig().getBoolean("dropGuardHeads")) {
					plugin.logger.info("Head Dropping Enabled");
					ItemStack Skull = new ItemStack(Material.SKULL_ITEM, 1, (short)SkullType.PLAYER.ordinal());
					SkullMeta skullMeta = (SkullMeta)Skull.getItemMeta();
					skullMeta.setOwner(victim.getName());
					Skull.setItemMeta(skullMeta);
					e.getDrops().add(Skull);
					
		            EconomyResponse r = Guards.econ.depositPlayer(killer.getName(), plugin.getConfig().getInt("moneyOnGuardKill"));
		            if(r.transactionSuccess()) {
		               killer.sendMessage(String.format("You were rewarded %s for killing a guard", Guards.econ.format(r.amount)));
		            } else {
		                killer.sendMessage(String.format("An error occured: %s", r.errorMessage));
		            }
					
					for(Player user: plugin.getServer().getOnlinePlayers()) {
						if (!(plugin.onDuty.contains(user.getName()))) {
							user.sendMessage(plugin.prefix +killer.getName() + " has just killed guard " + victim.getName());
						}
						
					}
					return;
				} else {
					plugin.logger.info("Head Dropping Disabled");
				}		
			}
			if(plugin.onDuty.contains(killer.getName())) {
				e.getDrops().clear();
				return;
			}
			
			int n = plugin.getConfig().getInt("playerHeadFrequency");
			int rand = generator.nextInt(n);
			plugin.logger.info(Integer.toString(rand));
			if (rand==0){
				if (plugin.getConfig().getBoolean("dropPlayerHeads")) {
					plugin.logger.info("Head Dropping Enabled");
					ItemStack Skull = new ItemStack(Material.SKULL_ITEM, 1, (short)SkullType.PLAYER.ordinal());
					SkullMeta skullMeta = (SkullMeta)Skull.getItemMeta();
					skullMeta.setOwner(victim.getName());
					Skull.setItemMeta(skullMeta);
					e.getDrops().add(Skull);
				}
			}
					
			
		}
		
	}
	
	//Gives Guard New Kit on Respawn
	@EventHandler
	public void onRespawn(PlayerRespawnEvent e){
		Player player = e.getPlayer();
		if (plugin.onDuty.contains(player.getName())) {
				plugin.giveKit(player);
				plugin.kitPotion.kitPotionEffect(player);
				player.sendMessage(plugin.prefix +"You have received a new kit. Don't Lose it again");
        }
	}
	
}
