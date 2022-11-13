package at.feddis08.mmorpg.minecraft.listeners;

import at.feddis08.mmorpg.MMORPG;
import at.feddis08.mmorpg.logic.Clock;
import at.feddis08.mmorpg.logic.game.onEvent;
import com.destroystokyo.paper.event.server.ServerTickStartEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.*;

import java.io.IOException;
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
    public static void onPlayerInteractEntity(PlayerInteractEntityEvent event) throws SQLException {
        onEvent.onEntityClick(event);
    }
    @EventHandler
    public static void test(InventoryDragEvent event){
        onEvent.onInventoryDrag(event);
    }
    @EventHandler
    public static void onDeath(PlayerDeathEvent event){
        onDeath.onDeath(event);
    }
    @EventHandler
    public static void onPlayerMove(PlayerMoveEvent event) throws SQLException {
        onEvent.onPlayerMove(event);
    }
    @EventHandler
    public static void onBlockBreak(BlockBreakEvent event) throws SQLException {
        onBlockEvents.onBlockBreak(event);
    }
    @EventHandler
    public static void onTickEvent(ServerTickStartEvent event) throws SQLException {Clock.tick(event);}
    @EventHandler
    public static void onPlayerInteractEvent(PlayerInteractEvent event){
        onPlayerInteract.playerInteract(event);
    }
    @EventHandler
    public static void onSpawn(EntitySpawnEvent event){
        onSpawn.onSpawn(event);
    }
    @EventHandler
    public static void onInventoryClick(InventoryClickEvent event) throws SQLException {
        onInventoryClick.onInventoryClick(event);
    }
    @EventHandler
    public static void onInvOpened(InventoryOpenEvent event) throws SQLException {
        onInvOpened.onInvOpened(event);
    }
    @EventHandler
    public static void onChat(AsyncPlayerChatEvent event) throws SQLException, IOException {onChat.onChat(event);}
    @EventHandler
    public static void onPlaced(BlockPlaceEvent event) throws SQLException {onEvent.onBlockPlaced(event);}
}
