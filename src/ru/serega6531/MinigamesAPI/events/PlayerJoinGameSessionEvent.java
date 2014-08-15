package ru.serega6531.MinigamesAPI.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import ru.serega6531.MinigamesAPI.player.GamePlayer;
import ru.serega6531.MinigamesAPI.session.GameSession;

/**
 * Raise when player trying to join game session. Can be cancelled.
 * @author serega6531
 */

public class PlayerJoinGameSessionEvent extends Event implements Cancellable {
	
	private GamePlayer player;
	private GameSession session;
	
	public PlayerJoinGameSessionEvent(GamePlayer player, GameSession session){
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
	private boolean cancelled = false;
	 
	public HandlerList getHandlers() {
	    return handlers;
	}
	 
	public static HandlerList getHandlerList() {
	    return handlers;
	}

    public boolean isCancelled() {
        return cancelled;
    }
 
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

}
