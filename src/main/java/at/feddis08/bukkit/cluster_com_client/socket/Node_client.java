package at.feddis08.bukkit.cluster_com_client.socket;

import java.io.*;
import java.net.Socket;
import java.util.*;

import at.feddis08.Boot;
import at.feddis08.bukkit.MMORPG;
import at.feddis08.bukkit.logic.scripts.Main;
import at.feddis08.bukkit.logic.scripts.VarObject;
import org.json.JSONObject;

public class Node_client extends Thread{
    private Socket clientSocket;
    private PrintWriter output;
    private BufferedReader input;
    public final ArrayList<JSONObject> response_requests = new ArrayList<>();
    public ArrayList<JSONObject> response_requests_buffer = new ArrayList<>();
    public String id = "";
    public boolean authenticated = false;
    private Thread th = this;
    private boolean got_message = false;
    private boolean wait_for_message = false;
    private boolean loops = false;




    public void startConnection(String ip, int port) throws IOException, InterruptedException {
        clientSocket = new Socket(ip, port);
        output = new PrintWriter(clientSocket.getOutputStream(), true);
        input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        clientSocket.setKeepAlive(true);
        th.start();
        JSONObject json = new JSONObject();
        json.put("token", Boot.config.node_token);
        json.put("server_name", Boot.config.server_name);

        this.send_event(json, "init-connection");
        json = this.wait_for_response(json);
        if (Objects.equals(json.getString("status"), "ok")){
            this.authenticated = true;
            Boot.consoleLog("Authenticated with master!");
        }else{
            Boot.consoleLog("ERROR: Could not authenticate to cluster master with token!");
        }
    }
    public void run(){
        while (true) {
            String str = null;
            try {
                str = listen();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            JSONObject json = new JSONObject(str);

            if (Objects.equals(json.getString("message_type"), "response")) {
                this.got_message = true;
                while (loops){
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                synchronized (response_requests){
                    response_requests.add(json);
                }
                this.got_message = false;
            }

            if (Objects.equals(json.getString("message_type"), "event")){
                try {
                    call_event(json);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public void call_event(JSONObject event) throws IOException {

        if (Objects.equals(event.getString("event_name"), "script-event-triggered")){
            ArrayList<VarObject> varObjects = new ArrayList<>((Collection) event.getJSONArray("varObjects"));
            varObjects.add(new VarObject("server_name", "STRING", event.getString("server_name")));
            varObjects.add(new VarObject("server_token", "STRING", event.getString("server_token")));
            Main.script_start_by_event_name_and_from_other_source(event.getString("script-event-name"), varObjects);
        }

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
        JSONObject response = null;
        while (response == null) {
            Thread.sleep(300);
            int index = 0;
            Boot.consoleLog("dwdw" + this.got_message + this.response_requests.size());
            while (!this.got_message && this.response_requests.size() > 0) {
                Boot.consoleLog("dwdw");
                loops = true;
                synchronized (response_requests){
                    response = findAndRemoveRequestById(msg_to_wait.getString("id"), this.response_requests);
                }
                loops = false;
                if (response != null) {
                    this.got_message = false;
                    break;
                }
            }
        }
        this.wait_for_message = false;
        loops = false;
        return response;
    }
    private JSONObject findAndRemoveRequestById(String id, List<JSONObject> requests) {
        for (Iterator<JSONObject> iterator = requests.iterator(); iterator.hasNext();) {
            JSONObject req = iterator.next();
            if (Objects.equals(req.getString("id"), id)) {
                iterator.remove();
                return req;
            }
        }
        return null;
    }

    public String listen() throws IOException {
        String str = input.readLine();
        Boot.consoleLog("wwdwdwddwdd " + str);
        return str;
    }
}