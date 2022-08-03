package at.feddis08.mmorpg.minecraft.listeners;

import at.feddis08.mmorpg.MMORPG;
import at.feddis08.mmorpg.commands.Rank;
import at.feddis08.mmorpg.io.database.*;
import at.feddis08.mmorpg.io.database.objects.PlayerObject;
import at.feddis08.mmorpg.io.database.objects.Player_balanceObject;
import org.bukkit.ChatColor;
import org.bukkit.Location;
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
            Player_balanceObject player_balanceObject = new Player_balanceObject();
            player_balanceObject.player_id = player.getUniqueId().toString();
            player_balanceObject.pocket = 0;
            player_balanceObject.stock_market = 0;
            Functions.createPlayers_balance(player_balanceObject);
            AddPlayer.addPlayer(event);
            Boolean rightTeleported = player.teleport(new Location(player.getServer().getWorld("world"), 0, 100, 0));
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
