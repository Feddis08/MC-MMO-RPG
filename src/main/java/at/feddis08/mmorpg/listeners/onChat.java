package at.feddis08.mmorpg.listeners;

import at.feddis08.mmorpg.MMORPG;
import at.feddis08.mmorpg.database.Functions;
import at.feddis08.mmorpg.database.objects.PlayerObject;
import at.feddis08.mmorpg.database.objects.RankObject;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.sql.SQLException;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;

public class onChat {
    public static void onChat(AsyncPlayerChatEvent event) throws SQLException {
        String chatMessage = event.getMessage();
        Player player = event.getPlayer();
        PlayerObject dbPlayer = null;
        dbPlayer = Functions.getPlayer("id", player.getUniqueId().toString());
        if (!(Objects.equals(dbPlayer.didStartup, "true") || Objects.equals(dbPlayer.didStartup, "false") || Objects.equals(dbPlayer.didStartup, ""))){
            event.setCancelled(true);
            player.kickPlayer("please rejoin");
        }
        if (Objects.equals(dbPlayer.didStartup, "true")){
            MMORPG.consoleLog("daw " + ChatColor.GRAY.toString() + " " + ChatColor.GRAY.getChar());
            RankObject dbRank = Functions.getRank("name", dbPlayer.player_rank);
                event.setFormat(ChatColor.GRAY + "[" + ChatColor.GREEN + dbRank.prefix + ChatColor.GRAY + "][" + ChatColor.GREEN + dbPlayer.display_name + ChatColor.GRAY + "]" + ChatColor.BLUE + ": "
                        + ChatColor.YELLOW + chatMessage + ChatColor.GRAY + " [" + Instant.now().toString() + "]");
        }else{
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "Please do the startup. " + ChatColor.GOLD + "/startup");
        }
        }
}
