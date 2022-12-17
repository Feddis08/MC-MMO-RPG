package at.feddis08.mmorpg.minecraft.listeners;

import at.feddis08.mmorpg.MMORPG;
import at.feddis08.mmorpg.commands.Rank;
import at.feddis08.mmorpg.discord.DISCORD;
import at.feddis08.mmorpg.discord.dcFunctions;
import at.feddis08.mmorpg.io.database.*;
import at.feddis08.mmorpg.io.database.objects.PlayerObject;
import at.feddis08.mmorpg.io.database.objects.Player_balanceObject;
import at.feddis08.mmorpg.io.database.objects.RankObject;
import at.feddis08.mmorpg.io.database.objects.UserObject;
import at.feddis08.mmorpg.logic.scripts.Main;
import at.feddis08.mmorpg.logic.scripts.VarObject;
import at.feddis08.mmorpg.minecraft.tools.Methods;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.checkerframework.checker.units.qual.Time;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class connectionEvents {
    public static void onJoin(PlayerJoinEvent event) throws SQLException {
        Player player = event.getPlayer();
        player.setDisplayName("test");
        PlayerObject dbPlayer = null;
        dbPlayer = Functions.getPlayer("id", player.getUniqueId().toString());
        MMORPG.debugLog(dbPlayer.getPlayer_name() + " ds");
        event.setJoinMessage("");
        player.sendMessage("Hi, and welcome to our MMO-RPG minecraft-server: " + player.getName());
        if (Objects.equals(dbPlayer.id, null)) {
            Player_balanceObject player_balanceObject = new Player_balanceObject();
            player_balanceObject.player_id = player.getUniqueId().toString();
            player_balanceObject.pocket = 0;
            player_balanceObject.stock_market = 0;
            Functions.createPlayers_balance(player_balanceObject);
            AddPlayer.addPlayer(event);
            UserObject userObject = new UserObject();
            userObject.time_created = String.valueOf(System.currentTimeMillis());
            userObject.id = player_balanceObject.player_id;
            Functions.createUser(userObject);
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
            MMORPG.consoleLog("Player " + dbPlayer.display_name + " joined!");
            RankObject dbRank = Functions.getRank("name", dbPlayer.player_rank);
            String str = ChatColor.GRAY + "[" + onChat.getChatColor(dbRank.prefix_color) + dbRank.prefix + ChatColor.GRAY + "][" + onChat.getChatColor(dbRank.rank_color) + dbPlayer.display_name + ChatColor.GRAY + "]" + ChatColor.BLUE + ": "
                    + ChatColor.YELLOW + "joined the server!" + ChatColor.GRAY + " [" + Methods.getTime() + "]";
            MMORPG.Server.broadcastMessage(str);
            dcFunctions.send_message_in_channel(DISCORD.config.read_only_chat, "[" + dbRank.prefix + "][" + dbPlayer.display_name + "] joined the server!");
            dcFunctions.send_message_in_channel(DISCORD.config.chat, "[" + dbRank.prefix + "][" + dbPlayer.display_name + "] joined the server!");
            player.sendMessage("Hi, " + dbPlayer.display_name + " your current level is: " + dbPlayer.stage);
            Main.vars_AFTER_PLAYER_JOINED.add(new VarObject("player_id", "STRING", dbPlayer.id));
            Main.run_AFTER_PLAYER_JOINED = true;
        }
    }
    public static void onQuit(PlayerQuitEvent event) throws SQLException {
        Player player = event.getPlayer();
        PlayerObject dbPlayer = Functions.getPlayer("id", player.getUniqueId().toString());
        if (Objects.equals(dbPlayer.didStartup, "true")) {
            Functions.update("players", "online", "0", dbPlayer.id, "id");
            MMORPG.consoleLog("Player  " + dbPlayer.display_name + "  left!");
            RankObject dbRank = Functions.getRank("name", dbPlayer.player_rank);
            String str = ChatColor.GRAY + "[" + onChat.getChatColor(dbRank.prefix_color) + dbRank.prefix + ChatColor.GRAY + "][" + onChat.getChatColor(dbRank.rank_color) + dbPlayer.display_name + ChatColor.GRAY + "]" + ChatColor.BLUE + ": "
                    + ChatColor.YELLOW + "left the server!" + ChatColor.GRAY + " [" + Methods.getTime() + "]";
            MMORPG.Server.broadcastMessage(str);
            dcFunctions.send_message_in_channel(DISCORD.config.read_only_chat, "[" + dbRank.prefix + "][" + dbPlayer.display_name + "] left the server!");
            dcFunctions.send_message_in_channel(DISCORD.config.chat, "[" + dbRank.prefix + "][" + dbPlayer.display_name + "] left the server!");
        }
        event.setQuitMessage(ChatColor.AQUA + "User left the Realm: " + ChatColor.GREEN + player.getName());

    }

}
