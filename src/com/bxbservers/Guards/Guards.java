package com.bxbservers.Guards;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import com.bxbservers.Guards.managers.CommandManager;
import com.bxbservers.Guards.managers.ListenerManager;
import com.matejdro.bukkit.jail.Jail;
import com.matejdro.bukkit.jail.JailAPI;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;


public class Guards extends JavaPlugin
implements Listener
{

	private Guards plugin;
	public KitPotion kitPotion;
	public KitArmour kitArmour;
	public KitItems kitItems;
	public CommandManager cmdManager;
	public ListenerManager ListenerManager;
	
	public JailAPI jail;
	public WorldGuardPlugin WGPlugin;
	public static Economy econ = null;
	public static Permission perms = null;
	
	public boolean TagAPIEnabled;
	public boolean JailAPIEnabled;
	public boolean WorldGuardEnabled;
	
	public Logger logger;
	
	public FileConfiguration configFile;
	private FileConfiguration playerInventoryConfig = null;
	private File playerInventoryConfigFile = null;
	private FileConfiguration GuardsConfig = null;
	private File GuardsConfigFile = null;
	
	public List<String> onDuty;
	public List<String> help;

	public boolean silent = false;

	public PotionEffectType potion;

	public String prefix = ChatColor.DARK_RED + "[Guards] " + ChatColor.GOLD;

	@Override
	public void onEnable() {
	
		plugin = this;
		logger = getLogger();

		plugin.kitPotion = new KitPotion(plugin);
		plugin.kitArmour = new KitArmour(plugin);
		plugin.kitItems = new KitItems(plugin);
	
		configFile = getConfig();
		configFile.options().copyDefaults(true);
		saveDefaultConfig();
	
		reloadPlayerInventoryConfig();
		reloadGuardsConfig();
	
		this.onDuty = getGuardsConfig().getStringList("onDuty");
		this.help = getGuardsConfig().getStringList("help");

		initDependancies();

		ListenerManager = new ListenerManager(this);
		cmdManager = new CommandManager(this);
		ListenerManager.initListeners();
		cmdManager.initCommands();
    
		getLogger().info("Guards has been Enabled");
	}	
	
	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return econ != null;
	}

	private boolean setupPermissions() {
		RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
		perms = rsp.getProvider();
		return perms != null;
	}

    public void initDependancies() {
    	
    	if (!setupEconomy() ) {
            logger.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
    	
        setupPermissions();
    	
   		Plugin WGplugin = getServer().getPluginManager().getPlugin("WorldGuard");
    	// WorldGuard may not be loaded
    	if (WGplugin == null || !(WGplugin instanceof WorldGuardPlugin)) {
   		    logger.info("WorldGuardError");
   			this.WorldGuardEnabled=false;
   			logger.severe("WorldGuard not Found. Disabling dependant sections.");	
   		} else {
   			this.WorldGuardEnabled=true;
   		}
    	
        Plugin jailPlugin = getServer().getPluginManager().getPlugin("Jail");
        if (jailPlugin != null){
                jail = ((Jail) jailPlugin).API;
                this.JailAPIEnabled=true;
            }
        else {
        	this.JailAPIEnabled=false;
            }
        
        Plugin TagAPIPlugin = getServer().getPluginManager().getPlugin("TagAPI");
        if (TagAPIPlugin != null){
                this.TagAPIEnabled=true;
            }
        else {
        	this.TagAPIEnabled=false;
        	logger.severe("TagAPI Not Found. Disabling dependant sections.");
            }
    }
    
    
    //Method from http://wiki.bukkit.org/Configuration_API_Reference
    public void reloadPlayerInventoryConfig() {
        if (playerInventoryConfigFile == null) {
        playerInventoryConfigFile = new File(getDataFolder(), "PlayerInventory.yml");
        }
        playerInventoryConfig = YamlConfiguration.loadConfiguration(playerInventoryConfigFile);
 
        // Look for defaults in the jar
        InputStream defConfigStream = this.getResource("PlayerInventory.yml");
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            playerInventoryConfig.setDefaults(defConfig);
        }
    }
 
    //Method from http://wiki.bukkit.org/Configuration_API_Reference
    public FileConfiguration getPlayerInventoryConfig() {
        if (playerInventoryConfig == null) {
            this.reloadPlayerInventoryConfig();
        }
        return playerInventoryConfig;
    }
 
    //Method from http://wiki.bukkit.org/Configuration_API_Reference
    public void savePlayerInventoryConfig() {
        if (playerInventoryConfig == null || playerInventoryConfigFile == null) {
        return;
        }
        try {
            getPlayerInventoryConfig().save(playerInventoryConfigFile);
        } catch (IOException ex) {
            this.getLogger().log(Level.SEVERE, "Could not save config to " + playerInventoryConfigFile, ex);
        }
    }

    //Method from http://wiki.bukkit.org/Configuration_API_Reference
    public void reloadGuardsConfig() {
        if (GuardsConfigFile == null) {
        GuardsConfigFile = new File(getDataFolder(), "Guards.yml");
        }
        GuardsConfig = YamlConfiguration.loadConfiguration(GuardsConfigFile);
 
        // Look for defaults in the jar
        InputStream defConfigStream = this.getResource("Guards.yml");
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            GuardsConfig.setDefaults(defConfig);
        }
    }
 
    //Method from http://wiki.bukkit.org/Configuration_API_Reference
    public FileConfiguration getGuardsConfig() {
        if (GuardsConfig == null) {
            this.reloadGuardsConfig();
        }
        return GuardsConfig;
    }
 
    //Method from http://wiki.bukkit.org/Configuration_API_Reference
    public void saveGuardsConfig() {
        if (GuardsConfig == null || GuardsConfigFile == null) {
        return;
        }
        try {
            getGuardsConfig().save(GuardsConfigFile);
        } catch (IOException ex) {
            this.getLogger().log(Level.SEVERE, "Could not save config to " + GuardsConfigFile, ex);
        }
    }
    
    @Override
    public void onDisable() {
	
    	getConfig().set("onDuty", this.onDuty);
    	getConfig().set("help", this.help);

    	savePlayerInventoryConfig();
    	saveGuardsConfig();

    	getLogger().info("Guards has been disabled");
    }

}