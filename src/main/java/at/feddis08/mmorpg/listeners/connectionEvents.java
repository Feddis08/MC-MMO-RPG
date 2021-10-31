package at.feddis08.mmorpg.listeners;

import at.feddis08.mmorpg.MMORPG;
import at.feddis08.mmorpg.database.AddPlayer;
import at.feddis08.mmorpg.database.DataObject;
import at.feddis08.mmorpg.database.Functions;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.SQLException;
import java.util.Dictionary;

public class connectionEvents {
    public static void onJoin(PlayerJoinEvent event) throws SQLException {
        Player player = event.getPlayer();
        event.setJoinMessage(ChatColor.AQUA + "User joined the Realm: " + ChatColor.GREEN + player.getName());
        player.sendMessage("Hi, and welcome to our MMO-RPG minecraft-server: " + player.getName());
        DataObject dbPlayer = Functions.searchWithPlayerName(player.getName());
        if (dbPlayer.didStartup == null || dbPlayer.didStartup == 0) {
            AddPlayer.addPlayer(event);
            player.sendMessage("Hi, if you are new here, you have to run" + ChatColor.GOLD + " /startup " + ChatColor.GRAY + "in the chat!");
            MMORPG.consoleLog("New player: " + player.getName() + " logged in!");
        }else {
            player.sendMessage("Hi, " + dbPlayer.player_name + " your current level is: " + dbPlayer.level);
        }
    }
    public static void onQuit(PlayerQuitEvent event) throws SQLException {
        Player player = event.getPlayer();
        Functions.updatePlayerWithName("online", "0", player.getName());
        event.setQuitMessage(ChatColor.AQUA + "User left the Realm: " + ChatColor.GREEN + player.getName());

    }

}
