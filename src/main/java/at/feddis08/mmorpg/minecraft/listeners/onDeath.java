package at.feddis08.mmorpg.minecraft.listeners;

import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

public class onDeath {
    public static void onDeath(PlayerDeathEvent event){
        ItemStack item = event.getEntity().getKiller().getItemInHand();
        String itemToDisplay = item.getType().toString().toLowerCase();
        if (itemToDisplay == "ItemStack{AIR x 0}"){
            itemToDisplay = "pure Hand";
        }
        event.setDeathMessage(event.getEntity().getKiller().getDisplayName() + " killed: " + event.getEntity().getDisplayName() + " with: " + itemToDisplay + " !");

    }
}
