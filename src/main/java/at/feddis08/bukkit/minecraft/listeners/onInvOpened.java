package at.feddis08.bukkit.minecraft.listeners;

import at.feddis08.bukkit.logic.game.onEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

import java.sql.SQLException;

public class onInvOpened {
    public static void onInvOpened(InventoryOpenEvent event) throws SQLException {
        onEvent.onInventoryOpened(event);
    }
}
