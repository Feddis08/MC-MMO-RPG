package at.feddis08.tools.io.text_files.files.file_objects;

import at.feddis08.Boot;
import at.feddis08.bukkit.logic.scripts.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;


public class ScriptFileObject extends Thread {

    public ArrayList<ArrayList<String>> script = new ArrayList<>();
    public String name = "";
    public String start_event = "SERVER_START";
    public ArrayList<VarObject> varObjects = new ArrayList<>();
    public Thread t = new Thread("script");
    public Boolean error = false;
    public Boolean show_enabling_message = true;
    public String author = "";
    public Integer current_line = 0;
    public boolean listen_to_others_events = false;
    public void run(){
        Boot.consoleLog("dd");
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
            error = true;
            throw_error("[" + this.name + "]: ERROR: Var " + name + " is already defined!", current_line);
        }else{
            if (Objects.equals(type, "ARRAY")){
                varObjects.add(new ArrayObject(name, new ArrayList<VarObject>()));
            }else{
                varObjects.add(new VarObject(name, type, ""));
            }
        }
    }
    public void add_value_to_array(String array_name, String str){
        ArrayObject arrayObject = (ArrayObject) get_var_by_name(array_name);
        arrayObject.varList.add(get_value(str).get(0));
    }
    public VarObject get_var_by_name(String var_name){
        Integer index = 0;
        VarObject result = null;
        while (index < varObjects.size()){
            if (Objects.equals(varObjects.get(index).name, var_name)){
                result = varObjects.get(index);
            }
            index = index + 1;
        }
        if (result == null){
            error = true;
            throw_error("[" + this.name + "]: ERROR: Var " + var_name + " is not defined!", current_line);
        }
        return result;
    }
    public ArrayList<VarObject> get_value(String arg){
        ArrayList<VarObject> result = new ArrayList<>();
        if (arg.contains("<@va>")){
            ArrayObject arrayObject = (ArrayObject) get_var_by_name(arg.split("<@va>")[1]);
            result.add(arrayObject.varList.get(Integer.parseInt (get_value(arg.split("]")[1]).get(0).value)));
        }
        if (arg.contains("<@v>")){
            result.add(get_var_by_name(arg.split("<@v>")[1]));
        }
        if (arg.contains("<@s>")) result.add(new VarObject("", "STRING", arg.split("<@s>")[1]));
        if (result.size() == 0){
        }
        return result;
    }
    public Boolean change_value_of_var(String name, String value, Integer i){
        Integer index = 0;
        Boolean result = false;
        while (index < varObjects.size()){
            if (Objects.equals(varObjects.get(index).name, name)){
                result = true;
                if (Objects.equals(varObjects.get(index).type, "ARRAY")){
                    ArrayObject arrayObject = (ArrayObject) varObjects.get(index);
                    arrayObject.varList.get(i).value = String.valueOf(i);
                }else{
                    varObjects.get(index).value = value;
                }
            }
            index = index + 1;
        }
        if (!(result)){
            error = true;
            throw_error("Var " + name + " is not defined!", current_line);
        }
        return result;
    }
    public void throw_error(String error, Integer line){
        Boot.consoleLog(varObjects.toString());
        varObjects.clear();
        error = String.valueOf(true);
        String str = "[" + name + "]: " + error + "[in line]: " + line;
        Boot.consoleLog(str);
    }
    public void start(){
        int index = 0;
        current_line = 0;
        if (!error) {
            Boot.consoleLog("Enabling script: " + name);
            while (index < script.size()) {
                current_line ++;
                ArrayList<String> cmd = script.get(index);
                if (error) {
                    throw_error ("Script " + name + " stopped! Error at line: ", index);
                    index = script.size() - 1;
                    break;
                }
                if (Objects.equals(script.get(index).get(0), "<*va>")) {
                    register_new_var(cmd.get(1), cmd.get(2));
                }
                if (Objects.equals(script.get(index).get(0), "<+va>")) {
                    add_value_to_array(cmd.get(1), cmd.get(2));
                }
                if (Objects.equals(script.get(index).get(0), "<!va>")) {
                    ArrayObject arrayObject = (ArrayObject) get_var_by_name(cmd.get(1));
                    arrayObject.varList.clear();
                }
                if (Objects.equals(script.get(index).get(0), "<*v>")) {
                    register_new_var(cmd.get(1), cmd.get(2));
                }
                if (Objects.equals(script.get(index).get(0), "<=v>")) {
                    change_value_of_var(cmd.get(1), get_value(cmd.get(2)).get(0).value, 0);
                }
                if (Objects.equals(script.get(index).get(0), "<=va>")) {
                    change_value_of_var(cmd.get(1), get_value(cmd.get(2)).get(0).value, Integer.parseInt(get_value(cmd.get(3)).get(0).value));
                }
                if (Objects.equals(script.get(index).get(0), "<=v,f>")) {
                    ArrayList<String> args = new ArrayList<>();
                    Integer count_c = 0;
                    Integer index2 = 0;
                    while (index2 < cmd.size()) {
                        if (cmd.get(index2).contains(":")) {
                            index2 = cmd.size();
                        } else {
                            if (cmd.get(index2).contains("<@v>")) {
                                count_c = count_c + 1;
                            }
                        }
                        index2 = index2 + 1;
                    }
                    index2 = count_c + 1;
                    while (index2 < cmd.size()) {
                        args.add(cmd.get((index2)));
                        index2 = index2 + 1;
                    }
                    ArrayList<VarObject> result = new ArrayList<>();
                    try {
                        result = new Executor().execute_functions(args, index, this);
                        if (result.size() == 0 && !Boot.is_bungee){
                            result = Executor_for_bukkit.execute_functions(args, index, this);
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    index2 = 0;
                    if (result.size() == 0) {
                        error = true;
                        throw_error("ERROR: function " + args.get(0) + " didn't returned any values!", current_line);
                        Boot.consoleLog(result.size() + " " + args.size() + " " + count_c);
                    } else {
                        while (index2 < count_c) {
                            if (cmd.get(index2 + 1).contains("<@v>")) {
                                change_value_of_var(cmd.get(index2 + 1).split("<@v>")[1], result.get(index2).value, 0);
                            }
                            index2 = index2 + 1;
                        }
                    }
                    index2 = 0;

                }
                if (Objects.equals(script.get(index).get(0), "<@v>")) {
                    get_var_by_name(cmd.get(1));
                }
                if (Objects.equals(script.get(index).get(0), "<?->")) {
                    Boolean pass = false;
                    if (Objects.equals(cmd.get(2), "<_==_>")) {
                        if (Objects.equals(get_value(cmd.get(3)).get(0).type, "STRING")) {
                            if (Objects.equals(get_value(cmd.get(3)).get(0).value, get_value(cmd.get(4)).get(0).value))
                                pass = true;
                        } else {
                            if (Integer.parseInt(get_value(cmd.get(3)).get(0).value) == Integer.parseInt(get_value(cmd.get(4)).get(0).value))
                                pass = true;
                        }
                    }
                    if (Objects.equals(cmd.get(2), "<_<_>")) {
                        if (Integer.parseInt(get_value(cmd.get(3)).get(0).value) < Integer.parseInt(get_value(cmd.get(4)).get(0).value))
                            pass = true;
                    }
                    if (Objects.equals(cmd.get(2), "<_>_>")) {
                        if (Integer.parseInt(get_value(cmd.get(3)).get(0).value) > Integer.parseInt(get_value(cmd.get(4)).get(0).value))
                            pass = true;
                    }
                    if (Objects.equals(cmd.get(2), "<_!=_>")) {
                        if (Objects.equals(get_value(cmd.get(3)).get(0).type, "STRING")) {
                            if (!(Objects.equals(get_value(cmd.get(3)).get(0).value, get_value(cmd.get(4)).get(0).value)))
                                pass = true;
                        } else {
                            if (!(Integer.parseInt(get_value(cmd.get(3)).get(0).value) == Integer.parseInt(get_value(cmd.get(4)).get(0).value)))
                                pass = true;
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
                    new Executor().execute_functions(cmd, index, this);
                    if (!Boot.is_bungee){
                        Executor_for_bukkit.execute_functions(cmd, index, this);
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                index = index + 1;
            }
        }
        varObjects.clear();
        //t.stop();
    }


    public void parse_config_file(ArrayList<String> lines) {
        Boot.debugLog("Parsing Script file ...");
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
            if (Objects.equals(params[0], "<AUTHOR>")) {
                if (params.length == 2){
                    author = params[1];
                    parse_ok = true;
                }else{
                    parse_ok = true;
                    Boot.consoleLog("ERROR: Could not parse script file. Missing argument. Load default value. Error at line: " + String.valueOf(index + 1));
                }
            }
            if (Objects.equals(params[0], "<LISTEN_TO_OTHERS_EVENTS>")) {
                if (params.length == 1){
                    listen_to_others_events = true;
                    parse_ok = true;
                }else{
                    parse_ok = true;
                    Boot.consoleLog("ERROR: Could not parse script file. Missing argument. Load default value. Error at line: " + String.valueOf(index + 1));
                }
            }
            if (Objects.equals(params[0], "<SHOW_ENABLING_MESSAGE>")) {
                if (params.length == 1){
                    show_enabling_message = true;
                    parse_ok = true;
                }else{
                    parse_ok = true;
                    Boot.consoleLog("ERROR: Could not parse script file. Missing argument. Load default value. Error at line: " + String.valueOf(index + 1));
                }
            }
            if (Objects.equals(params[0], "<SCRIPT_NAME>")) {
                if (params.length == 2){
                    name = params[1];
                    parse_ok = true;
                }else{
                    parse_ok = true;
                        Boot.consoleLog("ERROR: Could not parse script file. Missing argument. Load default value. Error at line: " + String.valueOf(index + 1));
                }
            }
            if (Objects.equals(params[0], "<START_EVENT>")) {
                if (params.length == 2){
                    start_event = params[1];
                    parse_ok = true;
                }else{
                    parse_ok = true;
                        Boot.consoleLog("ERROR: Could not parse script file. Missing argument. Load default value. Error at line: " + String.valueOf(index + 1));
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
                Boot.consoleLog("ERROR: Could not parse script file. Missing argument. Load default value. Error at line: " + String.valueOf(index + 1));

        }
        Boot.consoleLog("[" + name +"]: By " + author + ", parsed and loaded!");
    }
}
