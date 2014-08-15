package ru.serega6531.MinigamesAPI.arena;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import ru.serega6531.MinigamesAPI.MinigamesAPI;

/**
 * Game arena
 * @author serega6531
 */

public class GameArena {

	private Location fc, sc;
	private Location[] spawns;
	private String name;
	private boolean inuse = false;
	
	/**
	 * DO NOT USE DIRECTLY!
	 */
	
	protected GameArena(String name, Location fc, Location sc, Location[] spawns) {
		this.name = name;
		
		double tmp;
		
		if(fc.getX() < sc.getX()){
			tmp = fc.getX();
			fc.setX(sc.getX());
			sc.setX(tmp);
		}
		
		if(fc.getY() < sc.getY()){
			tmp = fc.getY();
			fc.setY(sc.getY());
			sc.setY(tmp);
		}
		
		if(fc.getZ() < sc.getZ()){
			tmp = fc.getZ();
			fc.setZ(sc.getZ());
			sc.setZ(tmp);
		}
		
		this.fc = fc;
		this.sc = sc;
		this.spawns = spawns;
	}
	
	/**
	 * Loading arena from config
	 * @param section {@link ConfigurationSection Section} with arena's data
	 * @return Класс арены
	 */
	
	protected static GameArena loadFromConfig(ConfigurationSection section){
		if(section == null) throw new NullPointerException("Section cannot be null");;
		MinigamesAPI.debug("Loading arena " + section.getString("name"));
		ArrayList<Location> spawns = new ArrayList<Location>();
		for(String num : section.getValues(false).keySet())
			if(num.startsWith("spawn")) spawns.add(deserializeLocation(section.getConfigurationSection(num)));
		
		return new GameArena(
				section.getString("name"),
				deserializeLocation(section.getConfigurationSection("fc")),
				deserializeLocation(section.getConfigurationSection("sc")),
				spawns.toArray(new Location[spawns.size()]));
	}
	
	/**
	 * @return Arena name
	 */
	
	public String getName(){
		return name;
	}
	
	/**
	 * @return Arenas corners (bigger first)
	 */
	
	public Location[] getCorners(){
		Location[] arr = {fc, sc};
		return arr;
	}
	
	/**
	 * @return Array of arena's spawns
	 */
	
	public Location[] getSpawns(){
		return spawns;
	}
	
	/**
	 * Saving arena to config
	 * @param fileConfiguration {@link FileConfiguration Config}
	 */
	
	public void saveArena(FileConfiguration fileConfiguration){
		if(fileConfiguration == null) throw new NullPointerException("Config cannot be null");
		MinigamesAPI.debug("Saving arena " + name);
		fileConfiguration.createSection("arenas." + name + ".fc", serializeLocation(fc));
		fileConfiguration.createSection("arenas." + name + ".sc", serializeLocation(sc));
		for(int i = 0; i < spawns.length; i++)
			fileConfiguration.createSection("arenas." + name + ".spawn" + i, serializeLocation(spawns[i]));
		fileConfiguration.set("arenas." + name + ".name", name);
	}
	
	/**
	 * Set arena using status
	 */
	
	public void use(){
		MinigamesAPI.debug("Start using arena " + name);
		inuse = true;
	}
	
	/**
	 * Set arena using status
	 */
	
	public void stopUse(){
		MinigamesAPI.debug("Stop using arena " + name);
		inuse = false;
	}
	
	/**
	 * @return Arena using status
	 */
	
	public boolean isInUse(){
		return inuse;
	}
	
	private Map<String, Object> serializeLocation(Location loc){
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("x", loc.getX());
		map.put("y", loc.getY());
		map.put("z", loc.getZ());
		map.put("w", loc.getWorld().getName());
		return map;
	}
	
	private static Location deserializeLocation(ConfigurationSection section){
		return new Location(
				Bukkit.getWorld(section.getString("w")),
				section.getDouble("x"),
				section.getDouble("y"),
				section.getDouble("z"));
	}
	
	public String toString(){
		return "GameArena[name=" + name + ",using="+ inuse + "]";
	}

}