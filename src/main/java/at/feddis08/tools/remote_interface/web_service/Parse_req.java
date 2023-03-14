package at.feddis08.tools.remote_interface.web_service;

import at.feddis08.Boot;
import at.feddis08.bukkit.MMORPG;
import at.feddis08.tools.io.database.Functions;
import at.feddis08.tools.io.database.objects.UserObject;
import at.feddis08.bukkit.minecraft.tools.Methods;
import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.sql.SQLException;
import java.util.Objects;

public class Parse_req {

    public static String do_req(HttpServletRequest req) throws IOException, SQLException {
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = req.getReader();
        String response = "";
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        String data = buffer.toString();
        if (!data.isBlank() || !data.isEmpty()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("ok", "true");
            JSONObject json_data = new JSONObject(data);
            String session_id;
            Web_user web_user = null;
            try {
                session_id = json_data.get("session_id").toString();
                int i = 0;
                while (i < Main.web_users.size()){
                    if (Objects.equals(session_id, Main.web_users.get(i).session_id)){
                        web_user = Main.web_users.get(i);
                        web_user.last_resp = System.currentTimeMillis();
                    }
                    i ++;
                }
            }catch (Exception ignored){};
            if (web_user == null) {
                session_id = Methods.get_hash(String.valueOf(System.currentTimeMillis()));
                web_user = new Web_user("", session_id, System.currentTimeMillis());
                jsonObject.put("session_id", session_id);
                Main.web_users.add( web_user);
            }

            if (Objects.equals(json_data.get("req").toString(), "get server data")) {
                RuntimeMXBean rb = ManagementFactory.getRuntimeMXBean();
                long uptime = rb.getUptime();
                jsonObject.put("server_name", MMORPG.Server.getMotd());
                jsonObject.put("server_type_name", MMORPG.Server.getName());
                jsonObject.put("online_players", MMORPG.Server.getOnlinePlayers().size());
                jsonObject.put("server_up_time", uptime);
                response = jsonObject.toString();
            }
            if (Objects.equals(json_data.get("req").toString(), "login with u&p")) {
                UserObject userObject = Functions.getUser("id", Functions.getPlayer("display_name", json_data.get("username").toString()).id);
                if (Objects.equals(userObject.id, "")) {
                    //user not found
                    jsonObject.put("status", "failed");
                } else {
                    if (Objects.equals(userObject.hash, Methods.get_hash(json_data.get("key").toString()))) {
                        web_user.id = userObject.id;

                        userObject.jsonObject.put("logged_in", "true");
                        userObject.jsonObject.put("auth_token", Methods.get_hash(String.valueOf(Math.random())));
                        userObject.jsonObject.put("remote_host", req.getRemoteHost());
                        userObject.jsonObject.put("last_login", String.valueOf(System.currentTimeMillis()));

                        userObject.data_json = userObject.jsonObject.toString();
                        Functions.update("users", "data_json", userObject.data_json, userObject.id, "id");
                        jsonObject.put("status", "success");
                        jsonObject.put("auth_token", userObject.jsonObject.get("auth_token"));

                        if (Objects.equals(userObject.first_name, "")) {
                            //account not set up
                            jsonObject.put("need_set_up", "true");
                        } else {
                            //passed
                            jsonObject.put("need_set_up", "false");
                        }
                    } else {
                        jsonObject.put("status", "failed");
                    }
                }
                response = jsonObject.toString();
            }
            if (Objects.equals(json_data.get("req").toString(), "login with token")) {
                UserObject userObject = Functions.getUser("id", Functions.getPlayer("display_name", json_data.get("username").toString()).id);
                if (Objects.equals(userObject.id, "")) {
                    //user not found
                    jsonObject.put("status", "failed");
                } else {
                    if (Objects.equals(userObject.jsonObject.get("auth_token").toString(), json_data.get("key").toString())) {
                        web_user.id = userObject.id;

                        userObject.jsonObject.put("logged_in", "true");
                        userObject.jsonObject.put("auth_token", Methods.get_hash(String.valueOf(Math.random())));
                        userObject.jsonObject.put("remote_host", req.getRemoteHost());
                        userObject.jsonObject.put("last_login", String.valueOf(System.currentTimeMillis()));

                        userObject.data_json = userObject.jsonObject.toString();
                        Functions.update("users", "data_json", userObject.data_json, userObject.id, "id");
                        jsonObject.put("status", "success");
                        jsonObject.put("auth_token", userObject.jsonObject.get("auth_token"));
                    } else {
                        jsonObject.put("status", "failed");
                    }
                }
                response = jsonObject.toString();
            }
        }
        Boot.consoleLog(response + " wwwwwwwwwwwwww");
        return response;
    }
}
