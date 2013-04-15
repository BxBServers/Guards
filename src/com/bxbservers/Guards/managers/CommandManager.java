package com.bxbservers.Guards.managers;

import com.bxbservers.Guards.Guards;
import com.bxbservers.Guards.commands.DutyCommand;
import com.bxbservers.Guards.commands.KitCommand;
import com.bxbservers.Guards.commands.PromoteCommand;

public class CommandManager {
	private Guards plugin;
	
	private DutyCommand duty;
	private KitCommand kit;
	private PromoteCommand promote;
	
	public CommandManager(Guards instance){
		plugin = instance;
		
		duty = new DutyCommand(plugin);
		kit = new KitCommand(plugin);
		promote = new PromoteCommand(plugin);
		
	}
	
	public void initCommands() {
		
		plugin.getCommand("duty").setExecutor(duty);
		plugin.getCommand("promoteGuard").setExecutor(promote);
		plugin.getCommand("kit").setExecutor(kit);
		
	}
	
	
	
	

}
