package at.feddis08.mmorpg.listeners;

import at.feddis08.mmorpg.logic.game.onEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class onInvOpened {
    public static void onInvOpened(InventoryOpenEvent event){
        onEvent.onInventoryOpened(event);
    }
}
