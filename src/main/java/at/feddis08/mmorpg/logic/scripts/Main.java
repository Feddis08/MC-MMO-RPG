package at.feddis08.mmorpg.logic.scripts;

import at.feddis08.mmorpg.MMORPG;
import at.feddis08.mmorpg.io.text_files.files.ReadFile;
import at.feddis08.mmorpg.io.text_files.files.file_objects.ScriptFileObject;
import at.feddis08.mmorpg.io.text_files.files.file_objects.ScriptsFileObject;

import java.io.IOException;
import java.util.Objects;

public class Main {
    public static void start() throws IOException {
        MMORPG.debugLog("Start parsing scripts...");
        parse_scripts();
        MMORPG.consoleLog("Starting scripts...");
        script_SERVER_START_event();

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
    public static void script_SERVER_START_event(){
        Integer index = 0;
        while (index < Var.scripts.size()){
            ScriptFileObject scriptFileObject = Var.scripts.get(index);
            if (Objects.equals(scriptFileObject.start_event, "SERVER_START")){
                scriptFileObject.start();
            }
            index = index + 1;
        }
    }
}
