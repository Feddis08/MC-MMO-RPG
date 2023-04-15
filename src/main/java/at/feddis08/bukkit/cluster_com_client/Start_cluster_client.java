package at.feddis08.bukkit.cluster_com_client;


import at.feddis08.Boot;
import at.feddis08.bukkit.cluster_com_client.socket.Client;

import java.io.IOException;

public class Start_cluster_client {
    public static Client client = null;

    public static void connect() throws IOException, InterruptedException {
        client = new Client();
        client.startConnection(Boot.config.network_master_ip, Boot.config.network_port);
    }
}