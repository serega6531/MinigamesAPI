package ru.serega6531.MinigamesAPI.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import ru.serega6531.MinigamesAPI.player.GamePlayer;
import ru.serega6531.MinigamesAPI.session.GameSession;

/**
 * Raise when player leaving game session.
 * @author serega6531
 */

public class PlayerLeaveGameSessionEvent extends Event {
	
	private GamePlayer player;
	private GameSession session;
	
	public PlayerLeaveGameSessionEvent(GamePlayer player, GameSession session){
		this.player = player;
		this.session = session;
	}
	
	/**
	 * @return Game player
	 */
	
	public GamePlayer getPlayer(){
		return player;
	}
	
	/**
	 * @return Game session
	 */
	
	public GameSession getSession(){
		return session;
	}

	private static final HandlerList handlers = new HandlerList();
	 
	public HandlerList getHandlers() {
	    return handlers;
	}
	 
	public static HandlerList getHandlerList() {
	    return handlers;
	}
    
}
