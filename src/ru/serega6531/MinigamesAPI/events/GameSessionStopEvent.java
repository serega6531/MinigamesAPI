package ru.serega6531.MinigamesAPI.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import ru.serega6531.MinigamesAPI.session.GameSession;

/**
 * Raise when game session stopping before players removing
 * @author serega6531
 */

public class GameSessionStopEvent extends Event {

	private GameSession session;
	
	public GameSessionStopEvent(GameSession session){
		this.session = session;
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
