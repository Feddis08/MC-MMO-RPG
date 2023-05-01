package at.feddis08.bukkit.logic.game.trade;

import at.feddis08.bukkit.MMORPG;
import at.feddis08.tools.io.database.Functions;
import at.feddis08.tools.io.database.objects.InventoryTrackObject;
import at.feddis08.tools.io.database.objects.Player_balanceObject;
import at.feddis08.tools.io.text_files.files.file_objects.TradeTable_Config_FileObject;
import at.feddis08.bukkit.logic.Clock;
import at.feddis08.bukkit.logic.game.CheckInventoryTrack;
import at.feddis08.bukkit.logic.game.Var;
import at.feddis08.bukkit.logic.scripts.Main;
import at.feddis08.bukkit.logic.scripts.VarObject;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
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
        if (!(inventoryTrackObject == null || event.getClickedInventory() == event.getWhoClicked().getInventory())){
            TradeTable_Config_FileObject tradeTable_config_fileObject = Var.get_TradeTable_Config_FileObject_by_inventory_display_name(inventoryTrackObject.type);

            if (!(tradeTable_config_fileObject == null)) {
                Integer index = 0;
                Boolean didSomething = false;
                while (index < tradeTable_config_fileObject.tradeTables.size()) {

                    TradeTable tradeTable = tradeTable_config_fileObject.tradeTables.get(index);
                    if (event.getSlot() == tradeTable.sell_index) {
                        if (event.getCursor().getType().name().equals(tradeTable.sell_item) && event.getCursor().getAmount() >= tradeTable.sell_amount) {
                            Clock.clear_wheat_inv = true;
                            Clock.inventory_who_clicked = event.getWhoClicked().getUniqueId().toString();
                            Clock.tradeTable = tradeTable;
                            Clock.inventory_type = inventoryTrackObject.type;
                            Var.get_inventory_by_display_name(inventoryTrackObject.type).inv.setItem(tradeTable.sell_index, new ItemStack(Material.AIR));
                            event.getCursor().setAmount(event.getCursor().getAmount() - tradeTable.sell_amount + 1);
                            ArrayList<VarObject> varObjects = new ArrayList<VarObject>();
                            varObjects.add(new VarObject("player_id", "STRING", event.getWhoClicked().getUniqueId().toString()));
                            varObjects.add(new VarObject("shop_name", "STRING", tradeTable_config_fileObject.inventory_display_name));
                            varObjects.add(new VarObject("material_name", "STRING", tradeTable.sell_item));
                            varObjects.add(new VarObject("profit", "INTEGER", String.valueOf(tradeTable.sell_price * tradeTable.sell_amount)));
                            varObjects.add(new VarObject("amount", "INTEGER", String.valueOf(tradeTable.sell_amount)));
                            try {
                                Main.script_start_by_event_name("PLAYER_SOLD_AT_SHOP", varObjects, false);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            event.setCancelled(true);
                        }
                        didSomething = true;
                    }
                    if (event.getSlot() == tradeTable.buy_index) {
                        event.setCancelled(true);
                        Player_balanceObject player_balance = Functions.getPlayers_balance("player_id", event.getWhoClicked().getUniqueId().toString());
                        if (player_balance.pocket >= tradeTable.buy_price * tradeTable.buy_amount) {
                            ArrayList<VarObject> varObjects = new ArrayList<VarObject>();
                            varObjects.add(new VarObject("player_id", "STRING", player_balance.player_id));
                            varObjects.add(new VarObject("shop_name", "STRING", tradeTable_config_fileObject.inventory_display_name));
                            varObjects.add(new VarObject("material_name", "STRING", tradeTable.buy_item));
                            varObjects.add(new VarObject("cost", "INTEGER", String.valueOf(tradeTable.buy_price * tradeTable.buy_amount)));
                            varObjects.add(new VarObject("amount", "INTEGER", String.valueOf(tradeTable.buy_amount)));
                            try {
                                Main.script_start_by_event_name("PLAYER_BOUGHT_AT_SHOP", varObjects, false);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            Functions.update("players_balance", "pocket", String.valueOf(player_balance.pocket - tradeTable.buy_price * tradeTable.buy_amount), player_balance.player_id, "player_id");
                            event.getWhoClicked().getInventory().addItem(new ItemStack(Material.getMaterial(tradeTable.buy_item), tradeTable.buy_amount));
                            MMORPG.Server.getPlayer(Functions.getPlayer("id", player_balance.player_id).player_name).sendMessage(ChatColor.GREEN + "You bought " + ChatColor.YELLOW + tradeTable.buy_item + ChatColor.GREEN + ". Now you have " + ChatColor.YELLOW + (player_balance.pocket - tradeTable.buy_amount * tradeTable.buy_price) + ChatColor.GREEN + " coins in your pocket!");
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
