package at.feddis08.mmorpg.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class onChat {
    public static void onChat(AsyncPlayerChatEvent event){
        String chatMessage = event.getMessage();
        Player player = event.getPlayer();
        if (player.isOp()){
            event.setFormat(ChatColor.GRAY + "<" + ChatColor.RED + player.getDisplayName() + ChatColor.BLUE + "-->" + ChatColor.YELLOW + chatMessage + ChatColor.GRAY + ">");
        }else {
            event.setFormat(ChatColor.GRAY + "<" + ChatColor.GREEN + player.getDisplayName() + ChatColor.BLUE + "-->" + ChatColor.YELLOW + chatMessage + ChatColor.GRAY + ">");
        }
        }
}
