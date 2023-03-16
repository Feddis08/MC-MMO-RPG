package at.feddis08.bungeecord.cluster_com_server.socket;


import at.feddis08.Boot;
import at.feddis08.bungeecord.cluster_com_server.Start_cluster_server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;

public class Server extends Thread{
    public static Thread th = new Server();
    public static ServerSocket serverSocket;
    public static ArrayList<Client> clients = new ArrayList<>();

    public void run(){
        try {
            startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static Client get_client(String id) throws IOException {
        Integer index = 0;
        Client client;
        Client result = null;
        while (index < Server.clients.size()) {
            client = Server.clients.get(index);
            if (Objects.equals(client.id, id)) {
               result = client;
            }
            index = index + 1;
        }
        return result;
    }
    public static void close() throws IOException {
        th.stop();
        Integer index = 0;
        while (index < Server.clients.size()) {
            Client client = Server.clients.get(index);
            client.closeConnection();
            index = index + 1;
        }
        serverSocket.close();
        clients.clear();
    }
    public static void startServer() throws IOException {
        serverSocket = new ServerSocket(Start_cluster_server.port);
        Boot.consoleLog("running");
        try {
            while (true){
                Socket c = serverSocket.accept();
                Client client = new Client(c);
                clients.add(client);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}