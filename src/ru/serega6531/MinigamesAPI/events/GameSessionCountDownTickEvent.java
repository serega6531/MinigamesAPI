package ru.serega6531.MinigamesAPI.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import ru.serega6531.MinigamesAPI.session.GameSession;

/**
 * Raise every second in countdown
 * @author serega6531
 */

public class GameSessionCountDownTickEvent extends Event {

	private GameSession session;
	private int time;
	
	public GameSessionCountDownTickEvent(GameSession session, int time){
		this.session = session;
		this.time = time;
	}
	
	/**
	 * @return Game session
	 */
	
	public GameSession getSession(){
		return session;
	}
	
	/**
	 * @return Time until countdown start
	 */
	
	public int getTime(){
		return time;
	}

	private static final HandlerList handlers = new HandlerList();
	 
	public HandlerList getHandlers() {
	    return handlers;
	}
	 
	public static HandlerList getHandlerList() {
	    return handlers;
	}
    
}
