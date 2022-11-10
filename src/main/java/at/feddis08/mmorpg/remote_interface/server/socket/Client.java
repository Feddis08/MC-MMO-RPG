package at.feddis08.mmorpg.remote_interface.server.socket;


import at.feddis08.mmorpg.remote_interface.server.Start;
import at.feddis08.mmorpg.remote_interface.server.entities.Player;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;

public class Client extends Thread{
    public Socket clientSocket;
    public BufferedReader input;
    public PrintWriter output;
    public Thread th = this;
    public ArrayList<String> requests = new ArrayList<>();
    public Player player;
    public String current_path = "/";
    public Boolean inGame = false;

    public Client(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.output = new PrintWriter(clientSocket.getOutputStream(), true);
        this.input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        Player player = new Player(Server.clients.size());
        this.player = player;
        th.start();
        sendMessage("SPACING" + Start.spacing);
    }
    public void run(){
        while (true){
            try {
                String str = listen();
                if (Objects.equals(str, null)){
                    int index = 0;
                    while (index < Server.clients.size()){
                        if (Objects.equals(Server.clients.get(index).player.id, player.id)){
                            Server.clients.remove(index);
                        }
                        index = index + 1;
                    }
                    closeConnection();
                }else{
                    requests.add(str);
                }
            } catch (IOException e) {
                e.printStackTrace();
                th.stop();
            }
        }
    }
    public void sendMessage(String msg) throws IOException {
        output.println(msg);
    }
    public void closeConnection() throws IOException {
        th.stop();
        output.close();
        input.close();
        clientSocket.close();
    }
    public String listen() throws IOException {
        String str = null;
        str = input.readLine();
        return str;
    }
}