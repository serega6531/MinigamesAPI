package ru.serega6531.MinigamesAPI.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import ru.serega6531.MinigamesAPI.signs.GameSign;

/**
 * Raise at game sign destroying
 * @author serega6531
 */

public class GameSignDestroyEvent extends Event implements Cancellable {

    private GameSign sign;
    private boolean cancelled = false;
   
    public GameSignDestroyEvent(GameSign sign){
        this.sign = sign;
    }
    
    /**
     * @return Sign
     */
   
    public GameSign getSign(){
        return sign;
    }

    private static final HandlerList handlers = new HandlerList();
     
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