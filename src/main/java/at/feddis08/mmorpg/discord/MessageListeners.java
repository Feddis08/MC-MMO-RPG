package at.feddis08.mmorpg.discord;

import at.feddis08.mmorpg.MMORPG;
import at.feddis08.mmorpg.minecraft.tools.Methods;
import org.bukkit.ChatColor;

import java.util.Objects;

public class MessageListeners {
    public static void create_message_listener(){
        DISCORD.api.addMessageCreateListener(event -> {
            if (String.valueOf(event.getChannel().getId()).equals(DISCORD.chat)){
                MMORPG.Server.broadcastMessage(ChatColor.GRAY + "[" + ChatColor.BLUE + "DISCORD" + ChatColor.GRAY + "][" + ChatColor.GREEN + event.getMessage().getAuthor().getName() + ChatColor.GRAY + "]: " + ChatColor.YELLOW + event.getMessage().getContent() + ChatColor.GRAY + " [" + Methods.getTime() + "]");
            }
        });
        DISCORD.api.addMessageCreateListener(event -> {
            String[] message = event.getMessage().getContent().split(" ");
            if (Objects.equals(message[0], "!play")) {
                event.getChannel().sendMessage("Join channel");
                dcFunctions.join_voice_channel_and_stream_audio(event.getMessage().getAuthor().getConnectedVoiceChannel().get().getId(), message[1]);
            }
        });
        DISCORD.api.addMessageCreateListener(event -> {
            if (!(String.valueOf(event.getMessage().getChannel().getId()).equals(DISCORD.server_log))){
                if (event.isPrivateMessage()){
                    MMORPG.consoleLog("DISCORD: got private_message: ; From: " + event.getMessage().getAuthor().getName() + "#" + event.getMessage().getAuthor().asUser().get().getMentionTag() + " ; Content: " + event.getMessage().getContent());
                }else{
                    MMORPG.consoleLog("DISCORD: got server_message: ; Server: " + event.getServer().get().getName() + " ; Channel: " + event.getMessage().getChannel().asServerChannel().get().getName() + " ; From: " + event.getMessage().getAuthor().getName() + "#" + event.getMessage().getAuthor().asUser().get().getMentionTag() + " ; Content: " + event.getMessage().getContent());
                }
            }
        });
        DISCORD.api.addMessageCreateListener(event -> {
            if (event.getMessageContent().equalsIgnoreCase("ping")) {
                event.getChannel().sendMessage("Pong!");
            }
        });
        DISCORD.api.addMessageCreateListener(event -> {
            if (event.getMessageContent().equals("Hello")){
                event.getChannel().sendMessage("World!");
            }
        });
        DISCORD.api.addMessageCreateListener(event -> {
            if (event.getMessageContent().equals("!help")){
                event.getChannel().sendMessage(
                        "```" +
                                "commands: " +
                                "\n" +
                                "!play <youtube_link> #You have to be in a channel" +
                                "\n" +
                                "```"
                );
            }
        });
    }
}
