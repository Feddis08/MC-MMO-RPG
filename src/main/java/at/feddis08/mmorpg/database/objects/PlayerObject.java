package at.feddis08.mmorpg.database.objects;

import at.feddis08.mmorpg.MMORPG;
import at.feddis08.mmorpg.database.Functions;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;

import java.sql.SQLException;
import java.util.SplittableRandom;

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
    public void destroy_block (BlockBreakEvent event){
        MMORPG.debugLog("da " + event.getPlayer().getItemInHand().getType().name());
    }
}
