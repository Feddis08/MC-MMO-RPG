package at.feddis08.mmorpg.minecraft.listeners;

import at.feddis08.mmorpg.logic.game.onEvent;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.sql.SQLException;

public class onInventoryClick {
    public static void onInventoryClick(InventoryClickEvent event) throws SQLException {
        onEvent.onInventoryClick(event);
    }
}
