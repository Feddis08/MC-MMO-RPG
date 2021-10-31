package at.feddis08.mmorpg.listeners;

import at.feddis08.mmorpg.MMORPG;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Array;
import java.util.Collection;

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
