package com.bxbservers.Guards.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.bxbservers.Guards.Guards;

public class SignListener implements Listener{

	private Guards plugin;
	
	public SignListener(Guards instance){
		this.plugin = instance;
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
							plugin.kitPotionEffect(player);
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
	
}
