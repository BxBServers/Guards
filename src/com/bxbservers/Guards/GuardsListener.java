package com.bxbservers.Guards;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.Sign;
import org.bukkit.entity.Arrow;
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

public class GuardsListener implements Listener{

	private Guards plugin;
	Random generator = new Random();
	
	
	public GuardsListener(Guards plugin) {
		this.plugin = plugin;
	}
	
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
	
	@EventHandler
	public void signEdit(SignChangeEvent e){
		plugin.logger.info("Event Triggered");
		String[] lines = e.getLines();
		Player player = e.getPlayer();
		//plugin.logger.info(lines[0]);
		//plugin.logger.info(plugin.getConfig().getString("kitSignHeader"));
		//plugin.logger.info(Integer.toString(lines.length));
		if ((lines.length > 1) && (lines[0].equalsIgnoreCase(ChatColor.stripColor(plugin.getConfig().getString("kitSignHeader"))))) {
			//plugin.logger.info("I GET PAST CHECK OF LINE 1");
			if (player.hasPermission("guards.sign.create")) {
				e.setLine(0, ChatColor.DARK_RED + "[" + ChatColor.GOLD + "Armoury"+ChatColor.DARK_RED+"]");
				player.sendMessage(plugin.prefix + "Armoury Created Successfully");
			} else {
				player.sendMessage(plugin.prefix + "You do not have permssion to create an Armoury");
				e.getBlock().breakNaturally();
			}
		}
	}
	
	@EventHandler
	public void signClick(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock()!=null){
			if (e.getClickedBlock().getType() == Material.WALL_SIGN || e.getClickedBlock().getType() == Material.SIGN_POST){
				Sign s = (Sign) e.getClickedBlock().getState();
				String[] lines = s.getLines();
				if (lines.length>1 && lines[0].equals(ChatColor.DARK_RED + "[" + ChatColor.GOLD + "Armoury"+ChatColor.DARK_RED+"]")){
					if (player.hasPermission("guards.sign,use")){
						if (plugin.onDuty.contains(player.getName())) {
							player.getInventory().clear();
							player.getInventory().setArmorContents(null);
							plugin.giveKit(player);
							player.sendMessage(plugin.prefix + "Kit has been Issued");
						} else {
							player.sendMessage(plugin.prefix + "You must be on Duty to use this.");
						}
					} else {
						player.sendMessage(plugin.prefix + "You do not have permssion to use an Armoury");
					}
				}
			}
		}
	}
	
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
    			if (plugin.onDuty.contains((attacker.getName())) && (i==(294))) {
    				//attacker.sendMessage("Hit wih hoe");
    				
    				if ((player.getHealth() + player.getLastDamage())<=20){
    				player.setHealth(player.getHealth() + player.getLastDamage());
    				}
    				player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,300,0));
    			}
    			if (plugin.onDuty.contains((attacker.getName())) && (i==(280))) {
    				if ((player.getHealth() + player.getLastDamage())<=20){
    				player.setHealth(player.getHealth() + player.getLastDamage());
    				}
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
	
        
	
	
	@EventHandler
	public void onBreak(BlockBreakEvent e){
		Player player = e.getPlayer();    
		String name = player.getName();
	        if (plugin.onDuty.contains(name)) {
	        	e.setCancelled(true);
	        	player.sendMessage(plugin.prefix +"You Cannot Break Blocks while on Duty");
	        }
	}
	
	
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
	
	@EventHandler
	public void onPickup(PlayerPickupItemEvent e){
		Player player = e.getPlayer();    
		String name = player.getName();
	        if (plugin.onDuty.contains(name)) {
   				e.setCancelled(true);
	        }
	        
	}
		
	@EventHandler
	public void death(PlayerDeathEvent e){
			e.setDeathMessage("");
	}
	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent e){
		Player player = e.getPlayer();
		if (plugin.onDuty.contains(player.getName())) {
				plugin.giveKit(player);
				player.sendMessage(plugin.prefix +"You have received a new kit. Don't Lose it again");
        }
	}
	
	@SuppressWarnings("incomplete-switch")
	@EventHandler
	public void onDeath(EntityDeathEvent e) {		
		Player killer = e.getEntity().getKiller();
		if (killer == null) {
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
					for(Player user: plugin.getServer().getOnlinePlayers()) {
						if (!(plugin.onDuty.contains(user.getName()))) {
							user.sendMessage(plugin.prefix +killer.getName() + " has just killed guard " + victim.getName());
						}
						return;
					}
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
	
	@EventHandler
	public void onNameTag(PlayerReceiveNameTagEvent e) {
		Player player = e.getNamedPlayer();
		if (plugin.onDuty.contains(player.getName())) {
			if (player.getName().length() <15) {
				e.setTag(ChatColor.RED + player.getName());
			}
		}
	}
}
