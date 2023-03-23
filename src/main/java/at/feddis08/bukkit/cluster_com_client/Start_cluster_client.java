package at.feddis08.bukkit.cluster_com_client;


import at.feddis08.Boot;
import at.feddis08.bukkit.cluster_com_client.socket.Node_client;

import java.io.IOException;

public class Start_cluster_client {
    public static Node_client nodeclient = null;

    public static void connect() throws IOException, InterruptedException {
        nodeclient = new Node_client();
        nodeclient.startConnection(Boot.config.network_master_ip, Boot.config.network_port);
    }
}