package ru.serega6531.MinigamesAPI.session;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.mcsg.double0negative.tabapi.TabAPI;

import ru.serega6531.MinigamesAPI.MinigamesAPI;
import ru.serega6531.MinigamesAPI.arena.GameArena;
import ru.serega6531.MinigamesAPI.events.GameSessionCountDownTickEvent;
import ru.serega6531.MinigamesAPI.events.GameSessionNotEnoughtPlayersEvent;
import ru.serega6531.MinigamesAPI.events.GameSessionStartEvent;
import ru.serega6531.MinigamesAPI.events.GameSessionStopEvent;
import ru.serega6531.MinigamesAPI.events.GameSessionTickEvent;
import ru.serega6531.MinigamesAPI.events.PlayerJoinGameSessionEvent;
import ru.serega6531.MinigamesAPI.events.PlayerLeaveGameSessionEvent;
import ru.serega6531.MinigamesAPI.player.GamePlayer;
import ru.serega6531.MinigamesAPI.player.GameTeam;

/**
 * Game session class
 * @author serega6531
 */

public class GameSession {

	public int GAME_TIME, WAIT_TIME;
	// (in seconds)

	private ArrayList<GameTeam> teams = new ArrayList<GameTeam>();
	private GameArena arena;
	
	private SessionManager manager;
	
	private int taskid;
	private boolean isstarted = false;
	private int minplayers, maxplayers;
	
	/**
	 * Creating new session
	 * @param arena Arena to use
	 * @param manager {@link SessionManager Manager} for callback
	 */
	
	protected GameSession(GameArena arena, SessionManager manager){
		this.manager = manager;
		this.arena = arena;
		arena.use();
		GAME_TIME = 60 * 15;
		WAIT_TIME = 60;
		minplayers = 2;
		maxplayers = 16;
		startCountDown();
	}
	
	/**
	 * Set countdown
	 * @param wait Lobby wait time
	 * @param game Game time
	 */
	
	public void setTime(int wait, int game){
		WAIT_TIME = wait;
		GAME_TIME = game;
		Bukkit.getScheduler().cancelTask(taskid);
		if(isstarted) taskid = Bukkit.getScheduler().scheduleSyncRepeatingTask(MinigamesAPI.getInstance(), new GameRunnable(this),      0L, 20L);
		else          taskid = Bukkit.getScheduler().scheduleSyncRepeatingTask(MinigamesAPI.getInstance(), new CountDownRunnable(this), 0L, 20L);
	}
	
	/**
	 * Set players limit
	 * @param min Min
	 * @param max Max
	 */
	
	public void setPlayersLimit(int min, int max){
		minplayers = min;
		maxplayers = max;
	}
	
	/**
	 * Returns players limit
	 * Result[0] = min;
	 * Result[1] = max;
	 * @return Players limit
	 */
	
	public int[] getPlayersLimit(){
		return new int[]{minplayers, maxplayers};
	}
	
	/**
	 * @return Arena
	 */
	
	public GameArena getArena(){
		return arena;
	}
	
	/**
	 * @return Is game started
	 */
	
	public boolean isStarted(){
		return isstarted;
	}
	
	/**
	 * @return Player's in session count
	 */
	
	public int getPlayersCount(){
		int count = 0;
		for(GameTeam team : getTeams()) count += team.getSize();
		return count;
	}
	
	/**
	 * @return Array of teams
	 */
	
	public GameTeam[] getTeams() {
		return teams.toArray(new GameTeam[teams.size()]);
	}
	
	public GameTeam getTeamByColor(ChatColor color){
		for(GameTeam team : teams)
			if(team.getColor() == color) return team;
		return null;
	}
	
	/**
	 * Adding new player to session.
	 * Will not add if it was cancelled by {@link PlayerJoinGameSessionEvent event} or session already contains 16 players. 
	 * Check using {@link GameSession#getPlayersCount() getPlayersCount()}. 
	 * Will add to smaller team.
	 * @param player Game player
	 * @return Was player added
	 */
	
	public boolean joinPlayer(GamePlayer player) throws NullPointerException, IllegalStateException {
		if(player == null) throw new NullPointerException("Player cannot be null");
		if(!player.isOnline()) throw new IllegalStateException("Player " + player.getName() + " not online");
		GameTeam tojoin;
		if(teams.size() == 0){
			GameTeam team = new GameTeam();
			addTeam(team);
			tojoin = team;
		} else {
			tojoin = teams.get(0);
			int min = tojoin.getSize();
			for(GameTeam team : getTeams())
				if(team.getSize() < min) tojoin = team;
		}
		return joinPlayer(player, tojoin);
	}
	
