package com.bxbservers.Guards;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.bxbservers.Guards.commands.CommandManager;
import com.matejdro.bukkit.jail.Jail;
import com.matejdro.bukkit.jail.JailAPI;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;


public class Guards extends JavaPlugin
implements Listener
{

public List<String> onDuty;
public CommandManager cmdManager;
public static Economy econ = null;
public static Permission perms = null;
public List<String> help;
   public boolean silent = false;
public GuardsListener listener;
public FileConfiguration configFile;
public Logger logger;
public PotionEffectType potion;
private FileConfiguration customConfig = null;
private File customConfigFile = null;
public JailAPI jail;
public String prefix = ChatColor.DARK_RED + "[Guards] " + ChatColor.GOLD;

public WorldGuardPlugin getWorldGuard() {
Plugin WGplugin = getServer().getPluginManager().getPlugin("WorldGuard");

// WorldGuard may not be loaded
if (WGplugin == null || !(WGplugin instanceof WorldGuardPlugin)) {
logger.info("WorldGuardError");
return null; // Maybe you want throw an exception instead

}

return (WorldGuardPlugin) WGplugin;
}


@Override
public void onEnable() {

if (!setupEconomy() ) {
            logger.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
cmdManager = new CommandManager(this);
logger = getLogger();
configFile = getConfig();
configFile.options().copyDefaults(true);
saveDefaultConfig();
this.onDuty = getConfig().getStringList("onDuty");
this.help = getConfig().getStringList("help");
        reloadCustomConfig();
        setupPermissions();
        
getLogger().info("Guards has been Enabled");

listener = new GuardsListener(this);
getServer().getPluginManager().registerEvents(listener, this);

cmdManager.initCommands();
        
        Plugin jailPlugin = getServer().getPluginManager().getPlugin("Jail");
        if (jailPlugin != null)
        {
            jail = ((Jail) jailPlugin).API;
        }
        else
        {
            //Code here will run if player don't have Jail installed.
            //Use that to disable features of your plugin that include Jail to prevent errors.
        }
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

public void givePotionEffect (Player player, String name, int duration, int potency){

switch (name.toLowerCase())
{
case "blindness":
potion = PotionEffectType.BLINDNESS;
break;
case "confusion":
potion = PotionEffectType.CONFUSION;
break;
case "damage_resistance":
potion = PotionEffectType.DAMAGE_RESISTANCE;
break;
case "fast_digging":
potion = PotionEffectType.FAST_DIGGING;
break;
case "fire_resistance":
potion = PotionEffectType.FIRE_RESISTANCE;
break;
case "harm":
potion = PotionEffectType.HARM;
break;
case "heal":
potion = PotionEffectType.HEAL;
break;
case "hunger":
potion = PotionEffectType.HUNGER;
break;
case "jump":
potion = PotionEffectType.JUMP;
break;
case "poison":
potion = PotionEffectType.POISON;
break;
case "regenration":
potion = PotionEffectType.REGENERATION;
break;
case "slow":
potion = PotionEffectType.SLOW;
break;
case "speed":
potion = PotionEffectType.SPEED;
break;
case "increased_damage":
potion = PotionEffectType.INCREASE_DAMAGE;
break;
case "water_breathing":
potion = PotionEffectType.WATER_BREATHING;
break;
case "weakness":
potion = PotionEffectType.WEAKNESS;
break;
case "wither":
potion = PotionEffectType.WITHER;
break;
case "invisibility":
potion = PotionEffectType.INVISIBILITY;
break;
case "night_vision":
potion = PotionEffectType.NIGHT_VISION;
break;
default:
potion = null;
break;
}

if (potion != null) {
player.addPotionEffect(new PotionEffect(potion,duration,potency));
}
}

public void kitPotionEffect(Player player){
List<String> listPotions;
listPotions = getConfig().getStringList("kits.Guard.potions");
//logger.info(listPotions.get(1));
int n = listPotions.size() -1;
int i;
for(i=0; i<=n ; i++) {
String data = listPotions.get(i);
String[] potion =data.split(":");
givePotionEffect(player, potion[0], Integer.MAX_VALUE, Integer.parseInt(potion[1]));
}

}

public ItemStack setColor(ItemStack item, int color) {
/*CraftItemStack craftStack = null;
net.minecraft.server.v1_4_5.ItemStack itemStack = null;
if (item instanceof CraftItemStack) {
craftStack = (CraftItemStack) item;
itemStack = craftStack.getHandle();
} else if (item instanceof ItemStack) {
craftStack = new CraftItemStack(item);
itemStack = craftStack.getHandle();
}
NBTTagCompound tag = itemStack.tag;
if (tag == null) {
tag = new NBTTagCompound();
tag.setCompound("display", new NBTTagCompound());
itemStack.tag = tag;
}
tag = itemStack.tag.getCompound("display");
tag.setInt("color", color);
itemStack.tag.setCompound("display", tag);*/
LeatherArmorMeta im = (LeatherArmorMeta) item.getItemMeta();
im.setColor(Color.fromRGB(color));
item.setItemMeta(im);
return item;
}

@SuppressWarnings("deprecation")
public void giveKit(Player player) {

this.help.remove(player.getName());
getLogger().info("Giving kit to " + player.getName());
int slot;
String className = "Guard";

// Item Section
for (slot = 0; slot<=35; slot++) {
ItemStack i = new ItemStack(0);
String getSlot = this.getConfig().getString("kits." + className + ".items." + slot);
            if (this.getConfig().contains("kits." + className + ".items." + slot) && !(this.getConfig().getString("kits." + className + ".items." + slot).equals("0")) && !(this.getConfig().getString("kits." + className + ".items." + slot).equals(""))) {
String[] s = getSlot.split(" ");
String[] item = s[0].split(":");

//Sets the block/item
i.setTypeId(Integer.parseInt(item[0]));

//Sets the amount and durability
if (item.length > 1) {
i.setAmount(Integer.parseInt(item[1]));

if (item.length > 2) {
i.setDurability((short)Integer.parseInt(item[2]));
}

} else {
i.setAmount(1); //Default amount is 1
}

if (this.getConfig().contains("kits." + className + ".items" + ".names." + slot) ) {
//get item name
String name = ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("kits." + className + ".items" + ".names." + slot));
if (name.equalsIgnoreCase("<username>")){
name=player.getName();
}
ItemMeta im = i.getItemMeta();
if (name.equals(ChatColor.RESET + "" + ChatColor.RESET)) {
im.setDisplayName(name + im.getDisplayName());
} else {
im.setDisplayName(name);
//im.setLore(lore);
}

i.setItemMeta(im);

}

// Sets the enchantments and level
Boolean first = true;

if (s.length > 1) {
for (String a : s) {
if (!first) {
String[] enchant = a.split(":");
Enchantment enchantmentInt = new EnchantmentWrapper(Integer.parseInt(enchant[0]));
int levelInt = Integer.parseInt(enchant[1]);
i.addUnsafeEnchantment(enchantmentInt,levelInt);
}
first = false;
}
}	
player.getInventory().setItem(slot, i);
}
}
// End of Item section

//Sets the armor contents
String getHelmet = this.getConfig().getString("kits." + className + ".items" + ".helmet");
String getChestplate = this.getConfig().getString("kits." + className + ".items" + ".chestplate");
String getLeggings = this.getConfig().getString("kits." + className + ".items" + ".leggings");
String getBoots = this.getConfig().getString("kits." + className + ".items" + ".boots");



//These hold the chosen colours for dying
int helmColor = 0;
int chestColor = 0;
int legColor = 0;
int bootColor = 0;

/**
* Main item stacks for various armour types.
* They will not necessarily all be used, only those
* that the user wishes to use and has defined in the config.
*/
ItemStack lhelmet = new ItemStack(Material.LEATHER_HELMET);
ItemStack lchestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
ItemStack lleggings = new ItemStack(Material.LEATHER_LEGGINGS);
ItemStack lboots = new ItemStack(Material.LEATHER_BOOTS);

ItemStack ihelmet = new ItemStack(Material.IRON_HELMET, 1);
ItemStack ichestplate = new ItemStack(Material.IRON_CHESTPLATE, 1);
ItemStack ileggings = new ItemStack(Material.IRON_LEGGINGS, 1);
ItemStack iboots = new ItemStack(Material.IRON_BOOTS, 1);

ItemStack ghelmet = new ItemStack(Material.GOLD_HELMET, 1);
ItemStack gchestplate = new ItemStack(Material.GOLD_CHESTPLATE, 1);
ItemStack gleggings = new ItemStack(Material.GOLD_LEGGINGS, 1);
ItemStack gboots = new ItemStack(Material.GOLD_BOOTS, 1);

ItemStack dhelmet = new ItemStack(Material.DIAMOND_HELMET, 1);
ItemStack dchestplate = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
ItemStack dleggings = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
ItemStack dboots = new ItemStack(Material.DIAMOND_BOOTS, 1);

ItemStack chelmet = new ItemStack(Material.CHAINMAIL_HELMET);
ItemStack cchestplate = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
ItemStack cleggings = new ItemStack(Material.CHAINMAIL_LEGGINGS);
ItemStack cboots = new ItemStack(Material.CHAINMAIL_BOOTS);

//ItemStack for final armour items
ItemStack finalHelmet = null;
ItemStack finalChestplate = null;
ItemStack finalLeggings = null;
ItemStack finalBoots = null;

//Dying leather armour
if (this.getConfig().contains("kits." + className + ".items" + ".helmetColor")) {
helmColor = Integer.parseInt(this.getConfig().getString("kits." + className + ".items.helmetColor").replace("#", ""), 16);
lhelmet = this.setColor(lhelmet, helmColor);	
}

if (this.getConfig().contains("kits." + className + ".items" + ".chestplateColor")) {
chestColor = Integer.parseInt(this.getConfig().getString("kits." + className + ".items.chestplateColor").replace("#", ""), 16);
lchestplate = this.setColor(lchestplate, chestColor);
}

if (this.getConfig().contains("kits." + className + ".items" + ".leggingColor")) {
legColor = Integer.parseInt(this.getConfig().getString("kits." + className + ".items.leggingColor").replace("#", ""), 16);
lleggings = this.setColor(lleggings, legColor);
}

if (this.getConfig().contains("kits." + className + ".items" + ".bootColor")) {
bootColor = Integer.parseInt(this.getConfig().getString("kits." + className + ".items.bootColor").replace("#", ""), 16);
lboots= this.setColor(lboots, bootColor);
}

//Determine which type of armour they want to use
if (getHelmet != null) {
if (getHelmet.equals("leather")) {
finalHelmet = lhelmet;
}
if (getHelmet.equals("iron")) {
finalHelmet = ihelmet;
}
if (getHelmet.equals("gold")) {
finalHelmet = ghelmet;
}
if (getHelmet.equals("diamond")) {
finalHelmet = dhelmet;
}
if (getHelmet.equals("chainmail")) {
finalHelmet = chelmet;
}

}

if (getChestplate != null) {
if (getChestplate.equals("leather")) {
finalChestplate = lchestplate;
}
if (getChestplate.equals("iron")) {
finalChestplate = ichestplate;
}
if (getChestplate.equals("gold")) {
finalChestplate = gchestplate;
}
if (getChestplate.equals("diamond")) {
finalChestplate = dchestplate;
}
if (getChestplate.equals("chainmail")) {
finalChestplate = cchestplate;
}

}

if (getLeggings != null) {
if (getLeggings.equals("leather")) {
finalLeggings = lleggings;
}
if (getLeggings.equals("iron")) {
finalLeggings = ileggings;
}
if (getLeggings.equals("gold")) {
finalLeggings = gleggings;
}
if (getLeggings.equals("diamond")) {
finalLeggings = dleggings;
}
if (getLeggings.equals("chainmail")) {
finalLeggings = cleggings;
}
}

if (getBoots != null) {
if (getBoots.equals("leather")) {
finalBoots = lboots;
}
if (getBoots.equals("iron")) {
finalBoots = iboots;
}
if (getBoots.equals("gold")) {
finalBoots = gboots;
}
if (getBoots.equals("diamond")) {
finalBoots = dboots;
}
if (getBoots.equals("chainmail")) {
finalBoots = cboots;
}

}

short s1 = (short) this.getConfig().getInt("kits." + className + ".items.helmetDurability", -2);
short s2 = (short) this.getConfig().getInt("kits." + className + ".items.chestplateDurability", -2);
short s3 = (short) this.getConfig().getInt("kits." + className + ".items.leggingsDurability", -2);
short s4 = (short) this.getConfig().getInt("kits." + className + ".items.bootsDurability", -2);

if (s1 == -1) s1 = finalHelmet.getType().getMaxDurability();
if (s2 == -1) s2 = finalChestplate.getType().getMaxDurability();
if (s3 == -1) s3 = finalLeggings.getType().getMaxDurability();
if (s4 == -1) s4 = finalBoots.getType().getMaxDurability();

if (this.getConfig().getString("kits." + className + ".items.helmetName") != null) {
ItemMeta im = finalHelmet.getItemMeta();
String name = this.getConfig().getString("kits." + className + ".items.helmetName");
name = ChatColor.translateAlternateColorCodes('&', name);
im.setDisplayName(name);
finalHelmet.setItemMeta(im);
}
if (this.getConfig().getString("kits." + className + ".items.chestplateName") != null) {
ItemMeta im = finalChestplate.getItemMeta();
String name = this.getConfig().getString("kits." + className + ".items.chestplateName");
name = ChatColor.translateAlternateColorCodes('&', name);
im.setDisplayName(name);
finalChestplate.setItemMeta(im);
}
if (this.getConfig().getString("kits." + className + ".items.leggingsName") != null) {
ItemMeta im = finalLeggings.getItemMeta();
String name = this.getConfig().getString("kits." + className + ".items.leggingsName");
name = ChatColor.translateAlternateColorCodes('&', name);
im.setDisplayName(name);
finalLeggings.setItemMeta(im);
}
if (this.getConfig().getString("kits." + className + ".items.bootsName") != null) {
ItemMeta im = finalBoots.getItemMeta();
String name = this.getConfig().getString("kits." + className + ".items.bootsName");
name = ChatColor.translateAlternateColorCodes('&', name);
im.setDisplayName(name);
finalBoots.setItemMeta(im);
}

if (s2 == -3) s2 = finalChestplate.getType().getMaxDurability();
if (s3 == -3) s3 = finalLeggings.getType().getMaxDurability();
if (s4 == -3) s4 = finalBoots.getType().getMaxDurability();

if (finalHelmet != null && s1 != -2 && s1 != -3) finalHelmet.setDurability(s1); logger.info("Setting durability to " + s1);
if (finalChestplate != null && s2 != -2 && s1 != -3) finalChestplate.setDurability(s2);
if (finalLeggings != null && s3 != -2 && s1 != -3) finalLeggings.setDurability(s3);
if (finalBoots != null && s4 != -2 && s1 != -3) finalBoots.setDurability(s4);




if (this.getConfig().contains("kits." + className + ".items.helmetEnchant") && finalHelmet != null) {
for (String a : this.getConfig().getString("kits." + className + ".items.helmetEnchant").split(" ")) {
String[] enchant = a.split(":");
Enchantment enchantmentInt = new EnchantmentWrapper(Integer.parseInt(enchant[0]));
int levelInt = Integer.parseInt(enchant[1]);
finalHelmet.addUnsafeEnchantment(enchantmentInt,levelInt);
}
}



if (this.getConfig().contains("kits." + className + ".items.chestplateEnchant") && finalChestplate != null) {
for (String a : this.getConfig().getString("kits." + className + ".items.chestplateEnchant").split(" ")) {
String[] enchant = a.split(":");
Enchantment enchantmentInt = new EnchantmentWrapper(Integer.parseInt(enchant[0]));
int levelInt = Integer.parseInt(enchant[1]);
finalChestplate.addUnsafeEnchantment(enchantmentInt,levelInt);
}
}

if (this.getConfig().contains("kits." + className + ".items.leggingsEnchant") && finalLeggings != null) {
for (String a : this.getConfig().getString("kits." + className + ".items.leggingsEnchant").split(" ")) {
String[] enchant = a.split(":");
Enchantment enchantmentInt = new EnchantmentWrapper(Integer.parseInt(enchant[0]));
int levelInt = Integer.parseInt(enchant[1]);
finalLeggings.addUnsafeEnchantment(enchantmentInt,levelInt);
}
}

if (this.getConfig().contains("kits." + className + ".items.bootsEnchant") && finalBoots != null) {
for (String a : this.getConfig().getString("kits." + className + ".items.bootsEnchant").split(" ")) {
String[] enchant = a.split(":");
Enchantment enchantmentInt = new EnchantmentWrapper(Integer.parseInt(enchant[0]));
int levelInt = Integer.parseInt(enchant[1]);
finalBoots.addUnsafeEnchantment(enchantmentInt,levelInt);
}
}

if (finalHelmet != null) { player.getInventory().setHelmet(finalHelmet); }
if (finalChestplate != null) { player.getInventory().setChestplate(finalChestplate); }
if (finalLeggings != null) { player.getInventory().setLeggings(finalLeggings); }
if (finalBoots != null) { player.getInventory().setBoots(finalBoots); }
player.updateInventory();

if (this.getConfig().contains(("kits." + className + ".commands"))) {
List<String> commands = this.getConfig().getStringList("kits." + className + ".commands");

for (String s : commands) {
s = s.replace("<player>", player.getName());
Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s);
}

}
//End of Armour
}

    //Method from http://wiki.bukkit.org/Configuration_API_Reference
    public void reloadCustomConfig() {
        if (customConfigFile == null) {
        customConfigFile = new File(getDataFolder(), "PlayerInventory.yml");
        }
        customConfig = YamlConfiguration.loadConfiguration(customConfigFile);
 
        // Look for defaults in the jar
        InputStream defConfigStream = this.getResource("PlayerInventory.yml");
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            customConfig.setDefaults(defConfig);
        }
    }
 
    //Method from http://wiki.bukkit.org/Configuration_API_Reference
    public FileConfiguration getCustomConfig() {
        if (customConfig == null) {
            this.reloadCustomConfig();
        }
        return customConfig;
    }
 
    //Method from http://wiki.bukkit.org/Configuration_API_Reference
    public void saveCustomConfig() {
        if (customConfig == null || customConfigFile == null) {
        return;
        }
        try {
            getCustomConfig().save(customConfigFile);
        } catch (IOException ex) {
            this.getLogger().log(Level.SEVERE, "Could not save config to " + customConfigFile, ex);
        }
    }


@Override
public void onDisable() {
getConfig().set("onDuty", this.onDuty);
getConfig().set("help", this.help);
saveConfig();
getLogger().info("Guards has been disabled");
saveCustomConfig();
}

}