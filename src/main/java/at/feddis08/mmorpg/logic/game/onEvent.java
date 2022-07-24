package at.feddis08.mmorpg.logic.game;

import at.feddis08.mmorpg.MMORPG;
import at.feddis08.mmorpg.inventories.getBakerInv;
import at.feddis08.mmorpg.inventories.getJobInv;
import org.bukkit.block.Furnace;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

import java.sql.SQLException;

public class onEvent {
    public static void onInventoryClick (InventoryClickEvent event) throws SQLException {
        /*
        if (event.getInventory().equals(getJobInv.inv)){
            event.setCancelled(true);
            getJobInv.setPlayerJob((Player) event.getWhoClicked(), event.getSlot());
            event.getWhoClicked().closeInventory();
        }
        if (event.getInventory().equals(getBakerInv.inv)){
            Boolean result = false;
            if (event.getSlot() == 2)
                result = true;
            if (event.getSlot() == 5)
                result = true;
            if (event.getSlot() == 8)
                result = true;
            if (result){

            }else{
                event.setCancelled(true);
                MMORPG.consoleLog("daw");
            }
        }

         */
    }
    public static void onInventoryOpened(InventoryOpenEvent event){
        /*
        if (getBakerInv.checkInvType(event.getInventory().getType().name())){
            event.setCancelled(true);
            getBakerInv.set_player_inventory((Player)event.getPlayer());
        }

         */
    }
}
