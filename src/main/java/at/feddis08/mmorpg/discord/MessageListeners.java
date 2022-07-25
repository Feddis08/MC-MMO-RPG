package at.feddis08.mmorpg.discord;

import at.feddis08.mmorpg.MMORPG;
import at.feddis08.mmorpg.minecraft.tools.Methods;
import org.bukkit.ChatColor;

public class MessageListeners {
    public static void create_message_listener(){
        DISCORD.api.addMessageCreateListener(event -> {
            if (String.valueOf(event.getChannel().getId()).equals(DISCORD.chat)){
                MMORPG.Server.broadcastMessage(ChatColor.GRAY + "[" + ChatColor.BLUE + "DISCORD" + ChatColor.GRAY + "][" + ChatColor.GREEN + event.getMessage().getAuthor().getName() + ChatColor.GRAY + "]: " + ChatColor.YELLOW + event.getMessage().getContent() + ChatColor.GRAY + " [" + Methods.getTime() + "]");
            }
        });
        DISCORD.api.addMessageCreateListener(event -> {
            if (event.getMessageContent().equalsIgnoreCase("!ping")) {
                event.getChannel().sendMessage("Pong!");
            }
        });
        DISCORD.api.addMessageCreateListener(event -> {
            if (event.getMessageContent().equals("Hello")){
                event.getChannel().sendMessage("World!");
            }
        });
        DISCORD.api.addMessageCreateListener(event -> {
            if (event.getMessageContent().equals("Felix")){
                event.getChannel().sendMessage("Riemer");
            }
        });
        DISCORD.api.addMessageCreateListener(event -> {
            if (event.getMessageContent().equals("Leon")){
                event.getChannel().sendMessage("Montel");
            }
        });
    }
}
