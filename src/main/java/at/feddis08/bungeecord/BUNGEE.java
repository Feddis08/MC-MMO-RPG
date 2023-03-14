package at.feddis08.bungeecord;

import at.feddis08.Boot;
import net.md_5.bungee.api.plugin.Plugin;
public class BUNGEE extends Plugin {
    @Override
    public void onEnable() {
        // You should not put an enable message in your plugin.
        // BungeeCord already does so
        getLogger().info("Starting the plugin in Bungee mode.");

        Boot.start(getLogger(), true);
    }
}