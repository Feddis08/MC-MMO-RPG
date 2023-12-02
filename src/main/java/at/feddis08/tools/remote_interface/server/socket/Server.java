package at.feddis08.tools.remote_interface.server.socket;


import at.feddis08.tools.remote_interface.server.Start;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Objects;

public class Server extends Thread{
    public static Thread th = new Server();
    public static ServerSocket serverSocket;
    public static ArrayList<Client> clients = new ArrayList<>();
    static boolean stop = false;

    public void run(){
        try {
            startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void send_audio_to_channel_by_client(Client client, byte[] data) throws IOException {
        Integer index = 0;
        Client client2;
        while (index < Server.clients.size()) {
            client2 = Server.clients.get(index);
            if (Objects.equals(client2.channel_name, client.channel_name)) {
                if (client2.in_chat){
                    client2.sendMessage("audio_output_stream" + Start.spacing + Base64.getEncoder().encodeToString(data) + Start.spacing + client.player.id);
                }
            }
            index = index + 1;
        }
    }
    public static void broadcast_chat_message(String message) throws IOException {
        stop = true;
        Integer index = 0;
        Client client;
        while (index < Server.clients.size()) {
            client = Server.clients.get(index);
            client.send_chat_message(message);
            index = index + 1;
        }
    }
    public static Client get_client(String id) throws IOException {
        Integer index = 0;
        Client client;
        Client result = null;
        while (index < Server.clients.size()) {
            client = Server.clients.get(index);
            if (Objects.equals(client.player.id, id)) {
               result = client;
            }
            index = index + 1;
        }
        return result;
    }
    public static void close() throws IOException {
        stop = true;
        if (!serverSocket.isClosed()) {
            Integer index = 0;
            while (index < Server.clients.size()) {
                Client client = Server.clients.get(index);
                client.closeConnection();
                index = index + 1;
            }
            serverSocket.close();
            clients.clear();
        }
    }
    public static void startServer() throws IOException {
        serverSocket = new ServerSocket(Start.port);
        Start.log("running");

        try {
            while (!stop){
                Socket c = serverSocket.accept();
                Client client = new Client(c);
                clients.add(client);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}