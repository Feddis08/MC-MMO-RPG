package at.feddis08.mmorpg.logic.game;

import at.feddis08.mmorpg.MMORPG;
import at.feddis08.mmorpg.logic.game.portal.checkMove;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.sql.SQLException;

public class onEvent {
    public static void onInventoryClick (InventoryClickEvent event) throws SQLException {
        CheckInventoryTrack.checkInvClicked(event);
    }
    public static void onInventoryOpened(InventoryOpenEvent event) throws SQLException {
        CheckInventoryTrack.checkInvOpen(event);
    }
    public static void onInventoryDrag(InventoryDragEvent event){
        CheckInventoryTrack.checkInvDrag(event);
    }
    public static void onPlayerMove(PlayerMoveEvent event) throws SQLException {
        checkMove.check(event);
    }
}
