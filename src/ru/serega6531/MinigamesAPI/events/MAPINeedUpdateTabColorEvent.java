package ru.serega6531.MinigamesAPI.events;

import org.bukkit.ChatColor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import ru.serega6531.MinigamesAPI.session.GameSession;

/**
 * The event raises when player color need to be updated
 * @author serega6531
 */

public class MAPINeedUpdateTabColorEvent extends Event {

	private GameSession session;
	private String tabHeader = null;
	private String tabTemplate = "!color!player";
	
	
	public MAPINeedUpdateTabColorEvent(GameSession session){
		this.session = session;
	}
	
	/**
	 * @return Game player
	 */
	
	public GameSession getSession(){
		return session;
	}
	
	/**
	 * @return Tab header
	 */

	public String getTabHeader() {
		return tabHeader;
	}
	
	/**
	 * @param tabHeader Tab header.
	 * Null, if you dont want it
	 */

	public void setTabHeader(String tabHeader) {
		this.tabHeader = tabHeader;
	}
	
	/**
	 * @return Tab template. Null, if not set.
	 */

	public String getTabTemplate() {
		return tabTemplate;
	}
	
	/**
	 * "!color" is team color mark,
	 * "!team" is a string color of team (e.g. GREEN),
	 * "!player" is a player name
	 * @param tabTemplate Tab template. Null, if you dont want to format it.
	 */

	public void setTabTemplate(String tabTemplate) throws NullPointerException {
		this.tabTemplate = tabTemplate;
	}
	
	public String formatTab(String p){
		if(tabTemplate == null) return p;
		ChatColor color = session.getPlayerTeam(p).getColor();
		return tabTemplate.replace("!color", color.toString()).replace("!team", color.name()).replace("!player", p);
	}

	private static final HandlerList handlers = new HandlerList();
	 
	public HandlerList getHandlers() {
	    return handlers;
	}
	 
	public static HandlerList getHandlerList() {
	    return handlers;
	}

}