	/**
	 * Adding new player to session.
	 * Will not add if it was cancelled by {@link PlayerJoinGameSessionEvent event} or session already contains 16 players. 
	 * Check using {@link GameSession#getPlayersCount() getPlayersCount()}. 
	 * @param player Game player
	 * @param team Team to join
	 * @return Was player added
	 */
	
	@SuppressWarnings("deprecation")
	public boolean joinPlayer(GamePlayer player, GameTeam team) throws NullPointerException {
		if(player == null) throw new NullPointerException("Player cannot be null");
		if(team == null) throw new NullPointerException("Team cannot be null");
		if(getPlayersCount() == maxplayers){
			MinigamesAPI.debug(player.getName() + " can't join: too much players");
			return false;
		}
		PlayerJoinGameSessionEvent event = new PlayerJoinGameSessionEvent(player, this);
		Bukkit.getPluginManager().callEvent(event);
		if(event.isCancelled()) {
			MinigamesAPI.debug(player.getName() + " can't join: cancelled by event");
			return false;
		}
		
		GameSession last = MinigamesAPI.getInstance().getPlayerSession(player.getName());
		if(last != null){
			last.removePlayer(player);
		}
		team.addPlayer(player);
		rebuildTab();
		MinigamesAPI.debug(player.getName() + " joined to session");
		return true;
	}
	
	/**
	 * Removing player from session.
	 * Raising {@link PlayerLeaveGameSessionEvent event}.
	 * @param player Player
	 */
	
	@SuppressWarnings("deprecation")
	public void removePlayer(GamePlayer player) throws NullPointerException {
		if(player == null) throw new NullPointerException("Player cannot be null");
		
		removePlayer0(player);
		for(GameTeam team : getTeams()) team.removePlayer(player);
	}
	
	private void removePlayer0(GamePlayer player){
		MinigamesAPI.debug("Removing player " + player.getName());
		Bukkit.getPluginManager().callEvent(new PlayerLeaveGameSessionEvent(player, this));
		if(player.isOnline()){
			player.getEntity().setExp(0);
			player.getEntity().setLevel(0);
			try {
				TabAPI.clearTab(player.getEntity());
			} catch(NullPointerException e){}
		}
		rebuildTab();
	}
	
	/**
	 * @param name Player name
	 * @return Is player in this session
	 */

	public boolean isPlayerInSession(String name){
		if(name == null) return false;
		for(GameTeam team : getTeams())
			if(team.containsPlayer(name)) return true;
		return false;
	}
	
	/**
	 * @param name Player name
	 * @return Player's team
	 */
	
	public GameTeam getPlayerTeam(String name){
		if(name == null) return null;
		for(GameTeam team : getTeams())
			if(team.containsPlayer(name)) return team;
		return null;
	}
	
	/**
	 * Sending message to all session members
	 * @param message Message
	 */
	
	public void sendBroadcastMessage(String message){
		for(GameTeam team : getTeams())
			for(GamePlayer player : team.getPlayers())
				player.getEntity().sendMessage(message);
	}
	
	/**
	 * Starting countdown to game start
	 */
	
	private void startCountDown(){
		MinigamesAPI.debug("Starting countdown");
		taskid = Bukkit.getScheduler().scheduleSyncRepeatingTask(MinigamesAPI.getInstance(), new CountDownRunnable(this), 20L, 20L);
	}
	
	/**
	 * Add player to session
	 * @param team Команда
	 */
	
	public void addTeam(GameTeam team) throws NullPointerException {
		if(team == null) throw new NullPointerException("Team cannot be null");
		teams.add(team);
		rebuildTab();
	}
	
	/**
	 * Starting game. 
	 * Stopping countdown.
	 * Teleport teams to spawn.
	 * Raising {@link GameSessionStartEvent event}.
	 */
	
