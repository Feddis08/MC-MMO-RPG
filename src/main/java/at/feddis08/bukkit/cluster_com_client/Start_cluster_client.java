package at.feddis08.bukkit.cluster_com_client;


import at.feddis08.Boot;
import at.feddis08.bukkit.cluster_com_client.socket.Cluster_client;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class Start_cluster_client {
    public static Cluster_client client = null;

    public static void connect() throws IOException, InterruptedException, ExecutionException {
        client = new Cluster_client();
        client.startConnection(Boot.config.network_master_ip, Boot.config.network_port);
    }
}