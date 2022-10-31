package at.feddis08.mmorpg.io.text_files.files.file_objects;

import at.feddis08.mmorpg.MMORPG;
import at.feddis08.mmorpg.commands.Rank;
import at.feddis08.mmorpg.io.database.Functions;
import at.feddis08.mmorpg.io.database.objects.PlayerObject;
import at.feddis08.mmorpg.logic.scripts.VarObject;
import at.feddis08.mmorpg.minecraft.tools.Methods;
import org.checkerframework.checker.units.qual.A;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
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
    public ArrayList<VarObject> get_value(String arg){
        ArrayList<VarObject> result = new ArrayList<>();
        if (arg.contains("<@v>")){
            result.add(get_var_by_name(arg.split("<@v>")[1]));
        }
        if (arg.contains("<@s>")) result.add(new VarObject("", "STRING", arg.split("<@s>")[1]));
        if (result.size() == 0){
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
            if (Objects.equals(script.get(index).get(0), "<*v>")){
                register_new_var(cmd.get(1), cmd.get(2));
            }
            if (Objects.equals(script.get(index).get(0), "<=v>")){
                change_value_of_var(cmd.get(1), get_value(cmd.get(2)).get(0).value);
            }
            if (Objects.equals(script.get(index).get(0), "<=v,f>")){
                ArrayList<String> args = new ArrayList<>();
                Integer count_c = 0;
                Integer index2 = 0;
                while (index2 < cmd.size()){
                    if (cmd.get(index2).contains(":")){
                        index2 = cmd.size();
                    }else{
                        if (cmd.get(index2).contains("<@v>")){
                            count_c = count_c + 1;
                        }
                    }
                    index2 = index2 + 1;
                }
                index2 = count_c + 1;
                while (index2 < cmd.size()){
                    args.add(cmd.get((index2)));
                    index2 = index2 + 1;
                }
                ArrayList<VarObject> result = new ArrayList<>();
                try {
                    result = execute_functions(args);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                index2 = 0;
                if (result.size() == 0){
                    MMORPG.consoleLog("ERROR: function " + args.get(0) + " didn't returned any values!");
                    MMORPG.consoleLog(result.size() + " " + args.size() + " " + count_c);
                }else {
                    while (index2 < count_c) {
                        if (cmd.get(index2 + 1).contains("<@v>")) {
                            change_value_of_var(cmd.get(index2 + 1).split("<@v>")[1], result.get(index2).value);
                        }
                        index2 = index2 + 1;
                    }
                }
                index2 = 0;

            }
            if (Objects.equals(script.get(index).get(0), "<@v>")){
                get_var_by_name(cmd.get(1));
            }
            if (Objects.equals(script.get(index).get(0), "<?->")){
                Boolean pass = false;
                if (Objects.equals(cmd.get(2), "<_==_>")){
                    if (Objects.equals(get_value(cmd.get(3)).get(0).type, "STRING")){
                        if (Objects.equals(get_value(cmd.get(3)).get(0).value, get_value(cmd.get(4)).get(0).value)) pass = true;
                    }else{
                        if (Integer.parseInt(get_value(cmd.get(3)).get(0).value) == Integer.parseInt(get_value(cmd.get(4)).get(0).value)) pass = true;
                    }
                }
                if (Objects.equals(cmd.get(2), "<_<_>")){
                    if (Integer.parseInt(get_value(cmd.get(3)).get(0).value) < Integer.parseInt(get_value(cmd.get(4)).get(0).value)) pass = true;
                }
                if (Objects.equals(cmd.get(2), "<_>_>")){
                    if (Integer.parseInt(get_value(cmd.get(3)).get(0).value) > Integer.parseInt(get_value(cmd.get(4)).get(0).value)) pass = true;
                }
                if (Objects.equals(cmd.get(2), "<_!=_>")){
                    if (Objects.equals(get_value(cmd.get(3)).get(0).type, "STRING")){
                        if (!(Objects.equals(get_value(cmd.get(3)).get(0).value, get_value(cmd.get(4)).get(0).value))) pass = true;
                    }else{
                        if (!(Integer.parseInt(get_value(cmd.get(3)).get(0).value) == Integer.parseInt(get_value(cmd.get(4)).get(0).value))) pass = true;
                    }
                }
                if (!(pass)) {
                    Integer index2 = index;
                    while (index2 < script.size()) {
                        ArrayList<String> cmd2 = script.get(index2);
                        if (Objects.equals(script.get(index2).get(0), "<-?>") && Objects.equals(script.get(index).get(1), script.get(index2).get(1))) {
                            index = index2;
                        }
                        index2 = index2 + 1;
                    }
                }
            }
            try {
                execute_functions(cmd);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            index = index + 1;
        }
        varObjects.clear();
        t.stop();
    }
    public ArrayList<VarObject> execute_functions(ArrayList<String> args) throws SQLException {
        ArrayList<VarObject> result = new ArrayList<>();
        if (Objects.equals(args.get(0), "rank.create_rank:")){
            Rank.create_rank(get_value(args.get(1)).get(0).value);
        }
        if (Objects.equals(args.get(0), "rank.add_rule:")){
            Rank.add_rule(get_value(args.get(1)).get(0).value, get_value(args.get(2)).get(0).value);
        }
        if (Objects.equals(args.get(0), "rank.set_player_rank:")){
            Rank.set_player_rank_from(get_value(args.get(1)).get(0).value, get_value(args.get(2)).get(0).value);
        }
        if (Objects.equals(args.get(0), "consoleLog:")){
            String str = "[" + name + "]: " + get_value(args.get(1)).get(0).value;
            MMORPG.consoleLog(str);
        }
        if (Objects.equals(args.get(0), "open_inv_on_minecraft_player:")){
            Methods.open_inv_on_minecraft_player(get_value(args.get(1)).get(0).value, get_value(args.get(2)).get(0).value);
        }
        if (Objects.equals(args.get(0), "test:")){
            result.add(new VarObject("", "STRING", "test"));
        }
        if (Objects.equals(args.get(0), "get_player_by_id:")){
            PlayerObject dbPlayer = Functions.getPlayer("id", get_value(args.get(1)).get(0).value);
            result.add(new VarObject("", "STRING", String.valueOf(dbPlayer.id.toString())));
            result.add(new VarObject("", "INTEGER", String.valueOf(dbPlayer.online)));
            result.add(new VarObject("", "STRING", dbPlayer.didStartup));
            result.add(new VarObject("", "STRING", dbPlayer.display_name));
            result.add(new VarObject("", "STRING", dbPlayer.player_name));
            result.add(new VarObject("", "STRING", dbPlayer.player_rank));
        }
        return result;
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
