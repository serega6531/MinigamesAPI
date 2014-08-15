package ru.serega6531.MinigamesAPI.signs;

import org.bukkit.entity.Player;

/**
 * Interface to realize sign's clicks executors
 * @author serega6531
 */

public interface SignHandler {
		
	/**
	 * Sign's click handler
	 * @param player Clicked player
	 * @param command Sign command
	 * @param argument Command argument
	 */
	public void onClick(Player player, String command, String argument);

}
