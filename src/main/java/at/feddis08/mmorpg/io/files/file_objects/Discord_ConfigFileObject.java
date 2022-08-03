package at.feddis08.mmorpg.io.files.file_objects;

import at.feddis08.mmorpg.MMORPG;

import java.util.ArrayList;
import java.util.Objects;

public class Discord_ConfigFileObject {

   public String server_log = "";
   public String read_only_chat = "";
   public String chat = "";

    public void parse_config_file(ArrayList<String> lines) {
        MMORPG.debugLog("Parsing discord_config file ...");
        Integer index = 0;
        Boolean parse_ok = false;
        while ((index + 1) <= lines.size()) {
            parse_ok = false;
            String line = lines.get(index);
            String[] params = line.split(" ");

            if (Objects.equals(params[0], "#")) {
                parse_ok = true;
            }
            if (Objects.equals(params[0], "")) {
                parse_ok = true;
            }
            if (Objects.equals(params[0], "server_log:")) {
                if (params.length == 2){
                    server_log = params[1];
                    parse_ok = true;
                }else{
                    parse_ok = true;
                }
            }
            if (Objects.equals(params[0], "read_only_chat:")) {
                if (params.length == 2){
                    read_only_chat = params[1];
                    parse_ok = true;
                }else{
                    parse_ok = true;
                }
            }
            if (Objects.equals(params[0], "chat:")) {
                if (params.length == 2){
                    chat = params[1];
                    parse_ok = true;
                }else{
                    parse_ok = true;
                }
            }
        index = index + 1;
        if (!(parse_ok))
            MMORPG.consoleLog("ERROR: Could not parse config file. Load default value. Error at line: " + String.valueOf(index));

        }
    }
}
