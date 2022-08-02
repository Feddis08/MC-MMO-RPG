package at.feddis08.mmorpg.logic.game;

import at.feddis08.mmorpg.database.Functions;
import at.feddis08.mmorpg.database.objects.InventoryTrackObject;
import at.feddis08.mmorpg.minecraft.inventories.WheatTradeInv;
import org.bukkit.event.inventory.InventoryOpenEvent;

import java.sql.SQLException;
import java.util.Objects;

public class CheckInventoryTrack {

    public static InventoryTrackObject get(String world_id, String x, String y, String z) throws SQLException {
        return Functions.getInventoryTrackByLocation(world_id, x, y, z);
    }
    public static void check(InventoryOpenEvent event) throws SQLException {
        String world_id = Objects.requireNonNull(Objects.requireNonNull(event.getInventory().getLocation()).getWorld()).getName();
        String x = String.valueOf(event.getInventory().getLocation().getBlock().getX());
        String y = String.valueOf(event.getInventory().getLocation().getBlock().getY());
        String z = String.valueOf(event.getInventory().getLocation().getBlock().getZ());
        InventoryTrackObject inventoryTrackObject = get(world_id, x, y, z);
        if (Objects.equals(inventoryTrackObject.world_id, world_id)){
            if (Objects.equals(inventoryTrackObject.type, "trade_wheat")){
                event.setCancelled(true);
                event.getPlayer().openInventory(WheatTradeInv.inv);
            }
        }
    }

}
