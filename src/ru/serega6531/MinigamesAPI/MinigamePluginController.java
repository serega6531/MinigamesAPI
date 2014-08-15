package ru.serega6531.MinigamesAPI;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import ru.serega6531.MinigamesAPI.arena.ArenaManager;
import ru.serega6531.MinigamesAPI.session.SessionManager;
import ru.serega6531.MinigamesAPI.signs.SignManager;

/**
 * Minigame controller.
 * Contains all managers.
 * @author serega6531
 */

public class MinigamePluginController {

	private Plugin plugin;
	private FileConfiguration config;
	private SessionManager sm;
	private ArenaManager am;
	private SignManager sim;
	
	/**
	 * Do not use directly.
	 */

	protected MinigamePluginController(Plugin plugin, FileConfiguration config){
		this.plugin = plugin;
		this.config = config;
		sm = new SessionManager();
		am = new ArenaManager();
		sim = new SignManager();
		
		am.loadArenas(config);
	}
	
	/**
	 * @return Main minigames class
	 */
	
	public Plugin getPlugin(){
		return plugin;
	}
	
	/**
	 * @return Sessions manager
	 */
	
	public SessionManager getSessionManager(){
		return sm;
	}
	
	/**
	 * @return Arenas manager
	 */
	
	public ArenaManager getArenaManager(){
		return am;
	}
	
	/**
	 * @return Signs manager
	 */
	
	public SignManager getSignManager(){
		return sim;
	}
	
	/**
	 * @return Plugin config
	 */

	public FileConfiguration getConfig() {
		return config;
	}
	
	/**
	 * @return Main MinigamesAPI class
	 * @see MinigamesAPI#getInstance()
	 */
	
	public MinigamesAPI getAPI(){
		return MinigamesAPI.getInstance();
	}

}
