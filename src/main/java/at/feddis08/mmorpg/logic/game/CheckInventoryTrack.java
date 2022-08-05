package at.feddis08.mmorpg.logic.game;

import at.feddis08.mmorpg.io.database.Functions;
import at.feddis08.mmorpg.io.database.objects.InventoryTrackObject;
import at.feddis08.mmorpg.logic.game.trade.InventoryManagment;
import at.feddis08.mmorpg.logic.game.trade.invObjects.PlayerInvObject;
import org.bukkit.event.inventory.*;

import java.sql.SQLException;
import java.util.Objects;

public class CheckInventoryTrack {

    public static InventoryTrackObject get(String world_id, String x, String y, String z) throws SQLException {
        return Functions.getInventoryTrackByLocation(world_id, x, y, z);
    }
    public static void checkInvOpen(InventoryOpenEvent event) throws SQLException {
        if(!( event.getInventory().getLocation() == null)){
            PlayerInvObject playerInvObject = Var.get_Player_inv_object_by_player_id(event.getPlayer().getUniqueId().toString());
            if (playerInvObject == null) {
                playerInvObject = new PlayerInvObject();
                Var.playerInvObjects.add(playerInvObject);
            }
            playerInvObject.player_id = event.getPlayer().getUniqueId().toString();
            playerInvObject.origin_opened_inv_location = event.getInventory().getLocation();

            String world_id = Objects.requireNonNull(event.getPlayer().getLocation().getWorld()).getName();
            String x = String.valueOf(event.getInventory().getLocation().getBlock().getX());
            String y = String.valueOf(event.getInventory().getLocation().getBlock().getY());
            String z = String.valueOf(event.getInventory().getLocation().getBlock().getZ());
            InventoryTrackObject inventoryTrackObject = get(world_id, x, y, z);
            if (Objects.equals(inventoryTrackObject.world_id, world_id)){
                event.setCancelled(true);
                    event.getPlayer().closeInventory();
                    event.getPlayer().openInventory(Var.get_inventory_by_display_name(inventoryTrackObject.type).inv);
            }
        }
    }
    public static void checkInvClicked(InventoryClickEvent event) throws SQLException {
        InventoryManagment.inv_interact(event);
    }
    public static void checkInvDrag(InventoryDragEvent event){
    }
}
