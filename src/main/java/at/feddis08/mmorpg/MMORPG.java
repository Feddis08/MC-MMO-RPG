package at.feddis08.mmorpg;

import at.feddis08.mmorpg.commands.*;
import at.feddis08.mmorpg.database.*;
import at.feddis08.mmorpg.database.objects.RankObject;
import at.feddis08.mmorpg.discord.DISCORD;
import at.feddis08.mmorpg.minecraft.listeners.Listeners;
import at.feddis08.mmorpg.minecraft.tools.StartLoadWorld;
import at.feddis08.mmorpg.minecraft.tools.WorldAutoLoad;
import org.bukkit.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.ServerSocket;
import java.sql.SQLException;
import java.util.Objects;

public final class MMORPG extends JavaPlugin {

    public static String prefix = "MMO-RPG: ";
    public static boolean debugMode = true;
    public static Integer current_dev_version = 1;
    public static boolean enable_discord_bot = false;

    public static Server Server;

    @Override
    public void onEnable() {
        // Plugin startup logic
        debugLog("Current_dev_version: " + current_dev_version);

        Server = getServer();
        if (enable_discord_bot) {
            debugLog("Discord_bot enabled");
            //DISCORD.start_bot();
        }else{
            debugLog("Discord_bot disabled");
        }
        consoleLog("Starting...");
        if (debugMode){
            consoleLog("DebugMode enabled...");
        }else{
            consoleLog("DebugMode disabled...");
        }
        JDBC.connectToDb("10.0.1.46", "3306", "MMORPG", "MMORPG", "felix123");
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
    }
        @Override
    public void onDisable() {
        // Plugin shutdown logic
            shutdown();
        }
    public static void debugLog(String log){
        if (debugMode){
            Bukkit.getConsoleSender().sendMessage(prefix + "Debug: " + log);
            if (enable_discord_bot)
                at.feddis08.mmorpg.discord.dcFunctions.send_message_in_channel(DISCORD.server_log, (prefix + "Debug: " + log));
        }
    }
    public static void consoleLog(String log){
        Bukkit.getConsoleSender().sendMessage(prefix + "Log: " + log);
        if (enable_discord_bot)
            at.feddis08.mmorpg.discord.dcFunctions.send_message_in_channel(DISCORD.server_log, (prefix + "Log: " + log));
    }
    public static void shutdown(){
        consoleLog("Shutdown...");
        debugLog("Disconnecting systems...");
        if (enable_discord_bot){
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
