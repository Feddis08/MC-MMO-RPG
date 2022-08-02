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
            ItemStack item = new ItemStack(Material.RED_STAINED_GLASS_PANE);
            ItemMeta meta = item.getItemMeta();

            meta.setDisplayName("SELL");
            List<String> lore = new ArrayList<String>();
            lore.add("Put the wheat in there");
            meta.setLore(lore);
            item.setItemMeta(meta);
            inv.setItem(2, item);

            meta.setDisplayName("!");
            List<String> lore3 = new ArrayList<String>();
            lore3.add("");
            meta.setLore(lore3);
            item.setType(Material.WHITE_STAINED_GLASS_PANE);
            item.setItemMeta(meta);
            inv.setItem(0, item);

            meta.setDisplayName("BUY");
            List<String> lore2 = new ArrayList<String>();
            lore2.add("Buy 32x wheat");
            meta.setLore(lore2);
            item.setType(Material.WHEAT);
            item.setItemMeta(meta);
            inv.setItem(6, item);


            meta.setDisplayName("!");
            List<String> lore4 = new ArrayList<String>();
            lore4.add("");
            meta.setLore(lore4);
            item.setType(Material.WHITE_STAINED_GLASS_PANE);
            item.setItemMeta(meta);
            inv.setItem(1, item);

            meta.setDisplayName("!");
            List<String> lore5 = new ArrayList<String>();
            lore5.add("");
            meta.setLore(lore5);
            item.setType(Material.WHITE_STAINED_GLASS_PANE);
            item.setItemMeta(meta);
            inv.setItem(3, item);


            meta.setDisplayName("!");
            List<String> lore6 = new ArrayList<String>();
            lore6.add("");
            meta.setLore(lore6);
            item.setType(Material.WHITE_STAINED_GLASS_PANE);
            item.setItemMeta(meta);
            inv.setItem(4, item);


            meta.setDisplayName("!");
            List<String> lore7 = new ArrayList<String>();
            lore7.add("");
            meta.setLore(lore7);
            item.setType(Material.WHITE_STAINED_GLASS_PANE);
            item.setItemMeta(meta);
            inv.setItem(5, item);


            meta.setDisplayName("!");
            List<String> lore8 = new ArrayList<String>();
            lore8.add("");
            meta.setLore(lore8);
            item.setType(Material.WHITE_STAINED_GLASS_PANE);
            item.setItemMeta(meta);
            inv.setItem(7, item);

            meta.setDisplayName("!");
            List<String> lore9 = new ArrayList<String>();
            lore9.add("");
            meta.setLore(lore9);
            item.setType(Material.WHITE_STAINED_GLASS_PANE);
            item.setItemMeta(meta);
            inv.setItem(8, item);
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
