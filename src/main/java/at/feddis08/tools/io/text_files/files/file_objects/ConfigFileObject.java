package at.feddis08.tools.io.text_files.files.file_objects;

import java.util.ArrayList;
import java.util.Objects;

public class ConfigFileObject {
    public String console_prefix = "MMO-RPG";
    public Boolean enable_debug_log = false;
    public String database_ip = "localhost";
    public String database_port = "3306";
    public String database_database_name = "MMORPG";
    public String database_username = "MMORPG";
    public String database_password = "database_password";
    public Boolean enable_discord_bot = false;
    public String discord_bot_token = "";
    public boolean is_in_network = false;
    public int network_port = 25564;
    public String network_master_ip = "localhost";
    public String node_token = "1234567890";
    public String server_name = "lobby";
    public boolean allow_entity_spawning = true;

    public void parse_config_file(ArrayList<String> lines){
        System.out.println("Parsing config file ...");
        Integer index = 0;
        Boolean parse_ok = false;
        while ((index + 1) <= lines.size()){
            parse_ok = false;
            String line = lines.get(index);
            String[] params = line.split(" ");

            if (Objects.equals(params[0], "#")){
                parse_ok = true;
            }
            if (Objects.equals(params[0], "")){
                parse_ok = true;
            }
            if (Objects.equals(params[0], "console_prefix:")){
                console_prefix = params[1];
                parse_ok = true;
            }
            if (Objects.equals(params[0], "enable_debug_log:")){
                if (Objects.equals(params[1], "true")){
                    enable_debug_log = true;
                    parse_ok = true;
                }
                if (Objects.equals(params[1], "false")){
                    enable_debug_log = false;
                    parse_ok = true;
                }
            }
            if (Objects.equals(params[0], "is_in_network:")){
                if (Objects.equals(params[1], "true")){
                    is_in_network = true;
                    parse_ok = true;
                }
                if (Objects.equals(params[1], "false")){
                    is_in_network = false;
                    parse_ok = true;
                }
            }
            if (Objects.equals(params[0], "network_port:")){
                network_port = Integer.parseInt(params[1]);
                parse_ok = true;
            }
            if (Objects.equals(params[0], "network_master_ip:")){
                network_master_ip = params[1];
                parse_ok = true;
            }
            if (Objects.equals(params[0], "node_token:")){
                node_token = params[1];
                parse_ok = true;
            }
            if (Objects.equals(params[0], "allow_entity_spawning:")){
                if (params[1].equals("true")){
                    allow_entity_spawning = true;
                    parse_ok = true;
                }
                if (params[1].equals("false")){
                    allow_entity_spawning = false;
                    parse_ok = true;
                }
            }
            if (Objects.equals(params[0], "server_name:")){
                server_name = params[1];
                parse_ok = true;
            }
            if (Objects.equals(params[0], "database_ip:")){
                database_ip = params[1];
                parse_ok = true;
            }
            if (Objects.equals(params[0], "database_port:")){
                database_port = params[1];
                parse_ok = true;
            }
            if (Objects.equals(params[0], "database_database_name:")){
                database_database_name = params[1];
                parse_ok = true;
            }
            if (Objects.equals(params[0], "database_username:")){
                database_username = params[1];
                parse_ok = true;
            }
            if (Objects.equals(params[0], "database_password:")){
                database_password = params[1];
                parse_ok = true;
            }
            if (Objects.equals(params[0], "enable_discord_bot:")){
                if (Objects.equals(params[1], "true")){
                    enable_discord_bot = true;
                    parse_ok = true;
                }
                if (Objects.equals(params[1], "false")){
                    enable_discord_bot = false;
                    parse_ok = true;
                }
            }
            if (Objects.equals(params[0], "discord_bot_token:")){
                if (params.length == 2){
                    discord_bot_token = params[1];
                    parse_ok = true;
                }else{
                    parse_ok = true;
                }
            }
            index = index + 1;
            if (!(parse_ok))
                System.out.println("ERROR: Could not parse config file. Load default value. Error at line: " + String.valueOf(index));
        }
    }
}
