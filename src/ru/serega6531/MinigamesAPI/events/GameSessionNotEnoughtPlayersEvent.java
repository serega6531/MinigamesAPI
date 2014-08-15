package ru.serega6531.MinigamesAPI.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import ru.serega6531.MinigamesAPI.session.GameSession;

/**
 * Raise when game session starting
 * @author serega6531
 */

public class GameSessionNotEnoughtPlayersEvent extends Event {

	private GameSession session;
	
	public GameSessionNotEnoughtPlayersEvent(GameSession session){
		this.session = session;
	}
	
	/**
	 * @return Game session
	 */
	
	public GameSession getSession(){
		return session;
	}

	private final static HandlerList handlers = new HandlerList();
	 
	public HandlerList getHandlers() {
	    return handlers;
	}
	 
	public static HandlerList getHandlerList() {
	    return handlers;
	}
    
}