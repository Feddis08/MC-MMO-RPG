package at.feddis08.mmorpg.logic.scripts;

import at.feddis08.mmorpg.io.text_files.files.Main;
import at.feddis08.mmorpg.io.text_files.files.file_objects.ScriptFileObject;
import at.feddis08.mmorpg.io.text_files.files.file_objects.ScriptsFileObject;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Objects;

public class Var {
    public static ArrayList<ScriptFileObject> scripts = new ArrayList<>();
    public static ScriptsFileObject config;
    public static String path = Main.path + "Scripts/";
    public static ArrayList<Var_pool> var_pools = new ArrayList<>();
    public static Var_pool get_var_pool_by_name(String name){
        Integer index = 0;
        Var_pool result = null;
        while (index < var_pools.size()){
            if (Objects.equals(var_pools.get(index).name, name)){
                result = var_pools.get(index);
            }
            index = index + 1;
        }
        return result;
    }

}
