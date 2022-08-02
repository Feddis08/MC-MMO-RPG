package at.feddis08.mmorpg.logic;

import at.feddis08.mmorpg.MMORPG;
import at.feddis08.mmorpg.database.Functions;
import at.feddis08.mmorpg.database.objects.Player_balanceObject;
import at.feddis08.mmorpg.logic.game.trade.Wheat;
import at.feddis08.mmorpg.minecraft.inventories.WheatTradeInv;
import com.destroystokyo.paper.event.server.ServerTickStartEvent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Clock {

    public static Boolean clear_wheat_inv = false;
    public static String wheat_who_clicked = "";

    public static void tick(ServerTickStartEvent event) throws SQLException {
        if (clear_wheat_inv){
            Player_balanceObject player_balance = Functions.getPlayers_balance("player_id", wheat_who_clicked);
            Functions.update("players_balance", "pocket", String.valueOf(player_balance.pocket + (Wheat.wheat_sell_price * Objects.requireNonNull(WheatTradeInv.inv.getItem(2)).getAmount())), player_balance.player_id, "player_id");
            player_balance = Functions.getPlayers_balance("player_id", wheat_who_clicked);
            ItemStack item = new ItemStack(Material.RED_STAINED_GLASS_PANE);
            ItemMeta meta = item.getItemMeta();

            meta.setDisplayName("SELL");
            List<String> lore = new ArrayList<String>();
            lore.add("Put the wheat in there");
            meta.setLore(lore);
            item.setItemMeta(meta);
            WheatTradeInv.inv.setItem(2, item);

            MMORPG.Server.getPlayer(Functions.getPlayer("id", player_balance.player_id).player_name).sendMessage(ChatColor.GREEN + "You sold " + ChatColor.YELLOW + "WHEAT" + ChatColor.GREEN + ". Now you have " + ChatColor.YELLOW + player_balance.pocket + ChatColor.GREEN + " coins in your pocket!");
            clear_wheat_inv = false;
            wheat_who_clicked = "";
        }
    }
}
