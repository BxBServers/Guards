package com.bxbservers.Guards;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class KitPotion {

	private Guards plugin;
	
	public KitPotion(Guards instance){
		this.plugin = instance;
	}
	
	public void givePotionEffect (Player player, String name, int duration, int potency){

		switch (name.toLowerCase())
		{
		case "blindness":
			plugin.potion = PotionEffectType.BLINDNESS;
			break;
		case "confusion":
			plugin.potion = PotionEffectType.CONFUSION;
			break;
		case "damage_resistance":
			plugin.potion = PotionEffectType.DAMAGE_RESISTANCE;
			break;
		case "fast_digging":
			plugin.potion = PotionEffectType.FAST_DIGGING;
			break;
		case "fire_resistance":
			plugin.potion = PotionEffectType.FIRE_RESISTANCE;
			break;
		case "harm":
			plugin.potion = PotionEffectType.HARM;
			break;
		case "heal":
			plugin.potion = PotionEffectType.HEAL;
			break;
		case "hunger":
			plugin.potion = PotionEffectType.HUNGER;
			break;
		case "jump":
			plugin.potion = PotionEffectType.JUMP;
			break;
		case "poison":
			plugin.potion = PotionEffectType.POISON;
			break;
		case "regenration":
			plugin.potion = PotionEffectType.REGENERATION;
			break;
		case "slow":
			plugin.potion = PotionEffectType.SLOW;
			break;
		case "speed":
			plugin.potion = PotionEffectType.SPEED;
			break;
		case "increased_damage":
			plugin.potion = PotionEffectType.INCREASE_DAMAGE;
			break;
		case "water_breathing":
			plugin.potion = PotionEffectType.WATER_BREATHING;
			break;
		case "weakness":
			plugin.potion = PotionEffectType.WEAKNESS;
			break;
		case "wither":
			plugin.potion = PotionEffectType.WITHER;
			break;
		case "invisibility":
			plugin.potion = PotionEffectType.INVISIBILITY;
			break;
		case "night_vision":
			plugin.potion = PotionEffectType.NIGHT_VISION;
			break;
		default:
			plugin.potion = null;
			break;
		}

		if (plugin.potion != null) {
			player.addPotionEffect(new PotionEffect(plugin.potion,duration,potency));
		}
	}

	public void kitPotionEffect(Player player){
		List<String> listPotions;
		listPotions = plugin.getConfig().getStringList("kits.Guard.potions");
		//logger.info(listPotions.get(1));
		int n = listPotions.size() -1;
		int i;
		for(i=0; i<=n ; i++) {
			String data = listPotions.get(i);
			String[] potion =data.split(":");
			givePotionEffect(player, potion[0], Integer.MAX_VALUE, Integer.parseInt(potion[1]));
		}
	}
}
