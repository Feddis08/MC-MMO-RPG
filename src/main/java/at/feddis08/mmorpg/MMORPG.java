package at.feddis08.mmorpg;

import at.feddis08.mmorpg.commands.*;
import at.feddis08.mmorpg.database.*;
import at.feddis08.mmorpg.database.objects.PlayerInWorlds;
import at.feddis08.mmorpg.database.objects.RankObject;
import at.feddis08.mmorpg.database.objects.WorldObject;
import at.feddis08.mmorpg.inventories.getBakerInv;
import at.feddis08.mmorpg.inventories.getJobInv;
import at.feddis08.mmorpg.listeners.Listeners;
import at.feddis08.mmorpg.scoreboads.BlocksInfoScoreboard;
import at.feddis08.mmorpg.tools.StartLoadWorld;
import at.feddis08.mmorpg.tools.WorldAutoLoad;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;

public final class MMORPG extends JavaPlugin {

    public static String prefix = "MMO-RPG: ";
    public static boolean enabled = false;
    @Override
    public void onEnable() {
        // Plugin startup logic
        consoleLog("Starting...");
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

/*
        getCommand("install").setExecutor(new install());
        Collection<? extends Player> players = getServer().getOnlinePlayers();
        for (Player player : players){
            consoleLog(player.getUniqueId().toString());
            PlayerObject dbPlayer = HibernateFunctions.getPlayerByUUID(player.getUniqueId().toString());
            if (!(dbPlayer.player_rank == "owner") || (!(player.isOp()))){
                player.kickPlayer("The MMORPG-Server restarts. Please reconnect!");
            }else{
                player.sendMessage("The MMO-RPG restarts");
            }
        }
 */

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
        consoleLog("Stopping...");
    }
    public static void consoleLog(String log){
        Bukkit.getConsoleSender().sendMessage(prefix + log);
    }
}
