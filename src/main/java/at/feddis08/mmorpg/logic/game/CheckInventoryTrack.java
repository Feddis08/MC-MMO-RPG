package at.feddis08.mmorpg.logic.game;

import at.feddis08.mmorpg.io.database.Functions;
import at.feddis08.mmorpg.io.database.objects.InventoryTrackObject;
import at.feddis08.mmorpg.io.database.objects.Player_balanceObject;
import at.feddis08.mmorpg.logic.Clock;
import at.feddis08.mmorpg.logic.game.trade.Wheat;
import at.feddis08.mmorpg.minecraft.inventories.WheatTradeInv;
import org.bukkit.Material;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
import java.util.Objects;

public class CheckInventoryTrack {

    public static InventoryTrackObject get(String world_id, String x, String y, String z) throws SQLException {
        return Functions.getInventoryTrackByLocation(world_id, x, y, z);
    }
    public static void checkInvOpen(InventoryOpenEvent event) throws SQLException {
        if(!( event.getInventory().getLocation() == null)){
            String world_id = Objects.requireNonNull(event.getPlayer().getLocation().getWorld()).getName();
            String x = String.valueOf(event.getInventory().getLocation().getBlock().getX());
            String y = String.valueOf(event.getInventory().getLocation().getBlock().getY());
            String z = String.valueOf(event.getInventory().getLocation().getBlock().getZ());
            InventoryTrackObject inventoryTrackObject = get(world_id, x, y, z);
            if (Objects.equals(inventoryTrackObject.world_id, world_id)){
                event.setCancelled(true);
                if (Objects.equals(inventoryTrackObject.type, "trade_wheat")){
                    event.getPlayer().closeInventory();
                    event.getPlayer().openInventory(WheatTradeInv.inv);
                }
            }
        }
    }
    public static void checkInvClicked(InventoryClickEvent event) throws SQLException {
        if (event.getClickedInventory() == WheatTradeInv.inv) {
            if (event.getSlot() == 2){
                if (event.getCursor().getType().name().equals("WHEAT")){
                    Clock.clear_wheat_inv = true;
                    Clock.wheat_who_clicked = event.getWhoClicked().getUniqueId().toString();
                    WheatTradeInv.inv.setItem(2, new ItemStack(Material.AIR));
                }else{
                    event.setCancelled(true);
                }
            }
            if (event.getSlot() == 6){
                event.setCancelled(true);
                Player_balanceObject player_balance = Functions.getPlayers_balance("player_id", event.getWhoClicked().getUniqueId().toString());
                if (player_balance.pocket >= Wheat.wheat_buy_price){
                    Functions.update("players_balance", "pocket", String.valueOf(player_balance.pocket - Wheat.wheat_buy_price), player_balance.player_id, "player_id");
                    event.getWhoClicked().getInventory().addItem(new ItemStack(Material.WHEAT, 32));
                }
            }
        }
    }
    public static void checkInvDrag(InventoryDragEvent event){
    }
}
