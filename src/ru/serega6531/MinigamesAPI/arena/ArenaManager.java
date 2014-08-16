package ru.serega6531.MinigamesAPI.arena;

import java.util.HashMap;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import ru.serega6531.MinigamesAPI.MinigamesAPI;

/**
 * Arenas manager
 * @author serega6531
 */

public class ArenaManager {

	HashMap<String, GameArena> arenas = new HashMap<String, GameArena>();
	
	/**
	 * Creating new arena.
	 * The corners of arena are changing to the smallest and biggest automatically.
	 * @param name Arena name
	 * @param fc First corner
	 * @param sc Secons corner
	 * @param spawns Array of spawns
	 * @return New arena
	 */
	
	public GameArena createArena(String name, Location fc, Location sc, Location[] spawns) throws NullPointerException {
		if(name == null || name.isEmpty()) throw new NullPointerException("Arena name cannot be empty");
		if(fc == null || sc == null) throw new NullPointerException("Corners cannot be null");
		if(spawns == null) throw new NullPointerException("Spawns cannot be null");
		for(Location loc : spawns){
			if(loc == null) throw new NullPointerException("Spawn cannot be null");
		}
		if(hasArena(name)) {
			MinigamesAPI.debug("Found existing arena " + name);
			return getArena(name);
		}
		MinigamesAPI.debug("Creating new arena " + name);
		GameArena arena = new GameArena(name, fc, sc, spawns);
		arenas.put(name, arena);
		return arena;
	}
	
	/**
	 * Removing arena
	 * @param name Arena name
	 * @param config Plugin config
	 */
	
	public void removeArena(String name, FileConfiguration config){
		config.set("arenas." + name, null);
		arenas.remove(name);
	}
	
	/**
	 * @param name Arena name
	 * @return Does arena with this name exists
	 */
	
	public boolean hasArena(String name){
		return arenas.containsKey(name);
	}
	
	/**
	 * Get all arenas names
	 * @return Array of arenas names
	 * @see ArenaManager#getArena(String)
	 */
	
	public String[] getArenas(){
		Set<String> keys = arenas.keySet();
		return keys.toArray(new String[keys.size()]);
	}
	
	/**
	 * Load arena from config
	 * @param config {@link ConfigurationSection Section} with arena's data
	 * @return Arena
	 */
	
	public GameArena loadArena(ConfigurationSection config){
		GameArena arena = GameArena.loadFromConfig(config);
		arenas.put(arena.getName(), arena);
		return arena;
	}
	
	/**
	 * Get arena by name
	 * @param name Arena name
	 * @return Arena
	 */

	public GameArena getArena(String name){
		return arenas.get(name);
	}
	
	/**
	 * Saving arenas to config
	 * @param fileConfiguration {@link FileConfiguration Config}
	 */
	
	public void saveArenas(FileConfiguration fileConfiguration) throws NullPointerException {
		if(fileConfiguration == null) throw new NullPointerException("Config cannot be null");
		for(GameArena arena : arenas.values())
			arena.saveArena(fileConfiguration);
	}
	
	/**
	 * Loading all arenas from config
	 * @param fileConfiguration {@link FileConfiguration Config}
	 */
	
	public void loadArenas(FileConfiguration fileConfiguration) throws NullPointerException {
		if(fileConfiguration == null) throw new NullPointerException("Config cannot be null");
		if(!fileConfiguration.contains("arenas")) return;
		ConfigurationSection arenas = fileConfiguration.getConfigurationSection("arenas");
		for(String name : arenas.getValues(false).keySet())
			loadArena(arenas.getConfigurationSection(name));
	}

}
