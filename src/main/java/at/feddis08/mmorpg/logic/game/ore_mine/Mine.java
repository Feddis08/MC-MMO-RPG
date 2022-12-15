package at.feddis08.mmorpg.logic.game.ore_mine;


import at.feddis08.mmorpg.MMORPG;
import at.feddis08.mmorpg.logic.scripts.Main;
import at.feddis08.mmorpg.logic.scripts.VarObject;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.Objects;

public class Mine {
    public String world_name;
    public Integer x;
    public Integer y;
    public Integer z;
    public String name;
    public String material_name;
    public Integer cool_down_ticks = 0;
    public Integer passed_ticks = 0;

    public void check(){
        check_time();
        check_world_block_status();
    }
    void check_time(){
        if (passed_ticks < cool_down_ticks) {
            passed_ticks += 1;
        }
    }

    void place_block(){
        Block block = Objects.requireNonNull(MMORPG.Server.getWorld(world_name)).getBlockAt(x, y, z);
        block.setType(Material.getMaterial(material_name));
    }
    void check_world_block_status(){
        Block block = Objects.requireNonNull(MMORPG.Server.getWorld(world_name)).getBlockAt(x, y, z);
        if (!block.getType().name().equals(material_name)){
            if (Objects.equals(passed_ticks, cool_down_ticks)){
                passed_ticks = 0;
                place_block();
            }
        }
    }
    public void break_block(String player_id){
        ArrayList<VarObject> varObjects = new ArrayList<VarObject>();
        varObjects.add(new VarObject("player_id", "STRING", player_id));
        varObjects.add(new VarObject("mine_name", "STRING", name));
        passed_ticks = 0;
        Main.script_PLAYER_MINE_BLOCK_event(varObjects);
    }
}
