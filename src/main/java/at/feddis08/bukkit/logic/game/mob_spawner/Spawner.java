package at.feddis08.bukkit.logic.game.mob_spawner;

import at.feddis08.bukkit.MMORPG;
import at.feddis08.bukkit.logic.scripts.Main;
import at.feddis08.bukkit.logic.scripts.VarObject;
import at.feddis08.bukkit.minecraft.tools.Methods;
import at.feddis08.bukkit.minecraft.tools.classes.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

import static at.feddis08.bukkit.logic.scripts.Main.script_start_by_event_name;

public class Spawner {
    public String world_name;
    public String name;
    public String mob_type;
    public ArrayList<Location> spawn_points = new ArrayList<Location>();
    public Integer cool_down_ticks = 0;
    public Integer passed_ticks = 0;
    public Integer max_mobs = 0;
    public ArrayList <String> mobs_ids = new ArrayList<>();

    public void check(){
        check_time();
        sort_died_mobs();
        check_mobs_spawn();
    }
    void check_time(){
        if (passed_ticks < cool_down_ticks) {
            passed_ticks += 1;
        }
    }
    void sort_died_mobs() {
        int index = 0;
        while (index < mobs_ids.size()){
            Entity entity = MMORPG.Server.getEntity(UUID.fromString(mobs_ids.get(index)));
            try {
                if (entity == null || entity.isDead()){
                    mobs_ids.remove(index);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            index += 1;
        }
    }
    void spawn_mob(){
        if (spawn_points.size() != 0){
            int used_location = Methods.getRandomInteger(spawn_points.size() - 1, 0);
            Entity entity = MMORPG.Server.getWorld(world_name).spawnEntity(new org.bukkit.Location(MMORPG.Server.getWorld(world_name), Double.parseDouble(String.valueOf(spawn_points.get(used_location).x)), Double.parseDouble(String.valueOf(spawn_points.get(used_location).y)), Double.parseDouble(String.valueOf(spawn_points.get(used_location).z))), EntityType.fromName(mob_type));
            mobs_ids.add(entity.getUniqueId().toString());
        }
    }
    void check_mobs_spawn(){
        if (Objects.equals(passed_ticks, cool_down_ticks) && mobs_ids.size() != max_mobs){
            int index = 0;
            passed_ticks = 0;
            while (index < max_mobs - mobs_ids.size()){
                spawn_mob();
                index += 1;
            }
        }
    }
    public void clear_spawner(){
        if (mobs_ids.size() > 0){
            Entity entity = MMORPG.Server.getEntity(UUID.fromString(mobs_ids.get(0)));
            mobs_ids.remove(0);
            entity.remove();
            clear_spawner();
        }
    }
    public void kill_mob(String player_id) throws IOException, InterruptedException {
        ArrayList<VarObject> varObjects = new ArrayList<VarObject>();
        varObjects.add(new VarObject("player_id", "STRING", player_id));
        varObjects.add(new VarObject("spawner_name", "STRING", name));
        if (!Objects.equals(passed_ticks, cool_down_ticks)){
            passed_ticks = 0;
        }
        try {
            script_start_by_event_name ("PLAYER_KILL_MOB_FROM_SPAWNER", varObjects);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
