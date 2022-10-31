package at.feddis08.mmorpg.logic.scripts;

import at.feddis08.mmorpg.MMORPG;
import at.feddis08.mmorpg.io.text_files.files.ReadFile;
import at.feddis08.mmorpg.io.text_files.files.file_objects.ScriptFileObject;
import at.feddis08.mmorpg.io.text_files.files.file_objects.ScriptsFileObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class Main {
    public static void start() throws IOException {
        MMORPG.debugLog("Start parsing scripts...");
        parse_scripts();
        MMORPG.consoleLog("Starting scripts by SERVER_START event...");
        ArrayList<VarObject> varObjects = new ArrayList<VarObject>();
        script_SERVER_START_event(varObjects);

    }
    public static void parse_scripts() throws IOException {
       Integer index = 0;
       while (index < Var.config.scripts.size()){
           ScriptFileObject scriptFileObject = new ScriptFileObject();
           scriptFileObject.parse_config_file(ReadFile.getFile(Var.path + Var.config.scripts.get(index)));
           Var.scripts.add(scriptFileObject);
           index = index + 1;
       }
    }
    public static void script_SERVER_START_event(ArrayList<VarObject> varObjects){
        Integer index = 0;
        while (index < Var.scripts.size()){
            ScriptFileObject scriptFileObject = Var.scripts.get(index);
            if (Objects.equals(scriptFileObject.start_event, "SERVER_START")){
                scriptFileObject.varObjects = varObjects;
                scriptFileObject.start();
            }
            index = index + 1;
        }
    }
    public static void script_SERVER_STOP_event(ArrayList<VarObject> varObjects){
        Integer index = 0;
        while (index < Var.scripts.size()){
            ScriptFileObject scriptFileObject = Var.scripts.get(index);
            if (Objects.equals(scriptFileObject.start_event, "SERVER_STOP")){
                scriptFileObject.varObjects = varObjects;
                scriptFileObject.start();
            }
            index = index + 1;
        }
    }
    public static void script_TICK_START_event(ArrayList<VarObject> varObjects){
        Integer index = 0;
        while (index < Var.scripts.size()){
            ScriptFileObject scriptFileObject = Var.scripts.get(index);
            if (Objects.equals(scriptFileObject.start_event, "TICK_START")){
                scriptFileObject.varObjects = varObjects;
                scriptFileObject.start();
            }
            index = index + 1;
        }
    }
    public static void script_PLAYER_CLICK_ENTITY_event(ArrayList<VarObject> varObjects){
        Integer index = 0;
        while (index < Var.scripts.size()){
            ScriptFileObject scriptFileObject = Var.scripts.get(index);
            if (Objects.equals(scriptFileObject.start_event, "PLAYER_CLICK_ENTITY")){
                scriptFileObject.varObjects = varObjects;
                scriptFileObject.start();
            }
            index = index + 1;
        }
    }
}
