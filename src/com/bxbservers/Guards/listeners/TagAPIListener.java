package com.bxbservers.Guards.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import org.kitteh.tag.PlayerReceiveNameTagEvent;

import com.bxbservers.Guards.Guards;

public class TagAPIListener implements Listener {
	
	private Guards plugin;
	
	public TagAPIListener(Guards instance){
		this.plugin = instance;
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
