package at.feddis08.mmorpg.logic.game.trade;

import at.feddis08.mmorpg.io.database.Functions;
import at.feddis08.mmorpg.io.database.objects.InventoryTrackObject;
import at.feddis08.mmorpg.io.database.objects.Player_balanceObject;
import at.feddis08.mmorpg.io.text_files.files.file_objects.TradeTable_Config_FileObject;
import at.feddis08.mmorpg.logic.Clock;
import at.feddis08.mmorpg.logic.game.CheckInventoryTrack;
import at.feddis08.mmorpg.logic.game.Var;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
import java.util.Objects;

public class InventoryManagment {

    public static void inv_interact(InventoryClickEvent event) throws SQLException {
        InventoryTrackObject inventoryTrackObject = null;
        Location location = null;
        if (!(Var.get_Player_inv_object_by_player_id(event.getWhoClicked().getUniqueId().toString()) == null)){
            location = Var.get_Player_inv_object_by_player_id(event.getWhoClicked().getUniqueId().toString()).origin_opened_inv_location;
        }
        if(!( location == null)){
            String world_id = Objects.requireNonNull(event.getWhoClicked().getLocation().getWorld()).getName();
            String x = String.valueOf(location.getBlock().getX());
            String y = String.valueOf(location.getBlock().getY());
            String z = String.valueOf(location.getBlock().getZ());
            inventoryTrackObject = CheckInventoryTrack.get(world_id, x, y, z);
        }
        if (!(inventoryTrackObject == null)){

            TradeTable_Config_FileObject tradeTable_config_fileObject =  Var.get_TradeTable_Config_FileObject_by_inventory_display_name(inventoryTrackObject.type);

            if (!(tradeTable_config_fileObject == null)) {
                Integer index = 0;
                Boolean didSomething = false;
                while (index < tradeTable_config_fileObject.tradeTables.size()) {

                    TradeTable tradeTable = tradeTable_config_fileObject.tradeTables.get(index);
                    if (event.getSlot() == tradeTable.sell_index) {
                        if (event.getCursor().getType().name().equals(tradeTable.sell_item)) {
                            Clock.clear_wheat_inv = true;
                            Clock.inventory_who_clicked = event.getWhoClicked().getUniqueId().toString();
                            Clock.tradeTable = tradeTable;
                            Clock.inventory_type = inventoryTrackObject.type;
                            Var.get_inventory_by_display_name(inventoryTrackObject.type).inv.setItem(tradeTable.sell_index, new ItemStack(Material.AIR));
                        } else {
                            event.setCancelled(true);
                        }
                        didSomething = true;
                    }
                    if (event.getSlot() == tradeTable.buy_index) {
                        event.setCancelled(true);
                        Player_balanceObject player_balance = Functions.getPlayers_balance("player_id", event.getWhoClicked().getUniqueId().toString());
                        if (player_balance.pocket >= tradeTable.buy_price) {
                            Functions.update("players_balance", "pocket", String.valueOf(player_balance.pocket - tradeTable.buy_price), player_balance.player_id, "player_id");
                            event.getWhoClicked().getInventory().addItem(new ItemStack(Material.getMaterial(tradeTable.buy_item), tradeTable.buy_amount));
                        }
                        didSomething = true;
                    }
                    index = index + 1;
                }
                if (!(didSomething)){
                        if((event.getClickedInventory() == Var.get_inventory_by_display_name(inventoryTrackObject.type).inv))
                            event.setCancelled(true);
                }
            }
        }
    }
}
