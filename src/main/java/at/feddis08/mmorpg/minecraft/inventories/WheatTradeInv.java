package at.feddis08.mmorpg.minecraft.inventories;

import at.feddis08.mmorpg.MMORPG;
import at.feddis08.mmorpg.database.Functions;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WheatTradeInv {
        public static Inventory inv;
        public static void createInv(){
            WheatTradeInv.inv = Bukkit.createInventory(null, 9, "Trade Wheat");
            ItemStack item = new ItemStack(Material.RED_STAINED_GLASS, 1);
            ItemMeta meta = item.getItemMeta();

            meta.setDisplayName("SELL");
            List<String> lore = new ArrayList<String>();
            lore.add("Put the wheat in there");
            meta.setLore(lore);
            item.setItemMeta(meta);
            inv.setItem(2, item);

            meta.setDisplayName("BUY");
            List<String> lore2 = new ArrayList<String>();
            lore2.add("Buy 16x wheat");
            meta.setLore(lore2);
            item.setType(Material.IRON_ORE);
            item.setItemMeta(meta);
            inv.setItem(5, item);

        }

        public static void set_player_inventory(Player player){
            player.openInventory(WheatTradeInv.inv);
        }
        public static void setPlayerJob(Player player, Integer slot) throws SQLException {
            if (slot == 2){
                MMORPG.consoleLog("2");
            }
            if (slot == 5){
                MMORPG.consoleLog("5");
            }
            if (slot == 6){
                MMORPG.consoleLog("6");
            }
            if (slot == 7){
                MMORPG.consoleLog("7");
            }
        }

    }
