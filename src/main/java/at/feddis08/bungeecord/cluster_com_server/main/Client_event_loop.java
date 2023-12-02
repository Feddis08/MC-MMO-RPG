package at.feddis08.bungeecord.cluster_com_server.main;

import at.feddis08.Boot;
import at.feddis08.bungeecord.BUNGEE;
import at.feddis08.bungeecord.cluster_com_server.socket.Server_cluster_client;
import at.feddis08.bungeecord.cluster_com_server.socket.Server;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.rmi.ServerError;
import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client_event_loop extends Thread {
    private ExecutorService executorService = Executors.newFixedThreadPool(10); // Adjust the pool size as needed

    public void run() {
        while (!Server.stop) {
            try {
                loop();
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        executorService.shutdownNow();
    }
    public void loop() {
        for (Server_cluster_client client : Server.clients) {
            executorService.submit(() -> {
                // Existing event processing code goes here
                try {
                    JSONObject request = client.event_requests.peek();
                    String message_id = request.getString("id");
                    String event_name = request.getString("event_name");
                    if (Objects.equals(event_name, "ping")) {
                        if (client.authenticated) {
                            JSONObject json = new JSONObject();
                            json.put("status", "ok");
                            client.send_event(json, "ping");
                        }
                    }
                    if (Objects.equals(event_name, "send_player_to_server")) {
                        JSONObject jsonObject = new JSONObject();
                        if (client.authenticated) {
                            for (Server_cluster_client client2 : Server.clients) {
                                if (client2.authenticated && Objects.equals(client2.server_data.name, request.getString("server_name"))) {
                                    ProxiedPlayer player = BUNGEE.proxyServer.getPlayer(UUID.fromString(request.getString("player_id")));
                                    ServerInfo target_server = BUNGEE.proxyServer.getServerInfo(client2.server_data.name);
                                    if (!Objects.equals(player.getServer().getInfo().getName(), target_server.getName())) {
                                        jsonObject.put("player_id", player.getUniqueId().toString());
                                        jsonObject.put("from_server", client.server_data.name);
                                        jsonObject = client2.send_event(jsonObject, "receive_player_to_server");
                                        jsonObject = client2.wait_for_response(jsonObject);
                                        if (jsonObject.getBoolean("ready")) {
                                            jsonObject.put("ok", true);
                                            player.connect(target_server);
                                            Boot.consoleLog("Sent Player to Server!");
                                            JSONObject jsonObject2 = new JSONObject();
                                            jsonObject2.put("arrived", jsonObject.getBoolean("arrived"));
                                            jsonObject2.put("status", true);
                                            jsonObject2.put("info", "sent to server");
                                            client.send_response(jsonObject2, request);
                                        }
                                    } else {
                                        jsonObject.put("status", false);
                                        jsonObject.put("info", "already connected");
                                        client.send_response(jsonObject, request);
                                    }
                                }else{
                                    jsonObject.put("status", false);
                                    jsonObject.put("info", "could not connect to server");
                                    client.send_response(jsonObject, request);
                                }
                            }
                        }
                    }
                    if (Objects.equals(event_name, "script_event_triggered")) {
                        for (Server_cluster_client client2 : Server.clients) {
                            if (client.authenticated) {
                                JSONObject json = new JSONObject();
                                json.put("status", "ok");
                                json.put("event_start", true);
                                if (client2.id == client.id) {
                                    client.send_response(json, request);
                                } else {
                                    json.put("varObjects", request.getString("varObjects"));
                                    json.put("script_event_name", request.getString("script_event_name"));
                                    json.put("server_name", client.server_data.name);
                                    client2.send_event(json, "script_event_triggered");
                                }
                            }
                        }
                    }
                    if (Objects.equals(event_name, "init-connection")) {
                        for (String token : Boot.cluster_config.nodes_tokens) {
                            if (Objects.equals(token, request.getString("token"))) {
                                client.authenticated = true;
                                client.server_data = new Server_client_data();
                                client.server_data.token = token;
                                JSONObject json = new JSONObject();
                                json.put("status", "ok");
                                client.send_response(json, request);
                            }
                        }
                    }
                    if (Objects.equals(event_name, "quit-connection")) {
                        if (client.authenticated) {
                            Boot.consoleLog("[NODE]" + "[" + client.server_data.name + "] Quit connection: " + request.getString("cause"));
                            client.authenticated = false;
                            client.interrupt();
                            client.clientSocket.close();
                            client.input.close();

                        }
                    }
                    if (Objects.equals(event_name, "post-connection")) {
                        if (client.authenticated) {
                            client.server_data.name = request.getString("server_name");

                            JSONObject json = new JSONObject();
                            json.put("status", "ok");
                            json.put("cluster_name", Boot.cluster_config.cluster_name);

                            client.send_response(json, request);
                        }
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                client.event_requests.clear();
            });
        }
    }
}