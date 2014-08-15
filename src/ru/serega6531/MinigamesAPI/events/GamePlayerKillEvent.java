package ru.serega6531.MinigamesAPI.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import ru.serega6531.MinigamesAPI.player.GamePlayer;
import ru.serega6531.MinigamesAPI.session.GameSession;

/**
 * Raise when player kills another player in game session
 * @author serega6531
 */

public class GamePlayerKillEvent extends Event {
	
	private GamePlayer killer, victim;
	private GameSession session;
	
	public GamePlayerKillEvent(GamePlayer killer, GamePlayer victim, GameSession session) {
		this.killer = killer;
		this.victim = victim;
	}
	
	/**
	 * @return Killer
	 */
	
	public GamePlayer getKiller(){
		return killer;
	}
	
	/**
	 * @return Killer's victim
	 */
	
	public GamePlayer getVictim() {
		return victim;
	}
	
	/**
	 * @return Session where kill is happened
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
