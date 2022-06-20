package at.feddis08.mmorpg.inventories;

import at.feddis08.mmorpg.database.Functions;
import at.feddis08.mmorpg.database.objects.PlayerObject;
import at.feddis08.mmorpg.logic.game.jobs.Baker;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class getJobInv {
    public static Inventory inv;
    public static void createInv(){
        getJobInv.inv = Bukkit.createInventory(null, 9, "Select Job");
        ItemStack item = new ItemStack(Material.WHEAT, 1);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("Farmer");
        List<String> lore = new ArrayList<String>();
        lore.add("a classic farmer");
        meta.setLore(lore);
        item.setItemMeta(meta);
        inv.setItem(0, item);

        meta.setDisplayName("Miner");
        List<String> lore2 = new ArrayList<String>();
        lore2.add("a classic miner");
        meta.setLore(lore2);
        item.setType(Material.IRON_ORE);
        item.setItemMeta(meta);
        inv.setItem(1, item);

        meta.setDisplayName("Woodcutter");
        List<String> lore3 = new ArrayList<String>();
        lore3.add("a classic woodcutter");
        meta.setLore(lore3);
        item.setType(Material.OAK_LOG);
        item.setItemMeta(meta);
        inv.setItem(2, item);

        meta.setDisplayName("baker");
        List<String> lore4 = new ArrayList<String>();
        lore4.add("a classic baker");
        meta.setLore(lore4);
        item.setType(Material.BREAD);
        item.setItemMeta(meta);
        inv.setItem(4, item);
    }

    public static void set_player_inventory(Player player){
        player.openInventory(getJobInv.inv);
    }
    public static void setPlayerJob(Player player, Integer slot) throws SQLException {
        if (slot == 0){
            Functions.update("players", "job", "Farmer", player.getUniqueId().toString(), "id");
        }
        if (slot == 1){
            Functions.update("players", "job", "Miner", player.getUniqueId().toString(), "id");
        }
        if (slot == 2){
            Functions.update("players", "job", "Woodcutter", player.getUniqueId().toString(), "id");
        }
        if (slot == 4){
            Baker.setPlayerJob(player.getUniqueId().toString());
        }
    }

}
