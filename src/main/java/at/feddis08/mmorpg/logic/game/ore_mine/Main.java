package at.feddis08.mmorpg.logic.game.ore_mine;

import java.util.ArrayList;
import java.util.Objects;

public class Main {
    public static ArrayList<Mine> mines = new ArrayList<>();

    public static void check_mines_per_tick(){
        Integer index = 0;
        while (index < mines.size()){
            mines.get(index).check();
        }
    }
    public static Mine get_mine_by_name(String name){
        Integer index = 0;
        Mine result = null;
        while (index < mines.size()){
            if (Objects.equals(mines.get(index).name, name))
                result = mines.get(index);
            index += 1;
        }
        return result;
    }
    public static Mine get_mine_by_cords(String world_name, Integer x, Integer y, Integer z){
        Integer index = 0;
        Mine result = null;
        while (index < mines.size()){
            boolean not_passed = false;
            if (!Objects.equals(mines.get(index).world_name, world_name))
                not_passed = true;
            if (!Objects.equals(mines.get(index).x, x))
                not_passed = true;
            if (!Objects.equals(mines.get(index).y, y))
                not_passed = true;
            if (!Objects.equals(mines.get(index).z, z))
                not_passed = true;
            if (!not_passed)
                result = mines.get(index);
            index += 1;
        }
        return result;
    }
}
