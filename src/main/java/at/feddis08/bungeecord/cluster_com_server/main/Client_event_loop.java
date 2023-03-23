package at.feddis08.bungeecord.cluster_com_server.main;

import at.feddis08.Boot;
import at.feddis08.bungeecord.cluster_com_server.socket.Server_node_client;
import at.feddis08.bungeecord.cluster_com_server.socket.Server;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class Client_event_loop extends Thread {
    public void run(){
        while (true){
            try {
                loop();
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
            //Thread.sleep(1);
        }
    }

    public static void loop() throws IOException, SQLException {
        Integer index = 0;
        try {
        while (index < Server.servernodeclients.size()) {
            Server_node_client servernodeclient = Server.servernodeclients.get(index);
            Integer index2 = 0;
            try {
            while (index2 < servernodeclient.event_requests.size()) {
                JSONObject request = servernodeclient.event_requests.get(index2);
                String message_id = request.getString("id");
                String event_name = request.getString("event_name");
                if (Objects.equals(event_name, "ping")){

                }
                if (Objects.equals(event_name, "init-connection") && servernodeclient.authenticated == false){
                    for (String token : Boot.cluster_config.nodes_tokens){
                        if (Objects.equals(token, request.getString("token"))){
                            servernodeclient.authenticated = true;
                            servernodeclient.server_data = new Server_client_data();
                            servernodeclient.server_data.name = request.getString("server_name");
                            servernodeclient.server_data.token = request.getString("token");
                            JSONObject json = new JSONObject();
                            json.put("status", "ok");
                            servernodeclient.send_response(json, request);
                        }
                    }
                }
                if (Objects.equals(event_name, "script-event-triggered") && servernodeclient.authenticated){
                    for (Server_node_client servernodeclient2 : Server.servernodeclients){
                        if (servernodeclient2.id != servernodeclient.id && servernodeclient2.authenticated){
                            JSONObject json = new JSONObject();
                            json.put("server_name", servernodeclient.server_data.name);
                            json.put("server_token", servernodeclient.server_data.token);
                            json.put("status", "ok");
                            servernodeclient2.send_event(json, "script-event-triggered");
                        }
                    }
                    JSONObject json = new JSONObject();
                    json.put("status", "ok");
                    servernodeclient.send_response (json, request);
                }

                index2 = index2 + 1;
            }
            } catch (Exception e) {
                index2 = index2 + 1;
                e.printStackTrace();
            }
            servernodeclient.event_requests.clear();
            index = index + 1;
        }
        } catch (Exception e) {
            index = index + 1;
            e.printStackTrace();
        }
    }
}