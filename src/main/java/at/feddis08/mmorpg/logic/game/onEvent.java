package at.feddis08.mmorpg.logic.game;

import at.feddis08.mmorpg.inventories.getJobInv;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

import java.sql.SQLException;

public class onEvent {
    public static void onInventoryClick (InventoryClickEvent event) throws SQLException {
        if (event.getInventory().equals(getJobInv.inv)){
            event.setCancelled(true);
            getJobInv.setPlayerJob((Player) event.getWhoClicked(), event.getSlot());
            event.getWhoClicked().closeInventory();
        }
    }
    public static void onInventoryOpened(InventoryOpenEvent event){

    }
}
