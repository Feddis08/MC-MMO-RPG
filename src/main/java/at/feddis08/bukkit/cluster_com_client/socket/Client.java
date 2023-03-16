package at.feddis08.bukkit.cluster_com_client.socket;

import javax.sound.sampled.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;

import at.feddis08.Boot;
import at.feddis08.bukkit.MMORPG;
import org.json.JSONObject;

public class Client extends Thread{
    private Socket clientSocket;
    private PrintWriter output;
    private BufferedReader input;
    public ArrayList<JSONObject> response_requests = new ArrayList<>();
    public static String id = "";
    private Thread th = this;



    public void startConnection(String ip, int port) throws IOException{
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
                call_event(json);
            }
        }
    }


    public void call_event(JSONObject event){
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
    public JSONObject wait_for_response(JSONObject msg_to_wait) {
        int index = 0;
        JSONObject response = null;
        while (response == null) {
            if (response_requests.size() != 0 ) {
                JSONObject jsonObject = new JSONObject(this.response_requests.get(index));
                if (Objects.equals(jsonObject.getString("id"), msg_to_wait.getString("id"))) {
                    response = jsonObject;
                    this.response_requests.remove(index);
                }
            }
        }
        return response;
    }
    public String listen() throws IOException {
        String str = input.readLine();
        Boot.consoleLog(str);
        return str;
    }
}