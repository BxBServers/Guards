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

public class DeathListener implements Listener
{
	private Guards plugin;
	Random generator = new Random();
	
	
	public DeathListener(Guards instance)
	{
		this.plugin = instance;
	}
	
	
	//Clearing Death Messages
	@EventHandler
	public void death(PlayerDeathEvent e)
	{
		e.setDeathMessage("");
	}
	
	
	@SuppressWarnings("incomplete-switch")
	@EventHandler
	public void onDeath(EntityDeathEvent e)
	{		
		//gets player who killed
		Player killer = e.getEntity().getKiller();
		
		//if not killed by another player
		if (killer == null) 
		{
			switch (e.getEntityType())
			{
			//Guard drop items are cleared
			case PLAYER:
				Player victim = (Player)e.getEntity();
				if (plugin.onDuty.contains(victim.getName()))
				{
					e.getDrops().clear();
				}
			}
			return;
		}
				
		
		switch (e.getEntityType()) 
		{
			case PLAYER:
				
				Player victim = (Player)e.getEntity();
				
				if (plugin.onDuty.contains(victim.getName()))
				{
					//clears dropped
					e.getDrops().clear();
					
					//if guard head drop is set to true
					if (plugin.getConfig().getBoolean("dropGuardHeads"))
					{
						//creates single human skull
						ItemStack Skull = new ItemStack(Material.SKULL_ITEM, 1, (short)SkullType.PLAYER.ordinal());
						
						//gets stacks information
						SkullMeta skullMeta = (SkullMeta)Skull.getItemMeta();
						
						//sets skull owner as victims
						skullMeta.setOwner(victim.getName());						
						Skull.setItemMeta(skullMeta);
						
						//drops skull
						e.getDrops().add(Skull);
												
						//reward preset amount of money
			            EconomyResponse r = Guards.econ.depositPlayer(killer.getName(), plugin.getConfig().getInt("moneyOnGuardKill"));
			            if(r.transactionSuccess()) 
			            {
			            	//success; tell user 
			            	killer.sendMessage(String.format("You were rewarded %s for killing a guard", Guards.econ.format(r.amount)));
			            } 
			            else
			            {
			            	//fail; tell server
			                plugin.logger.severe("[Guards] Vault Error: " + r.errorMessage);
			            }
						
			            
						for(Player user: plugin.getServer().getOnlinePlayers()) 
						{
							if (!(plugin.onDuty.contains(user.getName())))
							{
								//tell non guard players that guard killed by killer
								user.sendMessage(plugin.prefix +killer.getName() + " has just killed guard " + victim.getName());
							}
						}
						return;
					} 	
			}
				
				
			//if on duty don't drop items
			if(plugin.onDuty.contains(killer.getName()))
			{
				e.getDrops().clear();
				return;
			}
			
			
			int n = plugin.getConfig().getInt("playerHeadFrequency");
			int rand = 0;
			
			if (!(n == 0))
			{
				rand = generator.nextInt(n);
			}
			
			if (rand==0 || n == 0)
			{
				if (plugin.getConfig().getBoolean("dropPlayerHeads")) 
				{
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
	public void onRespawn(PlayerRespawnEvent e)
	{
		Player player = e.getPlayer();
		if (plugin.onDuty.contains(player.getName()))
		{
				plugin.kitItems.giveItemKit(player);
				plugin.kitArmour.giveArmourKit(player);
				plugin.kitPotion.kitPotionEffect(player);
				player.sendMessage(plugin.prefix + "You have received a new kit. Don't Lose it again");
        }
	}
	
}