	public void startGame(){
		if(getPlayersCount() < minplayers) {
			MinigamesAPI.debug("Game cannot be started: not enought players");
			return;
		}
		MinigamesAPI.debug("Game started");
		isstarted = true;
		List<Location> lspawns = new ArrayList<Location>(Arrays.asList(arena.getSpawns()));
		List<Location> tspawns = new ArrayList<Location>(lspawns);
		
		for(GameTeam team : getTeams()){
			team.updateColor();
			
			if(tspawns.size() == 0) tspawns.addAll(lspawns);
			int rand = new Random().nextInt(tspawns.size());
			team.teleport(tspawns.get(rand));
			tspawns.remove(rand);
		}
		Bukkit.getScheduler().cancelTask(taskid);
		taskid = Bukkit.getScheduler().scheduleSyncRepeatingTask(MinigamesAPI.getInstance(), new GameRunnable(this), 0L, 20L);
		Bukkit.getPluginManager().callEvent(new GameSessionStartEvent(this));
	}
	
	/**
	 * Stopping game. Raising {@link GameSessionStopEvent event}.
	 */
	
	public void stopGame(){
		MinigamesAPI.debug("Stopping game");
		isstarted = false;
		Bukkit.getScheduler().cancelTask(taskid);
		Bukkit.getPluginManager().callEvent(new GameSessionStopEvent(this));
		
		Iterator<GamePlayer> iterator;
		for(GameTeam team : getTeams()){
			iterator = team.getPlayers().iterator();
			while(iterator.hasNext()){
				removePlayer0(iterator.next());
				iterator.remove();
			}
		}
		arena.stopUse();
		manager.removeSession(this);
	}
	
	public String toString(){
		return "GameSession[teams=" + teams.toString() + ",arena=" + arena.toString() +"]";
	}
	
	private void rebuildTab(){
		MinigamesAPI.debug("Rebuilding tab");
		
		List<String> tab = new ArrayList<String>();
		
		for(GameTeam team : getTeams())
			for(String player : team.getPlayersNames())
				tab.add(player);
		tab.sort(new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}
		});
		
		for(GameTeam team : getTeams()){
			
			int vert, hor;
			
			for(GamePlayer player : team.getPlayers()){
				Player p = player.getEntity();
				if(p == null) continue;
				try {
					TabAPI.clearTab(p);
				} catch(NullPointerException e){
					continue;
				}
				hor = 0;
				vert = 1;
				TabAPI.setTabString(MinigamesAPI.getInstance(), p, 0, 0, ChatColor.BLUE + "Players list");
				for(String nick : tab){
					if(vert == TabAPI.getVertSize()){
						if(hor + 1 == TabAPI.getHorizSize()) continue;
						vert = 0;
						hor++;
					}
					TabAPI.setTabString(MinigamesAPI.getInstance(), p, vert, hor, nick);
					vert++;
				}
				TabAPI.updatePlayer(p);
			}
		}
	}
	
	/**
	 * Countdown to game start class
	 * @author serega6531
	 */

	class CountDownRunnable implements Runnable {
		
		private GameSession session;
		
		public CountDownRunnable(GameSession session){
			this.session = session;
			countdown = session.WAIT_TIME;
		}

		int countdown;
		
		public void run() {
			if(countdown == 0){
				Bukkit.getPluginManager().callEvent(new GameSessionCountDownTickEvent(session, session.WAIT_TIME));
				if(session.getPlayersCount() >= minplayers){
					session.startGame();
				} else {
					countdown = session.WAIT_TIME;
					Bukkit.getPluginManager().callEvent(new GameSessionNotEnoughtPlayersEvent(session));
				}
				return;
			}
			for(GameTeam team : session.getTeams())
				for(GamePlayer player : team.getPlayers()){
					if(!player.isOnline()){
						removePlayer(player);
						return;
					}
				}
			Bukkit.getPluginManager().callEvent(new GameSessionCountDownTickEvent(session, session.WAIT_TIME - countdown));
			countdown--;
		}
		
	}
	
	/**
	 * Countdown to game end class
	 * @author serega6531
	 */

	class GameRunnable implements Runnable {
		
	private GameSession session;
		
		public GameRunnable(GameSession session){
			this.session = session;
			countdown = session.GAME_TIME;
		}

		int countdown;
		
		public void run() {
			if(countdown == 0){
				Bukkit.getPluginManager().callEvent(new GameSessionTickEvent(session, session.GAME_TIME));
				session.stopGame();
				return;
			}
			countdown--;
			for(GameTeam team : session.getTeams())
				for(GamePlayer player : team.getPlayers()){
					if(!player.isOnline()){
						removePlayer(player);
						return;
					}
				}
			Bukkit.getPluginManager().callEvent(new GameSessionTickEvent(session, session.GAME_TIME - countdown));
		}
		
	}

}