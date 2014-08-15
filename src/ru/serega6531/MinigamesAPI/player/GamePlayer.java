package ru.serega6531.MinigamesAPI.player;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import ru.serega6531.MinigamesAPI.MinigamesAPI;

/**
 * Player class
 * @author serega6531
 */

public class GamePlayer {

	private String name;
	private int money;
	
	/**
	 * Creating new player
	 * @param name Ник игрока
	 */
	
	public GamePlayer(String name) throws NullPointerException {
		if(name == null) throw new NullPointerException("Name cannot be null");
		this.name = name;
		this.money = 0;
		pullMoney();
	}
	
	/**
	 * @return Player nick
	 */

	public String getName() {
		return name;
	}
	
	/**
	 * @return Bukkit {@link Player player}
	 */
	
	@SuppressWarnings("deprecation")
	public Player getEntity(){
		return Bukkit.getPlayerExact(name);
	}
	
	/**
	 * @return Is player online
	 */
	
	public boolean isOnline(){
		return getEntity() != null;
	}
	
	/**
	 * Add money to player
	 * @param add Money to add
	 */
	
	public void addMoney(int add){
		money += add;
	}
	
	/**
	 * Remove money from player
	 * @param remove Money to remove
	 */
	
	public boolean removeMoney(int remove){
		if(hasMoney(remove)){
			money -= remove;
			return true;
		}
		return false;
	}
	
	/**
	 * @return Money on player balance
	 */
	
	public int getMoney(){
		return money;
	}
	
	/**
	 * @param money Amount of money
	 * @return Does player have this amount of money
	 */
	
	public boolean hasMoney(int money){
		return this.money >= money;
	}
	
	/**
	 * Loading money from database
	 */
	
	public void pullMoney(){
		try {
			ResultSet res = MinigamesAPI.getInstance().db.createStatement().executeQuery(
					String.format("SELECT money FROM money WHERE `user`='%s'", name));
			if(res.next()){
				money = res.getInt(0);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Saving money to database
	 */
	
	public void pushMoney(){
		try {
			MinigamesAPI.getInstance().db.createStatement().execute(
					String.format("UPDATE money SET `money`=%d WHERE `user`='%s'", money, name));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public String toString(){
		return "GamePlayer[name=" + name + "]";
	}

}
