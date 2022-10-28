package at.feddis08.mmorpg.logic.scripts;

import at.feddis08.mmorpg.io.text_files.files.Main;
import at.feddis08.mmorpg.io.text_files.files.file_objects.ScriptFileObject;
import at.feddis08.mmorpg.io.text_files.files.file_objects.ScriptsFileObject;

import java.util.ArrayList;

public class Var {

    public static ArrayList<ScriptFileObject> scripts = new ArrayList<>();
    public static ScriptsFileObject config;
    public static String path = Main.path + "Scripts/";

}
