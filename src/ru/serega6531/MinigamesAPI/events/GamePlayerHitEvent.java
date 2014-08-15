package ru.serega6531.MinigamesAPI.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import ru.serega6531.MinigamesAPI.player.GamePlayer;
import ru.serega6531.MinigamesAPI.session.GameSession;

/**
 * Raising when player hits another player in game session
 * @author serega6531
 */

public class GamePlayerHitEvent extends Event implements Cancellable {
	
	private GamePlayer damager, victim;
	private DamageCause cause;
	private GameSession session;
	private double damage;
	private boolean cancelled = false;
	
	public GamePlayerHitEvent(GamePlayer damager, GamePlayer victim, GameSession session, DamageCause cause, double damage) {
		this.damager = damager;
		this.victim = victim;
		this.session = session;
		this.cause = cause;
		this.damage = damage;
	}
	
	/**
	 * @return Damager
	 */
	
	public GamePlayer getDamager(){
		return damager;
	}
	
	/**
	 * @return Damager's victim
	 */
	
	public GamePlayer getVictim() {
		return victim;
	}
	
	/**
	 * @return Session where player hits another player
	 */
	
	public GameSession getSession(){
		return session;
	}
	
	/**
	 * @return Damage cause
	 */
	
	public DamageCause getCause(){
		return cause;
	}
	
	/**
	 * @return Damage
	 */
	
	public double getDamage(){
		return damage;
	}
	private final static HandlerList handlers = new HandlerList();
	 
	public HandlerList getHandlers() {
	    return handlers;
	}
	 
	public static HandlerList getHandlerList() {
	    return handlers;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		cancelled = cancel;
	}

}
