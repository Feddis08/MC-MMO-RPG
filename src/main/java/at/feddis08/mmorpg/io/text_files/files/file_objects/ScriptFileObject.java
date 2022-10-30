package at.feddis08.mmorpg.io.text_files.files.file_objects;

import at.feddis08.mmorpg.MMORPG;
import at.feddis08.mmorpg.commands.Rank;
import at.feddis08.mmorpg.logic.scripts.VarObject;

import java.util.ArrayList;
import java.util.Objects;

public class ScriptFileObject extends Thread {

    public ArrayList<ArrayList<String>> script = new ArrayList<>();
    public String name = "";
    public String start_event = "SERVER_START";
    public ArrayList<VarObject> varObjects = new ArrayList<>();
    public Thread t = new Thread("script");

    public void run(){
        MMORPG.consoleLog("dd");
        start();
    }

    public void register_new_var(String name, String type){
        Integer index = 0;
        Boolean result = false;
        while (index < varObjects.size()){
            if (Objects.equals(varObjects.get(index).name, name)){
                result = true;
            }
            index = index + 1;
        }
        if (result){
            MMORPG.consoleLog("[" + this.name + "]: ERROR: Var " + name + " is already defined!");
        }else{
            varObjects.add(new VarObject(name, type, ""));
        }
    }
    public VarObject get_var_by_name(String name){
        Integer index = 0;
        VarObject result = null;
        while (index < varObjects.size()){
            if (Objects.equals(varObjects.get(index).name, name)){
                result = varObjects.get(index);
            }
            index = index + 1;
        }
        if (result == null){
            MMORPG.consoleLog("[" + this.name + "]: ERROR: Var " + name + " is not defined!");
        }
        return result;
    }
    public VarObject get_value(String arg){
        VarObject result = null;
        if (arg.contains("<@v>")){
            result = get_var_by_name(arg.split("<@v>")[1]);
        }else{
            if (arg.contains("<@s>")) result = new VarObject("", "STRING", arg.split("<@s>")[1]);
        }
        return result;
    }
    public Boolean change_value_of_var(String name, String value){
        Integer index = 0;
        Boolean result = false;
        while (index < varObjects.size()){
            if (Objects.equals(varObjects.get(index).name, name)){
                result = true;
                varObjects.get(index).value = value;
            }
            index = index + 1;
        }
        if (!(result)){
            MMORPG.consoleLog("[" + this.name + "]: ERROR: Var " + name + " is not defined!");
        }
        return result;
    }
    public void start(){
        MMORPG.consoleLog("Enabling script: " + name);
        Integer index = 0;
        while (index < script.size()){
            ArrayList<String> cmd = script.get(index);
            if (Objects.equals(script.get(index).get(0), "<@@@v>")){
                register_new_var(cmd.get(1), cmd.get(2));
            }
            if (Objects.equals(script.get(index).get(0), "<@@v>")){
                change_value_of_var(cmd.get(1), get_value(cmd.get(2)).value);
            }
            if (Objects.equals(script.get(index).get(0), "<@v>")){
                get_var_by_name(cmd.get(1));
            }
            if (Objects.equals(script.get(index).get(0), "<if>")){
                Boolean pass = false;
                if (Objects.equals(cmd.get(1), "<_==_>")){
                    if (Objects.equals(get_value(cmd.get(2)).type, "STRING")){
                        if (Objects.equals(get_value(cmd.get(2)).value, get_value(cmd.get(3)).value)) pass = true;
                    }else{
                        if (Integer.parseInt(get_value(cmd.get(2)).value) == Integer.parseInt(get_value(cmd.get(3)).value)) pass = true;
                    }
                }
                if (Objects.equals(cmd.get(1), "<_<_>")){
                    if (Integer.parseInt(get_value(cmd.get(2)).value) < Integer.parseInt(get_value(cmd.get(3)).value)) pass = true;
                }
                if (Objects.equals(cmd.get(1), "<_>_>")){
                    if (Integer.parseInt(get_value(cmd.get(2)).value) > Integer.parseInt(get_value(cmd.get(3)).value)) pass = true;
                }
                if (Objects.equals(cmd.get(1), "<_!=_>")){
                    if (Objects.equals(get_value(cmd.get(2)).type, "STRING")){
                        if (!(Objects.equals(get_value(cmd.get(2)).value, get_value(cmd.get(3)).value))) pass = true;
                    }else{
                        if (!(Integer.parseInt(get_value(cmd.get(2)).value) == Integer.parseInt(get_value(cmd.get(3)).value))) pass = true;
                    }
                }
                if (!(pass)) {
                    Integer index2 = index;
                    while (index2 < script.size()) {
                        ArrayList<String> cmd2 = script.get(index2);
                        if (Objects.equals(script.get(index2).get(0), "<if end>")) {
                            index = index2;
                        }
                        index2 = index2 + 1;
                    }
                }
            }
            if (Objects.equals(script.get(index).get(0), "rank.create_rank:")){
                Rank.create_rank(get_value(cmd.get(1)).value);
            }
            if (Objects.equals(script.get(index).get(0), "rank.add_rule:")){
                Rank.add_rule(get_value(cmd.get(1)).value, get_value(cmd.get(2)).value);
            }
            if (Objects.equals(script.get(index).get(0), "rank.set_player_rank:")){
                Rank.set_player_rank_from(get_value(cmd.get(1)).value, get_value(cmd.get(2)).value);
            }
            if (Objects.equals(script.get(index).get(0), "consoleLog:")){
                String str = "[" + name + "]: " + get_value(cmd.get(1)).value;
                MMORPG.consoleLog(str);
            }
            index = index + 1;
        }
        varObjects.clear();
        t.stop();
    }

    public void parse_config_file(ArrayList<String> lines) {
        MMORPG.debugLog("Parsing Scripts file ...");
        Integer index = 0;
        Boolean parse_ok = false;
        while ((index + 1) <= lines.size()) {
            parse_ok = false;
            String line = lines.get(index);
            String[] params = line.split("__");
            if (Objects.equals(params[0], "#")) {
                parse_ok = true;
            }
            if (Objects.equals(params[0], "")) {
                parse_ok = true;
            }
            if (Objects.equals(params[0], "<SCRIPT_NAME>")) {
                if (params.length == 2){
                    name = params[1];
                    parse_ok = true;
                }else{
                    parse_ok = true;
                    MMORPG.consoleLog("ERROR: Could not parse config file. Missing argument. Load default value. Error at line: " + String.valueOf(index + 1));
                }
            }
            if (Objects.equals(params[0], "<START_EVENT>")) {
                if (params.length == 2){
                    start_event = params[1];
                    parse_ok = true;
                }else{
                    parse_ok = true;
                    MMORPG.consoleLog("ERROR: Could not parse config file. Missing argument. Load default value. Error at line: " + String.valueOf(index + 1));
                }
            }
            if (Objects.equals(params[0], "<$>")) {
                ArrayList<String> command = new ArrayList<>();
                Integer index2 = 1;
                while (index2 < params.length){
                    command.add(params[index2]);
                    index2 = index2 + 1;
                }
                script.add(command);
                parse_ok = true;
            }
            index = index + 1;
            if (!(parse_ok))
                MMORPG.consoleLog("ERROR: Could not parse config file. Load default value. Error at line: " + String.valueOf(index));

        }
    }
}
