package at.feddis08.bungeecord;

import at.feddis08.Boot;
import at.feddis08.bungeecord.cluster_com_server.Start_cluster_server;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.IOException;

public class BUNGEE extends Plugin {
    public static ProxyServer proxyServer;
    @Override
    public void onEnable() {
        proxyServer = getProxy();
        getLogger().info("Starting the plugin in Bungee mode.");
        Boot.start(getLogger(), true);
        try {
            Start_cluster_server.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            at.feddis08.bukkit.logic.scripts.Main.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}