package at.feddis08.mmorpg.minecraft.listeners;

import at.feddis08.mmorpg.MMORPG;
import at.feddis08.mmorpg.commands.Rank;
import at.feddis08.mmorpg.database.*;
import at.feddis08.mmorpg.database.objects.PlayerObject;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.SQLException;
import java.util.Objects;

public class connectionEvents {
    public static void onJoin(PlayerJoinEvent event) throws SQLException {
        Player player = event.getPlayer();
        player.setDisplayName("test");
        PlayerObject dbPlayer = null;
        dbPlayer = Functions.getPlayer("id", player.getUniqueId().toString());
        //event.setJoinMessage(ChatColor.AQUA + "User joined the Realm: " + ChatColor.GREEN + player.getName());
        player.sendMessage("Hi, and welcome to our MMO-RPG minecraft-server: " + player.getName());
        if (Objects.equals(dbPlayer.id, null)) {
            AddPlayer.addPlayer(event);
            if (player.isOp()){
                Rank.set_player_rank_from("operator", player.getUniqueId().toString());
            }else{
                Rank.set_player_rank_from("default", player.getUniqueId().toString());
            }
            player.kickPlayer("Please rejoin!");
        }else{
            Functions.update("players", "online", "1", dbPlayer.id, "id");
        }
        if(Objects.equals(dbPlayer.didStartup, "false")){
            player.sendMessage("Hi, if you are new here, you have to run" + ChatColor.GOLD + " /startup " + ChatColor.GRAY + "in the chat!");
            MMORPG.consoleLog("New player: " + player.getName() + " logged in!");
        }else if(Objects.equals(dbPlayer.didStartup, "true")){
            dbPlayer.init(player);
            player.sendMessage("Hi, " + dbPlayer.display_name + " your current level is: " + dbPlayer.stage);
        }
    }
    public static void onQuit(PlayerQuitEvent event) throws SQLException {
        Player player = event.getPlayer();
        PlayerObject dbPlayer = Functions.getPlayer("id", player.getUniqueId().toString());
        if (Objects.equals(dbPlayer.didStartup, "true")) {
            Functions.update("players", "online", "0", dbPlayer.id, "id");
            //ArrayList<PlayerInWorlds> worlds = Functions.getPlayerInWorlds("id", )
        }
        event.setQuitMessage(ChatColor.AQUA + "User left the Realm: " + ChatColor.GREEN + player.getName());

    }

}
