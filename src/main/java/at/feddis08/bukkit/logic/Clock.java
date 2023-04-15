package at.feddis08.bukkit.logic;

import at.feddis08.bukkit.MMORPG;
import at.feddis08.tools.io.database.Functions;
import at.feddis08.tools.io.database.objects.Player_balanceObject;
import at.feddis08.bukkit.logic.game.Var;
import at.feddis08.bukkit.logic.game.trade.TradeTable;
import at.feddis08.bukkit.logic.game.trade.invObjects.Inventory;
import at.feddis08.bukkit.logic.game.trade.invObjects.Slot;
import at.feddis08.bukkit.logic.scripts.Main;
import at.feddis08.bukkit.logic.scripts.VarObject;
import com.destroystokyo.paper.event.server.ServerTickStartEvent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static at.feddis08.bukkit.logic.scripts.Main.script_start_by_event_name;

public class Clock {

    public static Boolean clear_wheat_inv = false;
    public static String inventory_who_clicked = "";
    public static String inventory_type = "";
    public static TradeTable tradeTable = null;
    
    public static void tick(ServerTickStartEvent event) throws SQLException, IOException, InterruptedException {
        at.feddis08.bukkit.logic.game.ore_mine.Main.check_mines_per_tick();
        at.feddis08.bukkit.logic.game.mob_spawner.Main.check_spawners_per_tick();
        ArrayList<VarObject> varObjects = new ArrayList<VarObject>();
        varObjects.add(new VarObject("tick_count", "INTEGER", String.valueOf(event.getTickNumber())));
        try {
            script_start_by_event_name ("TICK_START", varObjects);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Main.check_all_after_events();
        if (clear_wheat_inv){

            Inventory inventory = Var.get_inventory_by_display_name(inventory_type);
            Player_balanceObject player_balance = Functions.getPlayers_balance("player_id", inventory_who_clicked);
            Functions.update("players_balance", "pocket", String.valueOf(player_balance.pocket + (tradeTable.sell_price * tradeTable.sell_amount)), player_balance.player_id, "player_id");
            player_balance = Functions.getPlayers_balance("player_id", inventory_who_clicked);
            Slot slot = Var.get_inventory_slot_by_index(inventory, tradeTable.sell_index);
            ItemStack item = new ItemStack(Material.getMaterial(slot.item_material));
            ItemMeta meta = item.getItemMeta();


            meta.setDisplayName(slot.display_name);
            List<String> lore = new ArrayList<String>();
            Integer index = 0;
            while (index < slot.lore.size()){
                lore.add(slot.lore.get(index));
                index = index + 1;
            }
            meta.setLore(lore);
            item.setItemMeta(meta);
            Var.get_inventory_by_display_name(inventory_type).inv.setItem(tradeTable.sell_index, item);

            MMORPG.Server.getPlayer(Functions.getPlayer("id", player_balance.player_id).player_name).sendMessage(ChatColor.GREEN + "You sold " + ChatColor.YELLOW + tradeTable.buy_item + ChatColor.GREEN + ". Now you have " + ChatColor.YELLOW + player_balance.pocket + ChatColor.GREEN + " coins in your pocket!");
            clear_wheat_inv = false;
            inventory_who_clicked = "";
            tradeTable = null;
            inventory_type = "";
        }
    }
}
