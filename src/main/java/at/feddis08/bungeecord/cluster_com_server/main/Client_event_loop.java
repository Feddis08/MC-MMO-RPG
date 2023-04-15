package at.feddis08.bungeecord.cluster_com_server.main;

import at.feddis08.Boot;
import at.feddis08.bungeecord.cluster_com_server.socket.Server_cluster_client;
import at.feddis08.bungeecord.cluster_com_server.socket.Server;
import org.json.JSONObject;

import java.io.IOException;
import java.rmi.ServerError;
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
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void loop() throws IOException, SQLException {
        Integer index = 0;
        try {
        while (index < Server.clients.size()) {
            Server_cluster_client client = Server.clients.get(index);
            Integer index2 = 0;
            try {
            while (index2 < client.event_requests.size()) {
                JSONObject request = client.event_requests.get(index2);
                String message_id = request.getString("id");
                String event_name = request.getString("event_name");
                if (Objects.equals(event_name, "ping")){
                    if (client.authenticated){
                        JSONObject json = new JSONObject();
                        json.put("status", "ok");
                        client.send_event(json, "ping");
                    }
                }
                if (Objects.equals(event_name, "script_event_triggered")){
                    for (Server_cluster_client client2 : Server.clients){
                        if (client.authenticated){
                            JSONObject json = new JSONObject();
                            json.put("status", "ok");
                            json.put("event_start", true);
                            if (client2.id == client.id){
                                client.send_response(json, request);
                            }else{
                                json.put("varObjects", request.getString("varObjects"));
                                json.put("script_event_name", request.getString("script_event_name"));
                                json.put("server_name", client.server_data.name);
                                client2.send_event(json, "script_event_triggered");
                            }
                        }
                    }
                }
                if (Objects.equals(event_name, "init-connection")){
                    for (String token : Boot.cluster_config.nodes_tokens){
                        if (Objects.equals(token, request.getString("token"))){
                            client.authenticated = true;
                            client.server_data = new Server_client_data();
                            client.server_data.token = token;
                            JSONObject json = new JSONObject();
                            json.put("status", "ok");
                            client.send_response(json, request);
                        }
                    }
                }
                if (Objects.equals(event_name, "post-connection")){
                    if (client.authenticated){
                        client.server_data.name = request.getString("server_name");

                        JSONObject json = new JSONObject();
                        json.put("status", "ok");
                        json.put("cluster_name", Boot.cluster_config.cluster_name);

                        client.send_response(json, request);
                    }
                }

                index2 = index2 + 1;
            }
            } catch (Exception e) {
                index2 = index2 + 1;
                e.printStackTrace();
            }
            client.event_requests.clear();
            index = index + 1;
        }
        } catch (Exception e) {
            index = index + 1;
            e.printStackTrace();
        }
    }
}