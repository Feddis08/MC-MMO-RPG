package at.feddis08.bungeecord.cluster_com_server.main;

import at.feddis08.Boot;
import at.feddis08.bungeecord.cluster_com_server.socket.Client;
import at.feddis08.bungeecord.cluster_com_server.socket.Server;
import jdk.security.jarsigner.JarSigner;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class Client_event_loop extends Thread {
    public void run(){
        Boot.consoleLog("e2ewgrdwd");
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
            Client client = Server.clients.get(index);
            Integer index2 = 0;
            try {
            while (index2 < client.event_requests.size()) {
                JSONObject request = client.event_requests.get(index2);
                String message_id = request.getString("id");
                String event_name = request.getString("event_name");
                if (Objects.equals(event_name, "ping")){

                }
                if (Objects.equals(event_name, "init-connection")){
                    for (String token : Boot.cluster_config.nodes_tokens){
                        if (Objects.equals(token, request.getString("token"))){
                            client.authenticated = true;
                            JSONObject json = new JSONObject();
                            json.put("status", "ok");
                            client.send_response(json, request);
                        }
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