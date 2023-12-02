package at.feddis08.bukkit.cluster_com_client.socket;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import at.feddis08.Boot;
import at.feddis08.bukkit.logic.scripts.Main;
import at.feddis08.bukkit.logic.scripts.VarObject;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.sun.security.jgss.InquireType;
import org.json.JSONObject;

public class Cluster_client extends Thread{
    private Socket clientSocket;
    private PrintWriter output;
    private BufferedReader input;
    private ConcurrentLinkedQueue<JSONObject> response_requests = new ConcurrentLinkedQueue<>();
    public static String id = "";
    private volatile boolean connected = false;
    public final ArrayList<Incoming_player> incoming_players = new ArrayList<>();

    private volatile boolean stop = false;
    private ExecutorService eventExecutor = Executors.newCachedThreadPool();

    public void startConnection(String ip, int port) throws IOException, InterruptedException {
        clientSocket = new Socket(ip, port);
        output = new PrintWriter(clientSocket.getOutputStream(), true);
        input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        clientSocket.setKeepAlive(true);
        this.start();
        JSONObject json = new JSONObject();
        json.put("token", Boot.config.node_token);
        json = this.send_event(json, "init-connection");
        json = this.wait_for_response(json);
        if (json.getString("status").equals("ok")) {
            this.connected = true;
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("server_name", Boot.config.server_name);
            this.send_event(jsonObject, "post-connection");
            jsonObject = this.wait_for_response(jsonObject);
            this.send_event(jsonObject, "ping");
        }
    }
    public void run() {
        while (!stop) {
            try {
                String str = listen();
                if (!str.equals("null")) {
                    JSONObject json = new JSONObject(str);
                    handleEvent(json);
                }
            } catch (IOException e) {
                // Handle exceptions appropriately
            }
        }
        eventExecutor.shutdownNow();
    }
    private void handleEvent(JSONObject json) {
        if (json.getString("message_type").equals("response")) {
            response_requests.add(json);
        } else if (json.getString("message_type").equals("event")) {
            eventExecutor.submit(() -> {
                try {
                    try {
                        processEvent(json);
                    } catch (ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }
    private void processEvent(JSONObject event) throws IOException, InterruptedException, ExecutionException {
        call_event(event);
    }
    public void call_event(JSONObject event) throws IOException, InterruptedException, ExecutionException {

        if (Objects.equals(event.getString("event_name"), "ping")){
            JSONObject json = new JSONObject();
            json.put("status", "ok");
            Thread.sleep(1000);
            this.send_event(json, "ping");
        }
        if (Objects.equals(event.getString("event_name"), "script_event_triggered")){
            Gson gson = new Gson();
            ArrayList<VarObject> varObjects = gson.fromJson(event.getString("varObjects"), new TypeToken<List<VarObject>>(){}.getType());
            varObjects.add(new VarObject("server_name","STRING" , event.getString("server_name")));
            Main.script_start_by_event_name(event.getString("script_event_name"), varObjects, true);
        }
        if (Objects.equals(event.getString("event_name"), "receive_player_to_server")){
            boolean already_connecting = false;
            synchronized (this.incoming_players) {
                Incoming_player incoming_player = new Incoming_player(event.getString("player_id"), event.getString("from_server"), false);
                for (Incoming_player incoming_player1 : this.incoming_players){
                    if (Objects.equals(incoming_player1.player_id, incoming_player.player_id)){
                        already_connecting = true;
                        break;
                    }
                }
                if (!already_connecting) incoming_players.add(incoming_player);
            }
            if (!already_connecting) {
                boolean stop = false;
                JSONObject json2 = new JSONObject();
                json2.put("ready", true);
                this.send_response(json2, event);
                Incoming_player i_p = null;
                while (!stop) {
                    synchronized (this.incoming_players) {
                        for (Incoming_player i_player : this.incoming_players) {
                            if (Objects.equals(i_player.player_id, event.getString("player_id"))) {
                                if (i_player.arrived) {
                                    i_p = i_player;
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("arrived", true);
                                    this.send_response(jsonObject, event);
                                    stop = true;
                                    break;
                                }
                            }
                        }
                        if (stop) {
                            this.incoming_players.remove(i_p);
                        }
                    }
                }
                Boot.consoleLog("Player " + i_p.player_id + ",has joined via CLUSTER!");
            }else{
                JSONObject json = new JSONObject();
                json.put("ready", false);
                this.send_response(json, event);
            }
        }

    }

    public void sendMessage(String msg) throws IOException {
        output.println(msg);
    }

    public void stopConnection(String cause) throws IOException {
        JSONObject event = new JSONObject();
        event.put("cause", cause);
        this.send_event(event, "quit-connection");
        this.stop = true;
        clientSocket.close();
        eventExecutor.shutdownNow();
    }
    public void send_response(JSONObject msg, JSONObject msg_to_response) throws IOException {
        msg.put("id", msg_to_response.getString("id"));
        msg.put("message_type", "response");
        output.println(msg);
    }
    public JSONObject send_event(JSONObject msg, String event_name) throws IOException {
        msg.put("id", String.valueOf(System.currentTimeMillis()));
        msg.put("message_type", "event");
        msg.put("event_name", event_name);
        output.println(msg);
        return msg;
    }
    public JSONObject wait_for_response(JSONObject msg_to_wait) throws InterruptedException {
        JSONObject response;
        do {
            response = response_requests.stream()
                    .filter(jsonObject -> jsonObject.getString("id").equals(msg_to_wait.getString("id")))
                    .findFirst()
                    .orElse(null);

            if (response != null) {
                response_requests.remove(response);
                break;
            }
        } while (!stop);
        return response;
    }
    public String listen() throws IOException {
        String str = input.readLine();
        if (str != null) {
            Boot.debugLog("CLUSTER MASTER: " + str);
            return str;
        } else {
            return "null";
        }
    }
}
