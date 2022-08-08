package at.feddis08.mmorpg.io.database.objects;

import at.feddis08.mmorpg.MMORPG;
import at.feddis08.mmorpg.io.database.Functions;
import at.feddis08.mmorpg.io.database.Object_Manager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import java.sql.SQLException;

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
        checkMails();
    }
    public void checkMails() throws SQLException {
        Integer mailsize =  Functions.getMails("receiver_id", id, "opened", "false").size();
        if (mailsize > 0){
            player.sendMessage(ChatColor.GOLD + "You have " + ChatColor.DARK_PURPLE + mailsize + ChatColor.GOLD + " unread mails!");
        }
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
