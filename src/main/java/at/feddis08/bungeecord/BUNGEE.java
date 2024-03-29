package at.feddis08.bungeecord;

import at.feddis08.Boot;
import at.feddis08.bukkit.minecraft.tools.Methods;
import at.feddis08.bungeecord.cluster_com_server.Start_cluster_server;
import at.feddis08.tools.discord.DISCORD;
import at.feddis08.tools.discord.dcFunctions;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.IOException;
import java.sql.SQLException;

public class BUNGEE extends Plugin {
    public static ProxyServer proxyServer;
    @Override
    public void onEnable() {
        proxyServer = getProxy();
        getLogger().info("Starting the plugin in Bungee mode.");
        try {
            Boot.start(getLogger(), true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            Start_cluster_server.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void onDisable(){
        try {
            Boot.stop();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static void shutdown(){
        if (Boot.config.enable_discord_bot){
            dcFunctions.send_message_in_channel(DISCORD.config.read_only_chat, "Server is closing...");
            dcFunctions.send_message_in_channel(DISCORD.config.chat, "<@&1000897321745260594> Server is closing...");
        }
    }
}