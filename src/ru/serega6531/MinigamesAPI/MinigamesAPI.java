package ru.serega6531.MinigamesAPI;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;

import ru.serega6531.MinigamesAPI.player.GamePlayer;
import ru.serega6531.MinigamesAPI.session.GameSession;
import ru.serega6531.MinigamesAPI.signs.SignManager;

/**
 * Main MinigamesAPI class
 * @author serega6531
 */

public class MinigamesAPI extends JavaPlugin {
	
	private ArrayList<MinigamePluginController> minigames = new ArrayList<MinigamePluginController>();
	private HashMap<String, GamePlayer> players = new HashMap<String, GamePlayer>();
	private Metrics metrics;
	
	public Connection db;
	
	private static MinigamesAPI singletone;
	private static boolean debug = false;
	
	public MinigamesAPI(){
		singletone = this;
	}

	public void onEnable(){
		try {
		    metrics = new Metrics(this);
		    metrics.start();
		} catch (IOException e) {
		    getLogger().warning("Can't load Metrics: " + e.getMessage());
		    metrics = null;
		}
		debug = getConfig().getBoolean("debug-mode", false);
		try {
			Class.forName("org.sqlite.JDBC");
			getDataFolder().mkdir();
			db = DriverManager.getConnection("jdbc:sqlite:" + getDataFolder().getAbsolutePath() + File.separator + "database.db");
			db.createStatement().execute("CREATE TABLE IF NOT EXISTS money (user TEXT PRIMARY KEY UNIQUE NOT NULL, money INTEGER NOT NULL)");
		} catch (ClassNotFoundException | SQLException e) {
			getLogger().warning("Cant open database: " + e.getMessage());
			Bukkit.getPluginManager().disablePlugin(this);
		}
		Bukkit.getPluginManager().registerEvents(new EventListener(), this);
		SignManager.runSignUpdater();
	}
	
	public void onDisable(){
		for(MinigamePluginController controller : minigames){
			controller.getArenaManager().saveArenas(controller.getConfig());
			controller.getPlugin().saveConfig();
			for(GameSession session : controller.getSessionManager().getAllSessions()) session.stopGame();
		}
		for(GamePlayer player : players.values())
			player.pushMoney();
		Bukkit.getScheduler().cancelTasks(this);
	}
	
	/**
	 * Registering minigames
	 * @param plugin Main minigame class
	 * @param config Plugin's config
	 * @return Minigame controller
	 */
	
	public MinigamePluginController registerMinigame(Plugin plugin, FileConfiguration config) throws NullPointerException {
		if(plugin == null || config == null) throw new NullPointerException();
		debug("Registering minigame " + plugin.getName());
		MinigamePluginController controller = new MinigamePluginController(plugin, config);
		minigames.add(controller);
		return controller;
	}
	
	/**
	 * Returning minigame controller if it registered
	 * @param plugin Main minigame class
	 * @return Minigame controller, if it created, or null
	 */

	public MinigamePluginController getPluginController(Plugin plugin){
		if(plugin == null) throw new NullPointerException("Plugin cannot be null");
		for(MinigamePluginController controller : minigames)
			if(controller.getPlugin().getName().equals(plugin.getName())) return controller;
		return null;
	}
	
	/**
	 * Return array of all minigames controller
	 * @return Array of controllers
	 */
	
	public MinigamePluginController[] getAllControllers(){
		return minigames.toArray(new MinigamePluginController[minigames.size()]);
	}
	
	/**
	 * Return player session
	 * @param player Player name
	 * @return Session, if it exists, or null
	 */
	
	public GameSession getPlayerSession(String player){
		for(MinigamePluginController controller : getAllControllers()){
			GameSession session = controller.getSessionManager().getPlayerSesion(player);
			if(session != null){
				return session;
			}
		}
		return null;
	}
	
	/**
	 * Return player status
	 * @param player Player name
	 */
	
	public boolean isPlayerInSession(String player){
		return getPlayerSession(player) != null;
	}
	
	/**
	 * Return game player class
	 * @param player Player name
	 */
	
	public GamePlayer getGamePlayer(String player){
		if(players.containsKey(player)) {
			debug("Player " + player + " found");
			return players.get(player);
		}
		debug("Creating new player " + player);
		GamePlayer gplayer = new GamePlayer(player);
		players.put(player, gplayer);
		return gplayer;
	}
	
	/**
	 * Return MinigamesAPI {@link MinigamesAPI#singletone class}
	 */
	
	public static MinigamesAPI getInstance(){
		return singletone;
	}
	
	/**
	 * Show debug message
	 */
	
	public static void debug(String message){
		if(debug) singletone.getLogger().info("[DEBUG] " + message);
	}

}
