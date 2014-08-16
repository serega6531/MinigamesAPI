package ru.serega6531.MinigamesAPI.player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.kitteh.tag.TagAPI;

import ru.serega6531.MinigamesAPI.MinigamesAPI;
import ru.serega6531.MinigamesAPI.session.GameSession;

/**
 * Team of players. In one-against-all game create new team for each player.
 * @author serega6531
 */

public class GameTeam {
	
	private List<GamePlayer> players = new ArrayList<GamePlayer>();
	private ChatColor color;
	
	/**
	 * Creating team with first player
	 * @param player Player
	 */
	
	public GameTeam(GamePlayer player){
		this();
		if(player == null) throw new NullPointerException("Player cannot be null");
		this.players.add(player);
	}
	
	/**
	 * Creating team with specified color
	 * @param color Team color
	 */
	
	public GameTeam(ChatColor color){
		if(color == null) throw new NullPointerException("Color cannot be null");
		this.color = color;
	}
	
	/**
	 * Creating command with random color
	 * Warning! Color can be same with other team color! I recommend to use your colors.
	 * @see GameTeam(ChatColor)
	 */

	public GameTeam() {
		this(ChatColor.values()[new Random().nextInt(15)]);
	}
	
	/**
	 * @return Team members list
	 * @see GameTeam#getPlayersNames()
	 */

	public List<GamePlayer> getPlayers() {
		return players;
	}
	
	/**
	 * @return Tean members names list
	 * @see GameTeam#getPlayers()
	 */
	
	public List<String> getPlayersNames(){
		List<String> list = new ArrayList<String>();
		
		for(GamePlayer player : players)
			list.add(player.getName());
			
		return list;
	}
	
	/**
	 * @return Tean members colored names list
	 * @see GameTeam#getPlayers()
	 */
	
	public List<String> getPlayersColoredNames(){
		List<String> list = new ArrayList<String>();
		
		for(GamePlayer player : players)
			list.add(color + player.getName());
			
		return list;
	}
	
	/**
	 * Add new player to command
	 * @param player Player
	 * @deprecated Do not use! Use {@link GameSession#joinPlayer(GamePlayer, GameTeam)} instead!
	 */

	public void addPlayer(GamePlayer player) {
		players.add(player);
	}
	
	/**
	 * Removing player from team
	 * @param player Player
	 * @deprecated Do not use! Use {@link GameSession#removePlayer(GamePlayer)} instead!
	 */
	
	public void removePlayer(GamePlayer player){
		players.remove(player);
	}
	
	/**
	 * @param player Player
	 * @return Is player in this team
	 * @see GameTeam#containsPlayer(String)
	 */
	
	public boolean containsPlayer(GamePlayer player){
		return players.contains(player);
	}
	
	/**
	 * @param playername Player name
	 * @return Is player with that nick in this team
	 * @see GameTeam#containsPlayer(GamePlayer)
	 */
	
	public boolean containsPlayer(String playername){
		if(playername == null) return false;
		for(GamePlayer player : players)
			if(player.getName().equalsIgnoreCase(playername)) return true;
		return false;
	}
	
	/**
	 * @return Team members count
	 */
	
	public int getSize(){
		return players.size();
	}
	
	/**
	 * @return Team color
	 */
	
	public ChatColor getColor(){
		return color;
	}
	
	/**
	 * Updating team color in player's chat and nametag.
	 */
	
	public void updateColor(){
		MinigamesAPI.debug("Updating team color (" + color.name() + ")");
		for(GamePlayer player : players){
			player.getEntity().setDisplayName(color + player.getEntity().getName() + ChatColor.RESET);
			TagAPI.refreshPlayer(player.getEntity());
		}
	}
	
	/**
	 * Teleporting all members in one point
	 * @param loc Teleportation point
	 */
	
	public void teleport(Location loc){
		for(GamePlayer player : players)
			player.getEntity().teleport(loc);
	}
	
	public String toString(){
		return "GameTeam[players=" + players.toString() + ", color=" + color.name() + "]";
	}

}
