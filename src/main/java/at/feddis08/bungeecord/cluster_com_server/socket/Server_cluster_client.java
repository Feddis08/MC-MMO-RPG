package at.feddis08.bungeecord.cluster_com_server.socket;
import at.feddis08.Boot;
import at.feddis08.bungeecord.cluster_com_server.main.Server_client_data;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Server_cluster_client extends Thread {
    public Socket clientSocket;
    public BufferedReader input;
    public PrintWriter output;
    public ConcurrentLinkedQueue<JSONObject> event_requests = new ConcurrentLinkedQueue<>();
    private ConcurrentLinkedQueue<JSONObject> response_requests = new ConcurrentLinkedQueue<>();
    public int id;
    public Server_client_data server_data = new Server_client_data();
    public boolean authenticated = false;
    boolean stop_connection = false;

    public Server_cluster_client(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.output = new PrintWriter(clientSocket.getOutputStream(), true);
        this.input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.clientSocket.setKeepAlive(true);
        this.id = (int) System.currentTimeMillis();
        this.start();
    }

    public void run() {
        while (!this.stop_connection) {
            try {
                String str = listen();
                if (!Objects.equals(str, "null")) {
                    JSONObject json = new JSONObject(str);
                    if (Objects.equals(json.getString("message_type"), "response")) {
                        response_requests.add(json);
                    } else if (Objects.equals(json.getString("message_type"), "event")) {
                        event_requests.add(json);
                    }
                }
            } catch (IOException e) {
                // Handle exception appropriately
                this.interrupt();
            }
        }

        try {
            closeResources();
        } catch (IOException e) {
            // Handle IOException during resource closing
        }
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
        } while (!this.isInterrupted());
        return response;
    }

    public void closeConnection() throws IOException {
        this.stop_connection = true;
        closeResources();
    }

    private void closeResources() throws IOException {
        output.close();
        input.close();
        clientSocket.close();
    }

    public String listen() throws IOException {
        String str = input.readLine();
        if (str != null) {
            Boot.debugLog("NODE SERVER: [" + server_data.name + "]: " + str);
            return str;
        } else {
            return "null";
        }
    }
}