package ru.serega6531.MinigamesAPI;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.kitteh.tag.AsyncPlayerReceiveNameTagEvent;

import ru.serega6531.MinigamesAPI.MinigamesAPI;
import ru.serega6531.MinigamesAPI.arena.GameArena;
import ru.serega6531.MinigamesAPI.events.GamePlayerCrossArenaBorderEvent;
import ru.serega6531.MinigamesAPI.events.GamePlayerHitEvent;
import ru.serega6531.MinigamesAPI.events.GamePlayerKillEvent;
import ru.serega6531.MinigamesAPI.events.GameSignDestroyEvent;
import ru.serega6531.MinigamesAPI.player.GamePlayer;
import ru.serega6531.MinigamesAPI.session.GameSession;
import ru.serega6531.MinigamesAPI.signs.GameSign;
import ru.serega6531.MinigamesAPI.signs.SignManager;

public class EventListener implements Listener {
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		Player player = e.getPlayer();
		player.teleport(player.getWorld().getSpawnLocation());
		player.setExp(0);
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e){
		GameSession s = MinigamesAPI.getInstance().getPlayerSession(e.getPlayer().getName());
		if(s != null) s.removePlayer(MinigamesAPI.getInstance().getGamePlayer(e.getPlayer().getName()));
	}

	@EventHandler
	public void onMove(PlayerMoveEvent e){
		GameSession session = MinigamesAPI.getInstance().getPlayerSession(e.getPlayer().getName());
		if(session != null && session.isStarted()){
			GameArena arena = session.getArena();
			if(!isInAABB(e.getTo(), arena.getCorners()[1], arena.getCorners()[0])){
				Bukkit.getPluginManager().callEvent(new GamePlayerCrossArenaBorderEvent(
						MinigamesAPI.getInstance().getGamePlayer(e.getPlayer().getName()), arena
				));
			}
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e){
		if(e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		BlockState state = e.getClickedBlock().getState();
		if(state instanceof Sign){
			Sign sign = (Sign) state;
			GameSign gs = SignManager.getSign(sign.getLocation());
			if(gs != null)
				gs.getHandler().onClick(e.getPlayer(), gs.getCommand(), gs.getArgument());
		}
	}
	
	@EventHandler
	public void onKill(PlayerDeathEvent e){
		Player victim = e.getEntity();
		Player killer = victim.getKiller();
		if(killer == null) return;
		GameSession s1, s2;
		s1 = MinigamesAPI.getInstance().getPlayerSession(killer.getName());
		s2 = MinigamesAPI.getInstance().getPlayerSession(victim.getName());
		if(s1 != null && s2 != null && s1.equals(s2)){
			GamePlayerKillEvent event = new GamePlayerKillEvent(
					MinigamesAPI.getInstance().getGamePlayer(killer.getName()), 
					MinigamesAPI.getInstance().getGamePlayer(victim.getName()),
					s1);
			Bukkit.getPluginManager().callEvent(event);
		}
	}
	
	@EventHandler
	public void onHit(EntityDamageByEntityEvent e){
		if(e.getDamager().getType() == EntityType.PLAYER && e.getEntityType() == EntityType.PLAYER){
			GamePlayer damager = MinigamesAPI.getInstance().getGamePlayer(((Player) e.getDamager()).getName());
			GamePlayer victim = MinigamesAPI.getInstance().getGamePlayer(((Player) e.getEntity()).getName());
			GameSession s1 = MinigamesAPI.getInstance().getPlayerSession(((Player) e.getDamager()).getName());
			GameSession s2 = MinigamesAPI.getInstance().getPlayerSession(((Player) e.getEntity()).getName());
			if(s1 != null && s1 != null && s1.equals(s2)){
				GamePlayerHitEvent event = new GamePlayerHitEvent(damager, victim, s1, e.getCause(), e.getDamage());
				Bukkit.getPluginManager().callEvent(event);
				e.setCancelled(event.isCancelled());
			}
		}
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent e){
		if(!e.getPlayer().isOp()){
			e.setCancelled(true);
			return;
		}
		if(SignManager.containsSign(e.getBlock().getLocation())){
			GameSign sign = SignManager.getSign(e.getBlock().getLocation());
			GameSignDestroyEvent event = new GameSignDestroyEvent(sign);
			Bukkit.getPluginManager().callEvent(event);
			if(!event.isCancelled())
				SignManager.unregisterSign(e.getBlock().getLocation());
			else
				e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onNameTag(AsyncPlayerReceiveNameTagEvent e) {
		GameSession sess = MinigamesAPI.getInstance().getPlayerSession(e.getNamedPlayer().getName());
		if(sess != null && sess.isStarted())
			e.setTag(sess.getPlayerTeam(e.getNamedPlayer().getName()).getColor() + e.getNamedPlayer().getName() + ChatColor.RESET);
		else e.setTag(e.getNamedPlayer().getName());
	}
	
	private boolean isInAABB(Location loc, Location min, Location max){
		return loc.getX() >= min.getX() && loc.getX() <= max.getX() && loc.getY() >= min.getY() && loc.getY() <= max.getY() && loc.getZ() >= min.getZ() && loc.getZ() <= max.getZ();
	}

}
