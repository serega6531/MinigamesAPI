package ru.serega6531.MinigamesAPI.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import ru.serega6531.MinigamesAPI.arena.GameArena;
import ru.serega6531.MinigamesAPI.player.GamePlayer;

/**
 * Raise when player trying to cross arena border.
 * @author serega6531
 */

public class GamePlayerCrossArenaBorderEvent extends Event {
	
	private GamePlayer player;
	private GameArena arena;
	
	public GamePlayerCrossArenaBorderEvent(GamePlayer player, GameArena arena){
		this.player = player;
		this.arena = arena;
	}
	
	/**
	 * @return Game player
	 */
	
	public GamePlayer getPlayer(){
		return player;
	}
	
	/**
	 * @return Arena
	 */
	
	public GameArena getSession(){
		return arena;
	}

	private static final HandlerList handlers = new HandlerList();
	 
	public HandlerList getHandlers() {
	    return handlers;
	}
	 
	public static HandlerList getHandlerList() {
	    return handlers;
	}

}
