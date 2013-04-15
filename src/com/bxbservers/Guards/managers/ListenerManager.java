package com.bxbservers.Guards.managers;

import com.bxbservers.Guards.Guards;
import com.bxbservers.Guards.listeners.DeathListener;
import com.bxbservers.Guards.listeners.DisableGuardEventsListener;
import com.bxbservers.Guards.listeners.HandcuffListener;
import com.bxbservers.Guards.listeners.SignListener;
import com.bxbservers.Guards.listeners.TagAPIListener;

public class ListenerManager {

	private Guards plugin;
	
	private SignListener signs;
	private TagAPIListener tag;
	private DeathListener death;
	private DisableGuardEventsListener guard;
	private HandcuffListener handcuff;
	
	public ListenerManager(Guards instance){
		plugin = instance;
		
		guard = new DisableGuardEventsListener(plugin);
		signs = new SignListener(plugin);
		tag = new TagAPIListener(plugin);
		death = new DeathListener(plugin);
		handcuff = new HandcuffListener(plugin);
	}
	
	public void initListeners() {
		plugin.getServer().getPluginManager().registerEvents(signs, plugin);
		plugin.getServer().getPluginManager().registerEvents(death, plugin);
		plugin.getServer().getPluginManager().registerEvents(guard, plugin);
		
		if (plugin.JailAPIEnabled)
			plugin.getServer().getPluginManager().registerEvents(handcuff, plugin);
		
		if (plugin.TagAPIEnabled) {
			plugin.getServer().getPluginManager().registerEvents(tag, plugin);
		} 
	}
	
}
