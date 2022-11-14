package at.feddis08.mmorpg.io.database.objects;

import at.feddis08.mmorpg.MMORPG;
import at.feddis08.mmorpg.discord.DISCORD;
import at.feddis08.mmorpg.io.database.Functions;
import at.feddis08.mmorpg.io.database.Object_Manager;
import at.feddis08.mmorpg.minecraft.listeners.onChat;
import at.feddis08.mmorpg.minecraft.tools.Methods;
import at.feddis08.mmorpg.remote_interface.server.socket.Server;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

public class PlayerObject {
    public String id = null;
    public Integer stage = 0;
    public Integer realm = 0;
    public String current_world_id = "";
    public Integer gamemode = 0;
    public String player_rank = "";
    public String display_name = "";
    public String player_name = "";
    public String didStartup = "false";
    public String online = "";
    public String job = "";
    public Player player;

    public void init(Player player) throws SQLException {
        this.player = player;
        player.setResourcePack("https://www.dropbox.com/sh/f3ie51vsn6pi1wj/AAAsq6jQvAp3e-yF6DHmFF5Ja?dl=1");
        checkMails();
    }
    public void checkMails() throws SQLException {
        Integer mailsize =  Functions.getMails("receiver_id", id, "opened", "false").size();
        if (mailsize > 0){
            player.sendMessage(ChatColor.GOLD + "You have " + ChatColor.DARK_PURPLE + mailsize + ChatColor.GOLD + " unread mails!");
        }
    }
    public void send_chat_message(String message) throws SQLException, IOException {
        RankObject dbRank = Functions.getRank("name", player_rank);
        ChatColor color_prefix = onChat.getChatColor(dbRank.prefix_color);
        ChatColor color_rank = onChat.getChatColor(dbRank.rank_color);
        MMORPG.Server.broadcastMessage(ChatColor.GRAY + "[" + color_prefix + dbRank.prefix + ChatColor.GRAY + "][" + color_rank + display_name + ChatColor.GRAY + "]" + ChatColor.BLUE + ": "
                + ChatColor.YELLOW + message + ChatColor.GRAY + " [" + Methods.getTime() + "]");

        at.feddis08.mmorpg.discord.dcFunctions.send_message_in_channel(DISCORD.config.read_only_chat, "[" +  dbRank.prefix +  "][" + display_name + "]" + ": "
                + message + " [" + Methods.getTime() + "]");
        at.feddis08.mmorpg.discord.dcFunctions.send_message_in_channel(DISCORD.config.chat, "[" +  dbRank.prefix +  "][" + display_name + "]" + ": "
                + message + " [" + Methods.getTime() + "]");
        Server.broadcast_chat_message("[" +  dbRank.prefix +  "][" + display_name + "]" + ": "
                + message + " [" + Methods.getTime() + "]");

    }
    public String getCurrent_world_id(){
        Object_Manager.addRequest("player", "get", id);
        return current_world_id;
    }
    public String getPlayer_rank(){
        Object_Manager.addRequest("player", "get", id);
        return player_rank;
    }
    public String getPlayer_name(){
        Object_Manager.addRequest("player", "get", id);
        return player_name;
    }
    public String getDidStartup() {
        Object_Manager.addRequest("player", "get", id);
        return didStartup;
    }
    public String getDisplay_name() {
        Object_Manager.addRequest("player", "get", id);
        return display_name;
    }
    public String getOnline() {
        Object_Manager.addRequest("player", "get", id);
        return online;
    }
}
