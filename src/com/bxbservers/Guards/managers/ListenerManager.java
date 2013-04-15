package com.bxbservers.Guards.managers;

import com.bxbservers.Guards.Guards;
import com.bxbservers.Guards.listeners.SignListener;
import com.bxbservers.Guards.listeners.TagAPIListener;

public class ListenerManager {

	private Guards plugin;
	
	private SignListener signs;
	private TagAPIListener tag;
	
	public ListenerManager(Guards instance){
		plugin = instance;
		
		signs = new SignListener(plugin);
		tag = new TagAPIListener(plugin);
	}
	
	public void initListeners() {
		plugin.getServer().getPluginManager().registerEvents(signs, plugin);
		
		
		if (plugin.getServer().getPluginManager().isPluginEnabled("TagAPI")) {
			plugin.getServer().getPluginManager().registerEvents(tag, plugin);
			plugin.TagAPIEnabled = true;
		} else {
			plugin.logger.severe("TagAPI Not Found. Disabling dependant sections.");
			plugin.TagAPIEnabled = false;
		}
	}
	
}
