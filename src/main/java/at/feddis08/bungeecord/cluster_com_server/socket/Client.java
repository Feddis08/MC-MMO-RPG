package at.feddis08.bungeecord.cluster_com_server.socket;


import at.feddis08.Boot;
import at.feddis08.bungeecord.cluster_com_server.main.Server_client_data;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;

public class Client extends Thread{
    public Socket clientSocket;
    public BufferedReader input;
    public PrintWriter output;
    public Thread th = this;
    public ArrayList<JSONObject> event_requests = new ArrayList<>();
    public ArrayList<JSONObject> response_requests = new ArrayList<>();
    public int id;
    public Server_client_data server_data;
    public boolean authenticated = false;

    public Client(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.output = new PrintWriter(clientSocket.getOutputStream(), true);
        this.input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.clientSocket.setKeepAlive(true);
        this.id = (int) System.currentTimeMillis();
        th.start();
    }
    public void run(){
        while (true){
            try {
                String str = listen();
                JSONObject json = new JSONObject(str);
                if (Objects.equals(json.getString("message_type"), "response")){
                    this.response_requests.add(json);
                }
                if (Objects.equals(json.getString("message_type"), "event")){
                    this.event_requests.add(json);
                }
            } catch (IOException e) {
                e.printStackTrace();
                th.stop();
            }
        }
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
        while (response == null) {
            if (response_requests.size() > 0 ) {
                Thread.sleep(100);
                JSONObject jsonObject = this.response_requests.get(index);
                Boot.consoleLog(jsonObject.toString());
                Boot.consoleLog(msg_to_wait.toString());
                if (Objects.equals(jsonObject.getString("id"), msg_to_wait.getString("id"))) {
                    response = jsonObject;
                    this.response_requests.remove(index);
                }
            }
        }
        return response;
    }
    public void closeConnection() throws IOException {
        th.stop();
        output.close();
        input.close();
        clientSocket.close();
    }
    public String listen() throws IOException {
        String str = null;
        if (clientSocket.isConnected()) {
            try {
            str = input.readLine();
            }catch(Exception e){
                closeConnection();
                str = null;
            }
        }
        Boot.consoleLog(str);
        return str;
    }
}