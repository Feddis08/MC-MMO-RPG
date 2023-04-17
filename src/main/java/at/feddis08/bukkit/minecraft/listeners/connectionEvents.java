package at.feddis08.bukkit.minecraft.listeners;

import at.feddis08.Boot;
import at.feddis08.bukkit.MMORPG;
import at.feddis08.bukkit.cluster_com_client.Start_cluster_client;
import at.feddis08.bukkit.cluster_com_client.socket.Incoming_player;
import at.feddis08.tools.Rank_api;
import at.feddis08.tools.discord.DISCORD;
import at.feddis08.tools.discord.dcFunctions;
import at.feddis08.tools.io.database.*;
import at.feddis08.tools.io.database.objects.PlayerObject;
import at.feddis08.tools.io.database.objects.Player_balanceObject;
import at.feddis08.tools.io.database.objects.RankObject;
import at.feddis08.tools.io.database.objects.UserObject;
import at.feddis08.bukkit.logic.scripts.Main;
import at.feddis08.bukkit.logic.scripts.VarObject;
import at.feddis08.bukkit.minecraft.tools.Methods;
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
        Boot.debugLog(dbPlayer.getPlayer_name() + " ds");
        boolean joined_from_other_server = false;
        if (Start_cluster_client.client != null){
            for (Incoming_player i_player : Start_cluster_client.client.incoming_players){
                if (Objects.equals(i_player.player_id, player.getUniqueId().toString())){
                    i_player.arrived = true;
                    Start_cluster_client.client.player_joined = true;
                    joined_from_other_server = true;
                }
            }
        }
        if (!joined_from_other_server){
            event.setJoinMessage("");
            player.sendMessage("Hi, and welcome to our MMO-RPG minecraft-server: " + player.getName());
            if (Objects.equals(dbPlayer.id, null)) {
                AddPlayer.addPlayer(event);
                UserObject userObject = new UserObject();
                userObject.time_created = String.valueOf(System.currentTimeMillis());
                userObject.id = player.getUniqueId().toString();
                Functions.createUser(userObject);
                Boolean rightTeleported = player.teleport(new Location(player.getServer().getWorld("world"), 0, 100, 0));
                if (player.isOp()){
                    Rank_api.set_player_rank_from("operator", player.getUniqueId().toString());
                }else{
                    Rank_api.set_player_rank_from("default", player.getUniqueId().toString());
                }
                player.kickPlayer("Please rejoin!");
            }else{
                Functions.update("players", "online", "1", dbPlayer.id, "id");
            }
            Player_balanceObject player_balanceObject = Functions.getPlayers_balance("player_id", player.getUniqueId().toString());
            if (player_balanceObject.player_id == null){
                player_balanceObject.player_id = player.getUniqueId().toString();
                player_balanceObject.pocket = 0;
                player_balanceObject.stock_market = 0;
                Functions.createPlayers_balance(player_balanceObject);
            }
            if(Objects.equals(dbPlayer.didStartup, "false")){
                player.sendMessage("Hi, you have to run" + ChatColor.GOLD + " /startup " + ChatColor.GRAY + "in the chat!");
                Boot.consoleLog("New player: " + player.getName() + " logged in!");
            }else if(Objects.equals(dbPlayer.didStartup, "true")){
                dbPlayer.init(player);
                player.setDisplayName(dbPlayer.display_name);
                Boot.consoleLog("Player " + dbPlayer.display_name + " joined!");
                RankObject dbRank = Functions.getRank("name", dbPlayer.player_rank);
                String str = ChatColor.GRAY + "[" + onChat.getChatColor(dbRank.prefix_color) + dbRank.prefix + ChatColor.GRAY + "][" + onChat.getChatColor(dbRank.rank_color) + dbPlayer.display_name + ChatColor.GRAY + "]" + ChatColor.BLUE + ": "
                        + ChatColor.YELLOW + "joined the server!" + ChatColor.GRAY + " [" + Methods.getTime() + "]";
                MMORPG.Server.broadcastMessage(str);

                if (Boot.config.enable_discord_bot){
                    dcFunctions.send_message_in_channel(DISCORD.config.read_only_chat, "[" + dbRank.prefix + "][" + dbPlayer.display_name + "] joined the server!");
                    dcFunctions.send_message_in_channel(DISCORD.config.chat, "[" + dbRank.prefix + "][" + dbPlayer.display_name + "] joined the server!");
                }
                player.sendMessage(ChatColor.GRAY + "Hi, " + ChatColor.WHITE + dbPlayer.display_name + ChatColor.GRAY + " you have: " + ChatColor.GOLD + player_balanceObject.pocket + ChatColor.GRAY + " in your pocket.");
                Main.vars_AFTER_PLAYER_JOINED.add(new VarObject("player_id", "STRING", dbPlayer.id));
                Main.run_AFTER_PLAYER_JOINED = true;
            }
        }
    }
    public static void onQuit(PlayerQuitEvent event) throws SQLException {
        Player player = event.getPlayer();
        PlayerObject dbPlayer = Functions.getPlayer("id", player.getUniqueId().toString());
        if (Objects.equals(dbPlayer.didStartup, "true")) {
            Functions.update("players", "online", "0", dbPlayer.id, "id");
            Boot.consoleLog("Player  " + dbPlayer.display_name + "  left!");
            RankObject dbRank = Functions.getRank("name", dbPlayer.player_rank);
            String str = ChatColor.GRAY + "[" + onChat.getChatColor(dbRank.prefix_color) + dbRank.prefix + ChatColor.GRAY + "][" + onChat.getChatColor(dbRank.rank_color) + dbPlayer.display_name + ChatColor.GRAY + "]" + ChatColor.BLUE + ": "
                    + ChatColor.YELLOW + "left the server!" + ChatColor.GRAY + " [" + Methods.getTime() + "]";
            MMORPG.Server.broadcastMessage(str);

            if (Boot.config.enable_discord_bot) {
                dcFunctions.send_message_in_channel(DISCORD.config.read_only_chat, "[" + dbRank.prefix + "][" + dbPlayer.display_name + "] left the server!");
                dcFunctions.send_message_in_channel(DISCORD.config.chat, "[" + dbRank.prefix + "][" + dbPlayer.display_name + "] left the server!");
            }
        }
        event.setQuitMessage(ChatColor.AQUA + "User left the Realm: " + ChatColor.GREEN + player.getName());

    }

}
