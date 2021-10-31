package at.feddis08.mmorpg;

import at.feddis08.mmorpg.commands.Gamemode;
import at.feddis08.mmorpg.commands.StartUp;
import at.feddis08.mmorpg.commands.TestCommand;
import at.feddis08.mmorpg.database.JDBC;
import at.feddis08.mmorpg.listeners.Listener;
import jdk.incubator.vector.VectorOperators;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public final class MMORPG extends JavaPlugin {

    public static String prefix = "MMO-RPG: ";
    @Override
    public void onEnable() {
        // Plugin startup logic
        consoleLog("Starting...");
        JDBC.connectToDb("192.168.1.100", "3306", "MMORPG", "MMORPG", "felix123");
        getServer().getPluginManager().registerEvents(new Listener(), this);
        getCommand("Test").setExecutor(new TestCommand());
        getCommand("gm").setExecutor(new Gamemode());
        getCommand("startUp").setExecutor(new StartUp());
    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
        consoleLog("Stopping...");
    }
    public static void consoleLog(String log){
        Bukkit.getConsoleSender().sendMessage(prefix + log);
    }
}
