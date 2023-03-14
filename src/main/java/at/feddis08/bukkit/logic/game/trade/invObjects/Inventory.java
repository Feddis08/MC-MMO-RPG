package at.feddis08.bukkit.logic.game.trade.invObjects;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Inventory {
    public ArrayList<Slot> slots = new ArrayList<>();
    public String display_name = "";
    public org.bukkit.inventory.Inventory inv;
    public Integer size = 0;

    public void construct(){
        Integer index = 0;
        inv = Bukkit.createInventory(null, size, display_name);
        while (index < slots.size()){
            Slot slot = slots.get(index);
            ItemStack item = new ItemStack(Objects.requireNonNull(Material.getMaterial(slot.item_material)));
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(slot.display_name);
            List<String> lore = new ArrayList<String>();
            Integer index2 = 0;
            while (index2 < slot.lore.size()){
                String text = slot.lore.get(index2);
                lore.add(text);
                index2 = index2 + 1;
            }
            meta.setLore(lore);
            item.setItemMeta(meta);
            inv.setItem(slot.index, item);
            index = index + 1;
        }
    }
}
