package at.feddis08.mmorpg.listeners;

import at.feddis08.mmorpg.MMORPG;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntitySpawnEvent;

public class onSpawn {
    public static void onSpawn(EntitySpawnEvent event){
        Entity entity = event.getEntity();
        if (!(entity instanceof Player)){
            event.setCancelled(true);
        }
    }
}
