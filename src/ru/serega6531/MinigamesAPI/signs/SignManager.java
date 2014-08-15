package ru.serega6531.MinigamesAPI.signs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.plugin.Plugin;

import ru.serega6531.MinigamesAPI.MinigamesAPI;

/**
 * Signs manager
 * @author serega6531
 */

public class SignManager {
	
	private static HashMap<Location, GameSign> signs = new HashMap<Location, GameSign>();
	
	/**
	 * Do not use this or the sky will fall on your head.
	 * Seriously, just to MAPI's inside uses.
	 */
	
	public static void runSignUpdater(){
		Bukkit.getScheduler().scheduleSyncRepeatingTask(MinigamesAPI.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				for(Map.Entry<Location, GameSign> entry : signs.entrySet()){
					if(entry.getValue().getText().equals("")) continue;
					Block block = entry.getKey().getBlock();
					if(block == null) continue;
					BlockState state = block.getState();
					if(state instanceof Sign){
						Sign sign = (Sign) state;
						sign.setLine(0, entry.getValue().getText());
						sign.setLine(1, "");
						sign.setLine(2, "");
						sign.setLine(3, "");
						sign.update();
					}
				}
			}
		}, 20L, 20L);
	}
	
	/**
	 * Registering new sign
	 * @param loc Sign's location
	 * @param sign Game sign
	 * @deprecated New method
	 * @see SignManager#registerSign(GameSign)
	 */

	public void registerSign(Location loc, GameSign sign){
		if(!signs.containsKey(loc)){
			MinigamesAPI.debug("Registering new sign");
			signs.put(loc, sign);
		}
	}
	
	/**
	 * Registering new sign
	 * @param sign Game sign
	 */
	
	public void registerSign(GameSign sign){
		if(!signs.containsKey(sign.getLocation())){
			MinigamesAPI.debug("Registering new sign");
			signs.put(sign.getLocation(), sign);
		}
	}
	
	/**
	 * Removing sign from database
	 * @param loc Sing's location
	 */
	
	public static void unregisterSign(Location loc){
		MinigamesAPI.debug("Removing sign");
		signs.remove(loc);
	}
	
	/**
	 * @param loc Sing's location
	 * @return Does sign at this location exists
	 */
	
	public static boolean containsSign(Location loc){
		return signs.containsKey(loc);
	}
	
	/**
	 * @param loc Sing's location
	 * @return Game sign (or null if not exists)
	 */
	
	public static GameSign getSign(Location loc){
		return signs.get(loc);
	}
	
	/**
	 * Returns array of plugin's signs to save
	 * @param plugin Minigame main class
	 * @return Array of game signs
	 */
	
	public GameSign[] getSignsToSave(Plugin plugin){
		ArrayList<GameSign> esigns = new ArrayList<GameSign>();
		for(GameSign sign : signs.values()){
			if(sign.getSignPlugin().equals(plugin)) esigns.add(sign);
		}
		return esigns.toArray(new GameSign[esigns.size()]);
	}

}
