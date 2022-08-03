package at.feddis08.mmorpg;

import at.feddis08.mmorpg.commands.*;
import at.feddis08.mmorpg.io.database.*;
import at.feddis08.mmorpg.io.database.objects.RankObject;
import at.feddis08.mmorpg.discord.DISCORD;
import at.feddis08.mmorpg.io.files.Main;
import at.feddis08.mmorpg.io.files.file_objects.ConfigFileObject;
import at.feddis08.mmorpg.minecraft.listeners.Listeners;
import at.feddis08.mmorpg.minecraft.tools.StartLoadWorld;
import at.feddis08.mmorpg.minecraft.tools.WorldAutoLoad;
import org.bukkit.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public final class MMORPG extends JavaPlugin {

    public static ConfigFileObject config;
    public static String prefix = "MMO-RPG: ";
    public static boolean debugMode = true;
    public static Integer current_dev_version = 12;
    public static boolean enable_discord_bot = false;


    public static boolean discord_bot_active = false;

    public static Server Server;

    @Override
    public void onEnable() {
        // Plugin startup logic
        System.out.println("Current_dev_version: " + current_dev_version);
        try {
            Main.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Server = getServer();
        if (config.enable_discord_bot) {
            discord_bot_active = true;
            DISCORD.start_bot();
            debugLog("Discord_bot enabled");
        }else{
            debugLog("Discord_bot disabled");
        }
        consoleLog("Starting...");
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
            Rank.create_rank("default");
            Rank.set_prefix("default", "Player");
            Rank.add_rule("default", "doChat");
            Rank.add_rule("default", "doMail");
            Rank.add_rule("default", "doBreakBlockInMain");
            Rank.add_rule("default", "doSetPlayerInfoScoreboard");
        }
        RankObject dbRank2 = null;
        try {
            dbRank2 = Functions.getRank("id", "operator");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (Objects.equals(dbRank2.name, "operator")) {
            StartLoadWorld.loadWorld("main");
            try {
                WorldAutoLoad.checkAutoloadWorlds();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            Rank.create_rank("operator");
            Rank.add_rule("operator", "*");
            Rank.set_prefix("operator", "Operator");
            Rank.set_prefix_color("operator", "red");
        }

        getServer().getPluginManager().registerEvents(new Listeners(), this);
        getCommand("Test").setExecutor(new TestCommand());
        getCommand("rank").setExecutor(new Rank());
        getCommand("tpworld").setExecutor(new TpWorld());
        getCommand("loadWorld").setExecutor(new LoadWorld());
        getCommand("gm").setExecutor(new Gamemode());
        getCommand("startUp").setExecutor(new StartUp());
        getCommand("reset").setExecutor(new reset());
        getCommand("mail").setExecutor(new Mail());
        getCommand("setScoreboard").setExecutor(new SetScoreboard());
        getCommand("setInventoryTrack").setExecutor(new SetInventoryTrack());

    }
        @Override
    public void onDisable() {
        // Plugin shutdown logic
            shutdown();
        }
    public static void debugLog(String log){
        if (config.enable_debug_log){
            Bukkit.getConsoleSender().sendMessage("[" + config.console_prefix + "]: [Debug]: " + log);
            if (config.enable_discord_bot && discord_bot_active)
                at.feddis08.mmorpg.discord.dcFunctions.send_message_in_channel(DISCORD.config.server_log, ("[" + config.console_prefix + "]: [Debug]: " + log));
        }
    }
    public static void consoleLog(String log){
        Bukkit.getConsoleSender().sendMessage("[" + config.console_prefix + "]: [Log]: " + log);
        if (config.enable_discord_bot && discord_bot_active)
            at.feddis08.mmorpg.discord.dcFunctions.send_message_in_channel(DISCORD.config.server_log, ("[" + config.console_prefix + "]: [Log]: " + log));
    }
    public static void shutdown(){
        consoleLog("Shutdown...");
        debugLog("Disconnecting systems...");
        if (config.enable_discord_bot){
            DISCORD.api.disconnect();
            debugLog("Disconnected Discord_bot");
        }
        try {
            JDBC.myConn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        debugLog("Disconnected Database");
    }
}
