package at.feddis08.mmorpg.remote_interface.server;

import at.feddis08.mmorpg.MMORPG;
import at.feddis08.mmorpg.remote_interface.server.main.GameLoop;
import at.feddis08.mmorpg.remote_interface.server.socket.Server;

import java.io.IOException;

public class Start {
    public static Thread loop_thread;
    public static String status = "running";
    public static String server_name = "test";
    public static Integer port = 25566;
    public static String spacing = String.valueOf(Math.random());

    public static void main() throws IOException, InterruptedException {
        MMORPG.debugLog("Starting the remoteInterface server...");
        Server.th.start();
        loop_thread = new GameLoop();
        loop_thread.start();
    }

    public static void log(String log){
        System.out.println(log);
    }
}
