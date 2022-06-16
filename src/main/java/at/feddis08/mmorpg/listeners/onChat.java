package at.feddis08.mmorpg.listeners;

import at.feddis08.mmorpg.MMORPG;
import at.feddis08.mmorpg.commands.Rank;
import at.feddis08.mmorpg.database.Functions;
import at.feddis08.mmorpg.database.objects.PlayerObject;
import at.feddis08.mmorpg.database.objects.RankObject;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.checkerframework.checker.units.qual.C;

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
            if (Rank.isPlayer_allowedTo(dbPlayer.id, "doChat") || Rank.isPlayer_allowedTo(dbPlayer.id, "*")) {

            RankObject dbRank = Functions.getRank("name", dbPlayer.player_rank);
            ChatColor color_prefix = getChatColor(dbRank.prefix_color);
            ChatColor color_rank = getChatColor(dbRank.rank_color);
                    event.setFormat(ChatColor.GRAY + "[" + color_prefix + dbRank.prefix + ChatColor.GRAY + "][" + color_rank + dbPlayer.display_name + ChatColor.GRAY + "]" + ChatColor.BLUE + ": "
                        + ChatColor.YELLOW + chatMessage + ChatColor.GRAY + " [" + Instant.now().toString() + "]");
            }else{
                player.sendMessage(ChatColor.RED + "You need the permission: 'doMail'!");
                event.setCancelled(true);
            }
        }else{
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "Please do the startup. " + ChatColor.GOLD + "/startup");
        }
        }
        public static ChatColor getChatColor(String color){
            if (Objects.equals(color, "dark_purple")){
                return ChatColor.DARK_PURPLE;
            }
            if (Objects.equals(color, "green")){
                return ChatColor.GREEN;
            }
            if (Objects.equals(color, "gray")){
                return ChatColor.GRAY;
            }
            if (Objects.equals(color, "gold")){
                return ChatColor.GOLD;
            }
            if (Objects.equals(color, "aqua")){
                return ChatColor.AQUA;
            }
            if (Objects.equals(color, "blue")){
                return ChatColor.BLUE;
            }
            if (Objects.equals(color, "black")){
                return ChatColor.BLACK;
            }
            if (Objects.equals(color, "red")){
                return ChatColor.RED;
            }
            if (Objects.equals(color, "magic")){
                return ChatColor.MAGIC;
            }
            return ChatColor.GRAY;
        }
}
