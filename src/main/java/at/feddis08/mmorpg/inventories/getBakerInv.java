package at.feddis08.mmorpg.inventories;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class getBakerInv {
    public static Inventory inv;
    public static void createInv(){
        getBakerInv.inv = Bukkit.createInventory(null, 9, "Furnace");
        ItemStack item = new ItemStack(Material.GRAY_STAINED_GLASS, 1);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("Blank");
        List<String> lore = new ArrayList<String>();
        lore.add("nothing to put in there");
        meta.setLore(lore);
        item.setItemMeta(meta);
        inv.setItem(0, item);

        meta.setDisplayName("Info");
        List<String> lore2 = new ArrayList<String>();
        lore2.add("What you want to burn");
        lore2.add("---->");
        meta.setLore(lore2);
        item.setType(Material.IRON_ORE);
        item.setItemMeta(meta);
        inv.setItem(1, item);

        meta.setDisplayName("Blank");
        List<String> lore3 = new ArrayList<String>();
        lore3.add("nothing to put in there");
        meta.setLore(lore3);
        item.setItemMeta(meta);
        item.setType(Material.GRAY_STAINED_GLASS);
        inv.setItem(3, item);

        meta.setDisplayName("Info");
        List<String> lore4 = new ArrayList<String>();
        lore4.add("Fuel");
        lore4.add("---->");
        meta.setLore(lore4);
        item.setItemMeta(meta);
        item.setType(Material.COAL);
        inv.setItem(4, item);

        meta.setDisplayName("Blank");
        List<String> lore5 = new ArrayList<String>();
        lore5.add("nothing to put in there");
        meta.setLore(lore5);
        item.setItemMeta(meta);
        item.setType(Material.GRAY_STAINED_GLASS);
        inv.setItem(6, item);

        meta.setDisplayName("Info");
        List<String> lore6 = new ArrayList<String>();
        lore6.add("Output");
        lore6.add("---->");
        meta.setLore(lore6);
        item.setType(Material.IRON_INGOT);
        item.setItemMeta(meta);
        inv.setItem(7, item);
    }


    public static void set_player_inventory(Player player){
        player.openInventory(getBakerInv.inv);
    }
    public static boolean checkInvType(String type){
        if(Objects.equals(type, "FURNACE")){
            return true;
        }
        return false;
    }
}
