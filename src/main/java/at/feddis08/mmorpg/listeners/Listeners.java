package at.feddis08.mmorpg.listeners;

import at.feddis08.mmorpg.MMORPG;
import at.feddis08.mmorpg.commands.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.SQLException;

public class Listeners implements org.bukkit.event.Listener {

    @EventHandler
    public static void onPlayerJoin(PlayerJoinEvent event) throws SQLException {
        connectionEvents.onJoin(event);
    }
    @EventHandler
    public static void onPlayerDisconnect(PlayerQuitEvent event) throws SQLException {
        connectionEvents.onQuit(event);
    }
    @EventHandler
    public static void onDeath(PlayerDeathEvent event){
        onDeath.onDeath(event);
    }
    @EventHandler
    public static void onBlockBreak(BlockBreakEvent event) throws SQLException {
        onBlockEvents.onBlockBreak(event);
    }
    @EventHandler
    public static void onPlayerInteractEvent(PlayerInteractEvent event){
        onPlayerInteract.playerInteract(event);
    }
    @EventHandler
    public static void onChat(AsyncPlayerChatEvent event) throws SQLException {onChat.onChat(event);}
}
