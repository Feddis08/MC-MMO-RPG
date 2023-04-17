package at.feddis08.bukkit.cluster_com_client.socket;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import at.feddis08.Boot;
import at.feddis08.bukkit.MMORPG;
import at.feddis08.bukkit.cluster_com_client.Start_cluster_client;
import at.feddis08.bukkit.logic.scripts.Main;
import at.feddis08.bukkit.logic.scripts.Var;
import at.feddis08.bukkit.logic.scripts.VarObject;
import at.feddis08.bungeecord.BUNGEE;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.checkerframework.checker.units.qual.A;
import org.json.JSONObject;

public class Cluster_client extends Thread{
    private Socket clientSocket;
    private PrintWriter output;
    private BufferedReader input;
    public ArrayList<JSONObject> response_requests = new ArrayList<>();
    public static String id = "";
    private Thread th = this;
    public boolean connected = false;
    public ArrayList<Incoming_player> incoming_players = new ArrayList<>();
    public boolean player_joined = false;



    public void startConnection(String ip, int port) throws IOException, InterruptedException {
        clientSocket = new Socket(ip, port);
        output = new PrintWriter(clientSocket.getOutputStream(), true);
        input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        clientSocket.setKeepAlive(true);
        th.start();
        JSONObject json = new JSONObject();
        json.put("token", Boot.config.node_token);
        this.send_event(json, "init-connection");
        json = this.wait_for_response(json);
        Boot.consoleLog(json.toString());
        if (Objects.equals(json.getString("status"), "ok")){
            this.connected = true;
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("server_name", Boot.config.server_name);
            this.send_event(jsonObject, "post-connection");
            jsonObject = this.wait_for_response(jsonObject);
            this.send_event(jsonObject, "ping");
        }
    }
    public void run(){
        while (true){
            String str = null;
            try {
                str = listen();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            JSONObject json = new JSONObject(str);
            if (Objects.equals(json.getString("message_type"), "response")){
                this.response_requests.add(json);
            }
            if (Objects.equals(json.getString("message_type"), "event")){
                Thread t = new Thread(){
                    public void run(){
                        try {
                            call_event(json);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                };
                t.start();

            }
        }
    }


    public void call_event(JSONObject event) throws IOException, InterruptedException {

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
            Boot.consoleLog(varObjects.toString());
            Main.script_start_by_event_name(event.getString("script_event_name"), varObjects, true);
        }
        if (Objects.equals(event.getString("event_name"), "send_player_to_server")){
            this.incoming_players.add(new Incoming_player(event.getString("player_id"), event.getString("from_server"), false));
            boolean stop = false;
            JSONObject json2 = new JSONObject();
            json2.put("ready", true);
            this.send_response(json2, event);
            while (!stop){
                Thread.sleep(5);
                if (this.player_joined){
                    Thread.sleep(5);
                    int i = 0;
                    for (Incoming_player i_player : this.incoming_players){
                        Thread.sleep(5);
                        if (Objects.equals(i_player.player_id, event.getString("player_id"))){
                            if (i_player.arrived){
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("arrived", true);
                                this.send_response(jsonObject, event);
                                stop = true;
                            }
                        }
                        if (!stop)
                            i ++;
                    }
                    if (stop){
                        this.incoming_players.remove(i);
                    }
                    player_joined = false;
                }
            }
        }

    }

    public void sendMessage(String msg) throws IOException {
        output.println(msg);
    }

    public void stopConnection() throws IOException {
        input.close();
        output.close();
        clientSocket.close();
        th.stop();
    }
    public void send_response(JSONObject msg, JSONObject msg_to_response) throws IOException {
        msg.put("id", msg_to_response.getString("id"));
        msg.put("message_type", "response");
        output.println(msg);
    }
    public void send_event(JSONObject msg, String event_name) throws IOException {
        msg.put("id", String.valueOf(System.currentTimeMillis()));
        msg.put("message_type", "event");
        msg.put("event_name", event_name);
        output.println(msg);
    }
    public JSONObject wait_for_response(JSONObject msg_to_wait) throws InterruptedException {
        int index = 0;
        JSONObject response = null;
        Boot.consoleLog("w2e2e2 " + msg_to_wait.toString() + " s " + Arrays.toString(Thread.currentThread().getStackTrace()));
        while (response == null) {
            Thread.sleep(1);
            if (response_requests.size() != 0 ) {
                Thread.sleep(5);
                index = 0;
                while (index < this.response_requests.size()){
                    JSONObject jsonObject = this.response_requests.get(index);
                    if (Objects.equals(jsonObject.getString("id"), msg_to_wait.getString("id"))) {
                        response = jsonObject;
                        this.response_requests.remove(index);
                    }
                    index ++;
                }
            }
        }
        Boot.consoleLog("dw344");
        return response;
    }
    public String listen() throws IOException {
        String str = input.readLine();
        Boot.consoleLog(str);
        return str;
    }
}