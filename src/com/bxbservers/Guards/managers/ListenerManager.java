package com.bxbservers.Guards.managers;

import com.bxbservers.Guards.Guards;
import com.bxbservers.Guards.listeners.SignListener;

public class ListenerManager {

	private Guards plugin;
	
	private SignListener signs;
	
	public ListenerManager(Guards instance){
		plugin = instance;
		
		signs = new SignListener(plugin);
	}
	
	public void initListeners() {
		plugin.getServer().getPluginManager().registerEvents(signs, plugin);
	}
	
}
