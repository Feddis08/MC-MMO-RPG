package at.feddis08.mmorpg.minecraft.listeners;

import at.feddis08.mmorpg.MMORPG;
import org.bukkit.event.player.PlayerInteractEvent;

public class onPlayerInteract {
    public static void playerInteract(PlayerInteractEvent event){
        MMORPG.consoleLog("wads " + event.getAction() + " " + event.getItem().getType().name());
    }

}
