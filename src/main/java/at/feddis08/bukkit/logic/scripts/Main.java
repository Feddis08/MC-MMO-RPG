package at.feddis08.bukkit.logic.scripts;

import at.feddis08.Boot;
import at.feddis08.bukkit.cluster_com_client.Start_cluster_client;
import at.feddis08.tools.io.text_files.files.ReadFile;
import at.feddis08.tools.io.text_files.files.config_patterns.CheckScriptsFile;
import at.feddis08.tools.io.text_files.files.file_objects.ScriptFileObject;
import com.google.gson.Gson;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class Main {

    public static Boolean run_AFTER_PLAYER_JOINED = false;
    public static ArrayList<VarObject> vars_AFTER_PLAYER_JOINED = new ArrayList<>();
    public static Boolean run_AFTER_PLAYER_CLICK_EVENT = false;
    public static ArrayList<VarObject> vars_AFTER_PLAYER_CLICK_EVENT = new ArrayList<>();

    public static void reload_all() throws IOException {
        run_AFTER_PLAYER_JOINED = false;
        run_AFTER_PLAYER_CLICK_EVENT = false;
        vars_AFTER_PLAYER_JOINED = new ArrayList<>();
        vars_AFTER_PLAYER_CLICK_EVENT = new ArrayList<>();
        Var.scripts = new ArrayList<>();
        Var.var_pools = new ArrayList<>();
        CheckScriptsFile.check();
        try {
            start();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void check_all_after_events() throws IOException, InterruptedException {
        if (run_AFTER_PLAYER_CLICK_EVENT){
            script_start_by_event_name ("AFTER_PLAYER_CLICK_ENTITY", vars_AFTER_PLAYER_CLICK_EVENT, false);
            run_AFTER_PLAYER_CLICK_EVENT = false;
        }
        if (run_AFTER_PLAYER_JOINED){
            script_start_by_event_name ("PLAYER_JOINED", vars_AFTER_PLAYER_JOINED, false);
            run_AFTER_PLAYER_JOINED = false;
        }
    }
    public static void start() throws IOException, InterruptedException {
        Boot.debugLog("Start parsing scripts...");
        parse_scripts();
        Boot.consoleLog("Starting scripts by SERVER_START event...");
        ArrayList<VarObject> varObjects = new ArrayList<VarObject>();
        script_start_by_event_name ("SERVER_START", varObjects, false);

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
    public static void script_PLAYER_BOUGHT_AT_SHOP_event(ArrayList<VarObject> varObjects){
        Integer index = 0;
        while (index < Var.scripts.size()){
            ScriptFileObject scriptFileObject = Var.scripts.get(index);
            ArrayList<VarObject> safe_varObjects = (ArrayList<VarObject>) varObjects.clone();
            if (Objects.equals(scriptFileObject.start_event, "PLAYER_BOUGHT_AT_SHOP")){
                scriptFileObject.varObjects = safe_varObjects;
                scriptFileObject.start();
            }
            index = index + 1;
        }
    }
    public static void script_PLAYER_SOLD_AT_SHOP_event(ArrayList<VarObject> varObjects){
        Integer index = 0;
        while (index < Var.scripts.size()){
            ScriptFileObject scriptFileObject = Var.scripts.get(index);
            ArrayList<VarObject> safe_varObjects = (ArrayList<VarObject>) varObjects.clone();
            if (Objects.equals(scriptFileObject.start_event, "PLAYER_SOLD_AT_SHOP")){
                scriptFileObject.varObjects = safe_varObjects;
                scriptFileObject.start();
            }
            index = index + 1;
        }
    }
    public static void script_start_by_event_name(String event_name, ArrayList<VarObject> varObjects, boolean called_by_other_server) throws InterruptedException, IOException {
        Integer index = 0;
        if (Boot.is_bungee){

        }else if(Start_cluster_client.client != null){
            if (Objects.equals(event_name, "TICK_START")){
                while (index < Var.scripts.size()){
                    ScriptFileObject scriptFileObject = Var.scripts.get(index);
                    ArrayList<VarObject> safe_varObjects = (ArrayList<VarObject>) varObjects.clone();
                    safe_varObjects.add(new VarObject("is_from_other_server", "STRING", "FALSE"));
                    if (Objects.equals(scriptFileObject.start_event, event_name)){
                        scriptFileObject.varObjects = safe_varObjects;
                        scriptFileObject.start();
                    }
                    index = index + 1;
                }
            }else{
                JSONObject json = new JSONObject();
                if (!called_by_other_server){
                    Gson gson = new Gson();
                    json.put("script_event_name", event_name);
                    json.put("varObjects", gson.toJson(varObjects));
                    Start_cluster_client.client.send_event(json, "script_event_triggered");
                    json = Start_cluster_client.client.wait_for_response(json);
                    Boot.consoleLog("wdwd22");
                }else{
                    json.put("status", "ok");
                    json.put("event_start", true);
                    Boot.consoleLog("2398t45390");
                }
                if (Objects.equals(json.getString("status"), "ok") && json.getBoolean("event_start")){
                    while (index < Var.scripts.size()){
                        ScriptFileObject scriptFileObject = Var.scripts.get(index);
                        if (called_by_other_server && scriptFileObject.listen_to_others_events){
                            ArrayList<VarObject> safe_varObjects = (ArrayList<VarObject>) varObjects.clone();
                            safe_varObjects.add(new VarObject("is_from_other_server", "STRING", "TRUE"));
                            if (Objects.equals(scriptFileObject.start_event, event_name)){
                                scriptFileObject.varObjects = safe_varObjects;
                                scriptFileObject.start();
                            }
                        }else if (!called_by_other_server){
                            ArrayList<VarObject> safe_varObjects = (ArrayList<VarObject>) varObjects.clone();
                            safe_varObjects.add(new VarObject("is_from_other_server", "STRING", "FALSE"));
                            if (Objects.equals(scriptFileObject.start_event, event_name)){
                                scriptFileObject.varObjects = safe_varObjects;
                                scriptFileObject.start();
                            }
                        }
                        index = index + 1;
                    }
                }
            }
        }
    }
    public static void script_SERVER_STOP_event(ArrayList<VarObject> varObjects){
        Integer index = 0;
        while (index < Var.scripts.size()){
            ScriptFileObject scriptFileObject = Var.scripts.get(index);
            ArrayList<VarObject> safe_varObjects = (ArrayList<VarObject>) varObjects.clone();
            if (Objects.equals(scriptFileObject.start_event, "SERVER_STOP")){
                scriptFileObject.varObjects = safe_varObjects;
                scriptFileObject.start();
            }
            index = index + 1;
        }
    }
    public static void script_TICK_START_event(ArrayList<VarObject> varObjects){
        Integer index = 0;
        while (index < Var.scripts.size()){
            ScriptFileObject scriptFileObject = Var.scripts.get(index);
            ArrayList<VarObject> safe_varObjects = (ArrayList<VarObject>) varObjects.clone();
            if (Objects.equals(scriptFileObject.start_event, "TICK_START")){
                scriptFileObject.varObjects = safe_varObjects;
                scriptFileObject.start();
            }
            index = index + 1;
        }
    }
    public static void script_PLAYER_CLICK_ENTITY_event(ArrayList<VarObject> varObjects){
        Integer index = 0;
        while (index < Var.scripts.size()){
            ScriptFileObject scriptFileObject = Var.scripts.get(index);
            ArrayList<VarObject> safe_varObjects = (ArrayList<VarObject>) varObjects.clone();
            if (Objects.equals(scriptFileObject.start_event, "PLAYER_CLICK_ENTITY")){
                scriptFileObject.varObjects = safe_varObjects;
                scriptFileObject.start();
            }
            index = index + 1;
        }
    }
    public static void script_PLAYER_KILL_MOB_FROM_SPAWNER_event(ArrayList<VarObject> varObjects){
        Integer index = 0;
        while (index < Var.scripts.size()){
            ScriptFileObject scriptFileObject = Var.scripts.get(index);
            ArrayList<VarObject> safe_varObjects = (ArrayList<VarObject>) varObjects.clone();
            if (Objects.equals(scriptFileObject.start_event, "PLAYER_KILL_MOB_FROM_SPAWNER")){
                scriptFileObject.varObjects = safe_varObjects;
                scriptFileObject.start();
            }
            index = index + 1;
        }
    }
    public static void script_PLAYER_TELEPORTED_BY_PORTAL_event(ArrayList<VarObject> varObjects){
        Integer index = 0;
        while (index < Var.scripts.size()){
            ScriptFileObject scriptFileObject = Var.scripts.get(index);
            ArrayList<VarObject> safe_varObjects = (ArrayList<VarObject>) varObjects.clone();
            if (Objects.equals(scriptFileObject.start_event, "PLAYER_TELEPORTED_BY_PORTAL")){
                scriptFileObject.varObjects = safe_varObjects;
                scriptFileObject.start();
            }
            index = index + 1;
        }
    }
    public static void script_PLAYER_JOINED_event(ArrayList<VarObject> varObjects){
        Integer index = 0;
        while (index < Var.scripts.size()){
            ScriptFileObject scriptFileObject = Var.scripts.get(index);
            ArrayList<VarObject> safe_varObjects = (ArrayList<VarObject>) varObjects.clone();
            if (Objects.equals(scriptFileObject.start_event, "PLAYER_JOINED")){
                scriptFileObject.varObjects = safe_varObjects;
                scriptFileObject.start();
            }
            index = index + 1;
        }
    }
    public static void script_PLAYER_MINE_BLOCK_event(ArrayList<VarObject> varObjects){
        Integer index = 0;
        while (index < Var.scripts.size()){
            ScriptFileObject scriptFileObject = Var.scripts.get(index);
            ArrayList<VarObject> safe_varObjects = (ArrayList<VarObject>) varObjects.clone();
            if (Objects.equals(scriptFileObject.start_event, "PLAYER_MINE_BLOCK")){
                scriptFileObject.varObjects = safe_varObjects;
                scriptFileObject.start();
            }
            index = index + 1;
        }
    }
    public static void script_PLAYER_DEATH_event(ArrayList<VarObject> varObjects){
        Integer index = 0;
        while (index < Var.scripts.size()){
            ScriptFileObject scriptFileObject = Var.scripts.get(index);
            ArrayList<VarObject> safe_varObjects = (ArrayList<VarObject>) varObjects.clone();
            if (Objects.equals(scriptFileObject.start_event, "PLAYER_DEATH")){
                scriptFileObject.varObjects = safe_varObjects;
                scriptFileObject.start();
            }
            index = index + 1;
        }
    }
    public static void script_AFTER_PLAYER_CLICK_ENTITY_event(ArrayList<VarObject> varObjects){
        Integer index = 0;
        while (index < Var.scripts.size()){
            ScriptFileObject scriptFileObject = Var.scripts.get(index);
            ArrayList<VarObject> safe_varObjects = (ArrayList<VarObject>) varObjects.clone();
            if (Objects.equals(scriptFileObject.start_event, "AFTER_PLAYER_CLICK_ENTITY")){
                scriptFileObject.varObjects = safe_varObjects;
                scriptFileObject.start();
            }
            index = index + 1;
        }
    }
}
