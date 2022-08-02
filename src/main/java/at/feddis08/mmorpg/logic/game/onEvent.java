package at.feddis08.mmorpg.logic.game;

import at.feddis08.mmorpg.MMORPG;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

import java.sql.SQLException;

public class onEvent {
    public static void onInventoryClick (InventoryClickEvent event) throws SQLException {
    }
    public static void onInventoryOpened(InventoryOpenEvent event) throws SQLException {
        CheckInventoryTrack.check(event);
    }
}
