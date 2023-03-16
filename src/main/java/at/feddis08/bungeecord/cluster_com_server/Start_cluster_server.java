package at.feddis08.bungeecord.cluster_com_server;

import at.feddis08.Boot;
import at.feddis08.bungeecord.cluster_com_server.main.Client_event_loop;
import at.feddis08.bungeecord.cluster_com_server.socket.Server;

import java.io.IOException;

public class Start_cluster_server {
    public static Thread loop_thread;
    public static Integer port = Boot.config.network_port;

    public static void start() throws IOException, InterruptedException {
        Boot.debugLog("Starting com_cluster_server...");
        Server.th.start();
        loop_thread = new Client_event_loop();
        loop_thread.start();
    }
}
