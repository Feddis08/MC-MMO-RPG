package at.feddis08.bukkit;

import at.feddis08.Boot;
import at.feddis08.bukkit.cluster_com_client.Start_cluster_client;
import at.feddis08.bukkit.commands.*;
import at.feddis08.tools.discord.dcFunctions;
import at.feddis08.tools.io.database.*;
import at.feddis08.tools.discord.DISCORD;
import at.feddis08.bukkit.logic.scripts.VarObject;
import at.feddis08.bukkit.minecraft.listeners.Listeners;
import at.feddis08.bukkit.minecraft.tools.Methods;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import static at.feddis08.bukkit.logic.scripts.Main.script_start_by_event_name;

public final class MMORPG extends JavaPlugin{

    public static Thread thread1 = new Object_Manager();


    public static org.bukkit.Server Server;
    public static Plugin plugin;

    @Override
    public void onEnable() {

        Server = getServer();
        Boot.start(Server.getLogger(), false);
        try {
            Start_cluster_client.connect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // Plugin startup logic
        plugin = this;
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
        getCommand("setPortalTrack").setExecutor(new SetPortalTrack());
        getCommand("setWarp").setExecutor(new SetWarp());
        getCommand("warp").setExecutor(new Warp());
        getCommand("removeWarp").setExecutor(new RemoveWarp());
        getCommand("discord").setExecutor(new Discord());
        getCommand("server").setExecutor(new at.feddis08.bukkit.commands.Server());
        try {
            Methods.update_all_players_online_state();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            at.feddis08.bukkit.logic.scripts.Main.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (Boot.discord_bot_active){
            dcFunctions.send_message_in_channel(DISCORD.config.read_only_chat, "Server started and is running ...");
            dcFunctions.send_message_in_channel(DISCORD.config.chat, "<@&1000897321745260594> Server started and is running ...");
        }
    }
        @Override
    public void onDisable() {
        // Plugin shutdown logic
            try {
                Boot.stop();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    public static void shutdown() throws Exception {
        if (!Boot.config.is_in_network && Boot.config.enable_discord_bot){
            dcFunctions.send_message_in_channel(DISCORD.config.read_only_chat, "Server is closing...");
            dcFunctions.send_message_in_channel(DISCORD.config.chat, "<@&1000897321745260594> Server is closing...");
        }
        Boot.debugLog("Clearing spawners...");
        at.feddis08.bukkit.logic.game.mob_spawner.Main.clear_spawners();
        Boot.debugLog("Saving players...");
        Methods.update_all_players_online_state();
    }
}
