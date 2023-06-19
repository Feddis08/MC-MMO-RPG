package at.feddis08;

import at.feddis08.bukkit.MMORPG;
import at.feddis08.bukkit.cluster_com_client.Start_cluster_client;
import at.feddis08.bukkit.cluster_com_client.socket.Cluster_client;
import at.feddis08.bukkit.logic.scripts.VarObject;
import at.feddis08.bukkit.minecraft.tools.Methods;
import at.feddis08.bungeecord.BUNGEE;
import at.feddis08.tools.DataLogger;
import at.feddis08.tools.Rank_api;
import at.feddis08.tools.discord.DISCORD;
import at.feddis08.tools.discord.dcFunctions;
import at.feddis08.tools.io.database.Functions;
import at.feddis08.tools.io.database.JDBC;
import at.feddis08.tools.io.database.objects.RankObject;
import at.feddis08.tools.io.text_files.files.Main;
import at.feddis08.tools.io.text_files.files.file_objects.ClusterFileObject;
import at.feddis08.tools.io.text_files.files.file_objects.ConfigFileObject;
import at.feddis08.tools.remote_interface.server.Start;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Logger;

import static at.feddis08.bukkit.logic.scripts.Main.script_start_by_event_name;

public class Boot {


    public static ConfigFileObject config;
    public static ClusterFileObject cluster_config;
    public static String prefix = "MMO-RPG: ";
    public static boolean discord_bot_active = false;
    public static Logger logger;
    public static boolean is_bungee = false;

    public static void start(Logger logger2, boolean bungee) throws SQLException {
        is_bungee = bungee;
        logger = logger2;
        try {
            Main.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        consoleLog("Starting all systems...");
        if (config.enable_discord_bot) {
            discord_bot_active = true;
            DISCORD.start_bot();
            debugLog("Discord_bot enabled");
        }else{
            debugLog("Discord_bot disabled");
        }
        if (config.is_in_network) discord_bot_active = false;
        if (config.enable_debug_log){
            consoleLog("DebugMode enabled...");
        }else{
            consoleLog("DebugMode disabled...");
        }
        JDBC.connectToDb(config.database_ip, config.database_port, config.database_database_name, config.database_username, config.database_password);
        RankObject dbRank = null;
        try {
            dbRank = Functions.getRank("id", "default");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (Objects.equals(dbRank.name, "default")) {
        } else {
            Rank_api.create_rank("default");
            Rank_api.set_prefix("default", "Player");
            Rank_api.add_rule("default", "doChat");
        }
        RankObject dbRank2 = null;
        try {
            dbRank2 = Functions.getRank("id", "operator");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (Objects.equals(dbRank2.name, "operator")) {
        } else {
            Rank_api.create_rank("operator");
            Rank_api.add_rule("operator", "*");
            Rank_api.set_prefix("operator", "Operator");
            Rank_api.set_prefix_color("operator", "red");
        }
        try {
            at.feddis08.bukkit.logic.scripts.Main.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (config.is_in_network == false){
            try {
                Start.main();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try {
                    Thread t = new Thread("web"){
                    @Override
                    public void run() {
                        try {
                            at.feddis08.tools.remote_interface.web_service.Main.start();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                };
                t.start();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        Thread t = new Thread(){
          @Override
          public void run(){
              try {
                  while (true){
                      DataLogger.start();
                      Thread.sleep(1000);
                  }
              } catch (SQLException e) {
                  throw new RuntimeException(e);
              } catch (InterruptedException e) {
                  throw new RuntimeException(e);
              }
          }
        };
        t.start();
        DataLogger.start();

        consoleLog("Basic Systems started. Server boot almost done...");
    }

    public static void debugLog(String log){
        if (config.enable_debug_log){
            logger.info ("[" + config.console_prefix + "]: [Debug]: " + log);
            if (config.enable_discord_bot && discord_bot_active)
                at.feddis08.tools.discord.dcFunctions.send_message_in_channel(DISCORD.config.server_log, ("[" + config.console_prefix + "]: [Debug]: " + log));
        }
    }

    public static void consoleLog(String log){
        logger.info("[" + config.console_prefix + "]: [Log]: " + log);
        if (config.enable_discord_bot && discord_bot_active)
            at.feddis08.tools.discord.dcFunctions.send_message_in_channel(DISCORD.config.server_log, ("[" + config.console_prefix + "]: [Log]: " + log));
    }
    public static void stop() throws Exception {
        Boot.consoleLog("Shutdown...");
        Boot.debugLog("Call SERVER_STOP event...");
        ArrayList<VarObject> varObjects = new ArrayList<VarObject>();
        script_start_by_event_name ("SERVER_STOP", varObjects, false);
        Boot.debugLog("done");

        if (is_bungee){
            BUNGEE.shutdown();
        }else{
            MMORPG.shutdown();
            Boot.debugLog("Stopping cluster-connection...");
            Start_cluster_client.client.stopConnection("shutdown");
            Boot.debugLog("done...");
        }

        Boot.debugLog("Closing socket-interface server...");
        at.feddis08.tools.remote_interface.server.socket.Server.close();
        Boot.debugLog("done");
        Boot.consoleLog("Closing WebService...");
        at.feddis08.tools.remote_interface.web_service.Main.stop();
        Boot.debugLog("done");
        Boot.debugLog("Disconnecting systems...");
        if (Boot.config.enable_discord_bot){
            Boot.debugLog("Closing Discord_bot");
            DISCORD.api.disconnect();
            Boot.debugLog("done");
        }
        try {
            Boot.debugLog("Closing database");
            JDBC.myConn.close();
            Boot.debugLog("done");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Boot.consoleLog("done");
    }
}
