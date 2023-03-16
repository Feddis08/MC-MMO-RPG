package at.feddis08.bungeecord;

import at.feddis08.Boot;
import at.feddis08.tools.io.database.objects.PlayerObject;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

public class Methods {


    public static boolean sendPlayerToServer(PlayerObject dbPlayer, String server_name) {
        boolean fail = true;
        try {
            ProxiedPlayer player = BUNGEE.proxyServer.getPlayer(UUID.fromString(dbPlayer.id));
            ServerInfo serverInfo = BUNGEE.proxyServer.getServerInfo(server_name);
            player.connect(serverInfo);
            fail = false;
            Boot.debugLog("Send player: " + dbPlayer.display_name + " to server " + server_name);
        }catch (Exception e) {
            Boot.debugLog("Failed to send player: " + dbPlayer.display_name + " to server " + server_name);
        }
        return fail;
    }


}
