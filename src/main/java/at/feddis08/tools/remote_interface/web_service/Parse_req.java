package at.feddis08.tools.remote_interface.web_service;

import at.feddis08.Boot;
import at.feddis08.bukkit.MMORPG;
import at.feddis08.bukkit.logic.scripts.Executor;
import at.feddis08.bungeecord.BUNGEE;
import at.feddis08.tools.Rank_api;
import at.feddis08.tools.io.database.Functions;
import at.feddis08.tools.io.database.JDBC;
import at.feddis08.tools.io.database.objects.PlayerObject;
import at.feddis08.tools.io.database.objects.UserObject;
import at.feddis08.bukkit.minecraft.tools.Methods;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import org.bukkit.Bukkit;
import org.checkerframework.checker.units.qual.A;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
                web_user = new Web_user("", "0", System.currentTimeMillis());
                Main.web_users.add( web_user);
            }
            web_user.session_id = Methods.get_hash(String.valueOf(System.currentTimeMillis()));
            jsonObject.put("session_id", web_user.session_id);
            if (Objects.equals(json_data.get("req").toString(), "get script commands")) {


                jsonObject.put("data", new JSONObject(Executor.commandsToJson(Executor.commands)));


                response = jsonObject.toString();
            }
            if (Objects.equals(json_data.get("req").toString(), "get server data")) {
                RuntimeMXBean rb = ManagementFactory.getRuntimeMXBean();
                long uptime = rb.getUptime();
                jsonObject.put("server_name", Boot.config.server_name);
                jsonObject.put("is_bungee", Boot.is_bungee);
                jsonObject.put("server_up_time", uptime);

                if (Boot.is_bungee){
                    jsonObject.put("player_count", BUNGEE.proxyServer.getOnlineCount());

                }else{
                    jsonObject.put("player_count", Bukkit.getServer().getOnlinePlayers().size());
                    jsonObject.put("favicon", Bukkit.getServer().getServerIcon());
                }

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
                        jsonObject.put("auth_id", Functions.getPlayer("display_name", json_data.getString("username")).id);

                        if (!userObject.jsonObject.getBoolean("did_setup")) {
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
                UserObject userObject = Functions.getUser("id", json_data.getString("auth_id"));
                if (Objects.equals(userObject.id, "")) {
                    //user not found
                    jsonObject.put("status", "failed");
                } else {
                    if (Objects.equals(userObject.jsonObject.get("auth_token").toString(), json_data.get("auth_token").toString())) {
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
            if (Objects.equals(json_data.get("req").toString(), "get user")) {
                UserObject userObject = Functions.getUser("id", web_user.id);
                if (Objects.equals(userObject.id, "")) {
                    //user not found
                    jsonObject.put("status", "failed");
                } else {
                    String req_user_id = json_data.getString("user_id");
                    if (req_user_id.equals(web_user.id)){
                        if (Rank_api.isPlayer_allowedTo(web_user.id, "get_own_user")){
                            PlayerObject player = Functions.getPlayer("id", web_user.id);
                            userObject.jsonObject.put("first_name", userObject.first_name);
                            userObject.jsonObject.put("last_name", userObject.last_name);
                            userObject.jsonObject.put("display_name", player.display_name);
                            userObject.jsonObject.put("player_name", player.player_name);
                            jsonObject.put("data", userObject.jsonObject);
                            jsonObject.put("status", "success");
                        }else{
                            jsonObject.put("status", "failed");
                            jsonObject.put("permission", "get_own_user");
                            jsonObject.put("info", "permission");
                        }
                    }else{
                        if (Rank_api.isPlayer_allowedTo(web_user.id, "get_other_user")){
                            userObject = Functions.getUser("id", json_data.getString("user_id"));
                            PlayerObject player = Functions.getPlayer("id", userObject.id);
                            userObject.jsonObject.put("first_name", userObject.first_name);
                            userObject.jsonObject.put("last_name", userObject.last_name);
                            userObject.jsonObject.put("display_name", player.display_name);
                            userObject.jsonObject.put("player_name", player.player_name);
                            jsonObject.put("data", userObject.jsonObject);
                            jsonObject.put("status", "success");
                        }else{
                            jsonObject.put("status", "failed");
                            jsonObject.put("permission", "get_other_user");
                            jsonObject.put("info", "permission");
                        }
                    }
                }
                response = jsonObject.toString();
            }
            if (Objects.equals(json_data.get("req").toString(), "setup")) {
                UserObject userObject = Functions.getUser("id", web_user.id);
                if (Objects.equals(userObject.id, "")) {
                    //user not found
                    jsonObject.put("status", "failed");
                } else {
                    if (Rank_api.isPlayer_allowedTo(userObject.id, "doSetup")){
                        try{
                            userObject.jsonObject.put("first_name", json_data.getString("first_name"));
                            userObject.jsonObject.put("last_name", json_data.getString("last_name"));
                            userObject.jsonObject.put("email", json_data.getString("email"));
                            userObject.jsonObject.put("login_message", json_data.getString("login_message"));
                            userObject.jsonObject.put("send_message_on_loggin", json_data.getString("send_message_on_loggin"));
                            userObject.jsonObject.put("did_setup", true);
                            userObject.data_json = userObject.jsonObject.toString();
                            Functions.update("users", "data_json", userObject.data_json, userObject.id, "id");
                            jsonObject.put("status", "success");
                        }catch (Exception e){
                            jsonObject.put("status", "failed");
                        }
                    }
                }
                response = jsonObject.toString();
            }
            if (Objects.equals(json_data.get("req").toString(), "get log data")) {
                UserObject userObject = Functions.getUser("id", web_user.id);
                if (Objects.equals(userObject.id, "")) {
                    //user not found
                    jsonObject.put("status", "failed");
                } else {
                    if (Rank_api.isPlayer_allowedTo(userObject.id, "doGetLogData")){
                        try{

                            Statement stmt = JDBC.myConn.createStatement();
                            String sql = "";
                            boolean isResultSet = stmt.execute(sql);

                            JSONObject response_db = new JSONObject();
                            JSONArray rowsArray = new JSONArray();

                            if (isResultSet) {
                                ResultSet resultSet = stmt.getResultSet();
                                ResultSetMetaData metaData = resultSet.getMetaData();
                                int columnCount = metaData.getColumnCount();

                                while (resultSet.next()) {
                                    JSONObject rowJson = new JSONObject();
                                    JSONArray columnsArray = new JSONArray();

                                    for (int i = 1; i <= columnCount; i++) {
                                        String columnName = metaData.getColumnName(i);
                                        Object value = resultSet.getObject(i);
                                        String dataType = metaData.getColumnTypeName(i);

                                        JSONObject columnData = new JSONObject();
                                        columnData.put("value", value);
                                        columnData.put("data_type", dataType);
                                        columnData.put("name", columnName);
                                        columnData.put("index", i);

                                        columnsArray.put(columnData);
                                    }

                                    rowJson.put("columns", columnsArray);
                                    rowsArray.put(rowJson);
                                }

                                response_db.put("rows", rowsArray);
                            } else {
                                int updateCount = stmt.getUpdateCount();
                                response_db.put("update_count", updateCount);
                            }

                            jsonObject.put("database_response", response_db);


                            jsonObject.put("status", "success");
                        }catch (Exception e){
                            jsonObject.put("info", "error");
                            jsonObject.put("error", e.toString());
                            jsonObject.put("status", "failed");
                        }
                    }
                }
                response = jsonObject.toString();
            }
            if (Objects.equals(json_data.get("req").toString(), "request database")) {
                UserObject userObject = Functions.getUser("id", web_user.id);
                if (Objects.equals(userObject.id, "")) {
                    //user not found
                    jsonObject.put("status", "failed");
                } else {
                    if (Rank_api.isPlayer_allowedTo(userObject.id, "doDatabase")){
                        try{

                            Statement stmt = JDBC.myConn.createStatement();
                            String sql = json_data.getString("database_request");
                            boolean isResultSet = stmt.execute(sql);

                            JSONObject response_db = new JSONObject();
                            JSONArray rowsArray = new JSONArray();

                            if (isResultSet) {
                                ResultSet resultSet = stmt.getResultSet();
                                ResultSetMetaData metaData = resultSet.getMetaData();
                                int columnCount = metaData.getColumnCount();

                                while (resultSet.next()) {
                                    JSONObject rowJson = new JSONObject();
                                    JSONArray columnsArray = new JSONArray();

                                    for (int i = 1; i <= columnCount; i++) {
                                        String columnName = metaData.getColumnName(i);
                                        Object value = resultSet.getObject(i);
                                        String dataType = metaData.getColumnTypeName(i);

                                        JSONObject columnData = new JSONObject();
                                        columnData.put("value", value);
                                        columnData.put("data_type", dataType);
                                        columnData.put("name", columnName);
                                        columnData.put("index", i);

                                        columnsArray.put(columnData);
                                    }

                                    rowJson.put("columns", columnsArray);
                                    rowsArray.put(rowJson);
                                }

                                response_db.put("rows", rowsArray);
                            } else {
                                int updateCount = stmt.getUpdateCount();
                                response_db.put("update_count", updateCount);
                            }

                            jsonObject.put("database_response", response_db);


                            jsonObject.put("status", "success");
                        }catch (Exception e){
                            jsonObject.put("info", "error");
                            jsonObject.put("error", e.toString());
                            jsonObject.put("status", "failed");
                        }
                    }
                }
                response = jsonObject.toString();
            }
        }
        Boot.consoleLog(response + " wwwwwwwwwwwwww");
        return response;
    }
}
