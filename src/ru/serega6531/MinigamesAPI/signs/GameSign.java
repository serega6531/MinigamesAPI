package ru.serega6531.MinigamesAPI.signs;

import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.plugin.Plugin;

/**
 * Game sign class
 * @author serega6531
 */

public class GameSign {
	
	private SignHandler handler;
	private String command, arg;
	private String text;
	private Plugin signplugin;
	private Location loc;
	
	/**
	 * Creating game sign
	 * @param handler Sign handler
	 * @param command Sign command
	 * @param arg Command argument
	 * @param text Text to replace. If it empty, sign will not change.
	 * @param loc Sign location
	 * @param signplugin Sign's minigame
	 */
	
	public GameSign(SignHandler handler, String command, String arg, String text, Location loc, Plugin signplugin){
		this.handler = handler;
		this.command = command;
		this.arg = arg;
		this.text = text;
		this.signplugin = signplugin;
		this.loc = loc;
	}
	
	/**
	 * Creating game sign. Getting text data from table rows.
	 * @param handler Sign handler
	 * @param sign {@link Sign Sign}
	 * @param loc Sign's location
	 * @param signplugin Sign's minigame
	 */
	
	public GameSign(SignHandler handler, Sign sign, Location loc, Plugin signplugin){
		this(handler, sign.getLine(1), sign.getLine(2), sign.getLine(3), loc, signplugin);
	}
	
	/**
	 * @return Click handler
	 */
	
	public SignHandler getHandler() {
		return handler;
	}
	
	/**
	 * @return Sing command
	 */

	public String getCommand() {
		return command;
	}
	
	/**
	 * @return Command argument
	 */

	public String getArgument() {
		return arg;
	}
	
	/**
	 * @return Text to replace.
	 */
	
	public String getText(){
		return text;
	}
	
	/**
	 * Set text to replace. Empty string stop replacing sign.
	 * @param text Text to replace
	 */
	
	public void setText(String text){
		this.text = text;
	}
	
	/**
	 * @return Sign's location
	 */
	
	public Location getLocation(){
		return loc;
	}
	
	/**
	 * @return Sign's minigame
	 */
	
	public Plugin getSignPlugin(){
		return signplugin;
	}

}
