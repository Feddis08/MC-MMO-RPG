package at.feddis08.bukkit.minecraft.listeners;

import at.feddis08.Boot;
import at.feddis08.bukkit.logic.scripts.VarObject;
import at.feddis08.bukkit.logic.scripts.Main;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.checkerframework.checker.units.qual.A;

import java.io.IOException;
import java.util.ArrayList;

public class onSpawn {
    public static void onSpawn(EntitySpawnEvent event) throws IOException, InterruptedException {
        Entity entity = event.getEntity();
        if (!(entity instanceof Player)){
            if (!Boot.config.allow_entity_spawning){
                event.setCancelled(true);
            }else{
                ArrayList<VarObject> varObjects = new ArrayList<>();
                varObjects.add(new VarObject("entity_name", "STRING", entity.getName()));
                varObjects.add(new VarObject("entity_ticks_lived", "INTEGER", String.valueOf(entity.getTicksLived())));
                varObjects.add(new VarObject("entity_world_name", "STRING", entity.getLocation().getWorld().getName()));
                varObjects.add(new VarObject("entity_x", "STRING", String.valueOf(entity.getLocation().getBlockX())));
                varObjects.add(new VarObject("entity_y", "STRING", String.valueOf(entity.getLocation().getBlockY())));
                varObjects.add(new VarObject("entity_z", "STRING", String.valueOf(entity.getLocation().getBlockZ())));
                varObjects.add(new VarObject("entity_type_name", "STRING", entity.getType().name()));
                varObjects.add(new VarObject("entity_is_in_water", "STRING", String.valueOf(entity.isInWater())));
                Main.script_start_by_event_name("ENTITY_SPAWNED", varObjects, false);
            }
        }
    }
}
