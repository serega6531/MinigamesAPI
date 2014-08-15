package ru.serega6531.MinigamesAPI.session;

import java.util.ArrayList;

import ru.serega6531.MinigamesAPI.MinigamesAPI;
import ru.serega6531.MinigamesAPI.arena.GameArena;

/**
 * Sessions manager
 * @author serega6531
 */

public class SessionManager {

	private ArrayList<GameSession> sessions = new ArrayList<GameSession>();
	
	/**
	 * Creating new session
	 * @param arena {@link GameArena Arena} for game
	 * @return Game session
	 */
	
	public GameSession createSession(GameArena arena) throws NullPointerException, IllegalStateException {
		if(arena == null) throw new NullPointerException("Arena cannot be null");
		if(arena.isInUse()) throw new IllegalStateException("Arena " + arena.getName() + " already in use!");
		MinigamesAPI.debug("Creating new session");
		GameSession session = new GameSession(arena, this);
		sessions.add(session);
		return session;
	}
	
	/**
	 * Removing the session. Do not use!
	 * @param session Session
	 * @see GameSession#stopGame()
	 */
	
	protected void removeSession(GameSession session){
		MinigamesAPI.debug("Removing session");
		sessions.remove(session);
	}
	
	/**
	 * Returns plauer session or null if not exists
	 * @param name Player name
	 * @return Game session
	 */
	
	public GameSession getPlayerSesion(String name){
		if(name == null) return null;
		for(GameSession session : sessions)
			if(session.isPlayerInSession(name)) return session; 
		return null;
	}
	
	/**
	 * @return All minigames's ingame sessions
	 */
	
	public GameSession[] getAllSessions(){
		return sessions.toArray(new GameSession[sessions.size()]);
	}

}
