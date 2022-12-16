package at.feddis08.mmorpg.logic.game.mob_spawner;

import at.feddis08.mmorpg.MMORPG;

import java.util.ArrayList;
import java.util.Objects;

public class Main {
    public static ArrayList<Spawner> spawners = new ArrayList<>();
    public static void clear_spawners(){
        Integer index = 0;
        while (index < spawners.size()){
            spawners.get(index).clear_spawner();
            index += 1;
        }
    }
    public static void check_spawners_per_tick(){
        Integer index = 0;
        while (index < spawners.size()){
            spawners.get(index).check();
            index += 1;
        }
    }
    public static Spawner get_spawner_by_name(String name){
        Integer index = 0;
        Spawner result = null;
        while (index < spawners.size()){
            if (Objects.equals(spawners.get(index).name, name))
                result = spawners.get(index);
            index += 1;
        }
        return result;
    }
}
