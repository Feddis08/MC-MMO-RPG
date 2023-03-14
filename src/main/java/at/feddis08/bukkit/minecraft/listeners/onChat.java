package at.feddis08.bukkit.minecraft.listeners;

import at.feddis08.tools.Rank_api;
import at.feddis08.tools.io.database.Functions;
import at.feddis08.tools.io.database.objects.PlayerObject;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class onChat {
    public static void onChat(AsyncPlayerChatEvent event) throws SQLException, IOException {
        event.setCancelled(true);
        String chatMessage = event.getMessage();
        Player player = event.getPlayer();
        PlayerObject dbPlayer = null;
        dbPlayer = Functions.getPlayer("id", player.getUniqueId().toString());
        if (!(Objects.equals(dbPlayer.didStartup, "true") || Objects.equals(dbPlayer.didStartup, "false") || Objects.equals(dbPlayer.didStartup, ""))){
            player.kickPlayer("please rejoin");
        }
        if (Objects.equals(dbPlayer.didStartup, "true")){
            if (Rank_api.isPlayer_allowedTo(dbPlayer.id, "doChat") || Rank_api.isPlayer_allowedTo(dbPlayer.id, "*")) {
                dbPlayer.send_chat_message(event.getMessage());
            }else{
                player.sendMessage(ChatColor.RED + "You need the permission: 'doChat'!");
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
