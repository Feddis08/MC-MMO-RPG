package at.feddis08.mmorpg.discord;

import at.feddis08.mmorpg.MMORPG;
import at.feddis08.mmorpg.io.database.Functions;
import at.feddis08.mmorpg.io.database.objects.Discord_playerObject;
import at.feddis08.mmorpg.io.database.objects.PlayerObject;
import at.feddis08.mmorpg.io.database.objects.RankObject;
import at.feddis08.mmorpg.minecraft.tools.Methods;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Minecart;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class MessageListeners {
    public static void create_message_listener(){
        DISCORD.api.addMessageCreateListener(event -> {
            if (String.valueOf(event.getChannel().getId()).equals(DISCORD.config.chat)){
                if (!(event.getMessage().getAuthor().getId() == DISCORD.api.getYourself().getId())){
                    MMORPG.Server.broadcastMessage(ChatColor.GRAY + "[" + ChatColor.BLUE + "DISCORD" + ChatColor.GRAY + "][" + ChatColor.GREEN + event.getMessage().getAuthor().getName() + ChatColor.GRAY + "]: " + ChatColor.YELLOW + event.getMessage().getContent() + ChatColor.GRAY + " [" + Methods.getTime() + "]");
                }
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
            if (!(String.valueOf(event.getMessage().getChannel().getId()).equals(DISCORD.config.server_log))){
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
            if (event.getMessageContent().equalsIgnoreCase("!showOnlineMinecraftPlayers")) {
                try {
                    ArrayList<PlayerObject> dbPlayers = Functions.getPlayers("online", "1");
                    String result = "There are: \n ```";
                    Integer index = 0;
                    while (index < dbPlayers.size()){
                        PlayerObject dbPlayer = dbPlayers.get(index);
                        RankObject dbRank = Functions.getRank("name", dbPlayer.player_rank);
                        result = result + "\n   [" + dbRank.prefix + "][" + dbPlayer.getDisplay_name() + "]";
                        index = index + 1;
                    }
                    result = result + "\n ```";
                    event.getChannel().sendMessage(result);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        DISCORD.api.addMessageCreateListener(event -> {
            if (event.getMessageContent().equals("Hello")){
                event.getChannel().sendMessage("World!");
            }
        });
        DISCORD.api.addMessageCreateListener(event -> {
            if (event.isPrivateMessage()){
                String[] message = event.getMessage().getContent().split(" ");
                if (Objects.equals(message[0], "!startup")) {
                    if (message.length == 2){
                        try {
                            Discord_playerObject dbDiscord_playerObject = Functions.getDiscordPlayer("discord_id", String.valueOf(event.getMessage().getAuthor().getId()));
                            if (dbDiscord_playerObject.discord_id == null){
                                dbDiscord_playerObject.discord_id = String.valueOf(event.getMessage().getAuthor().getId());
                                dbDiscord_playerObject.display_name = message[1];
                                Functions.createDiscordPlayer(dbDiscord_playerObject);
                            }else{
                                event.getMessage().getAuthor().asUser().get().sendMessage("You already did the startup!");
                            }
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                if (Objects.equals(message[0], "!link")) {
                    if (message.length == 2){
                        try {
                            Discord_playerObject dbDiscord_playerObject = Functions.getDiscordPlayer("discord_id", String.valueOf(event.getMessage().getAuthor().getId()));
                            if (dbDiscord_playerObject.discord_id == null){
                                event.getMessage().getAuthor().asUser().get().sendMessage("You have to do the startup!");
                            }else{
                                PlayerObject dbPlayer = Functions.getPlayer("display_name", message[1]);
                                if (Objects.equals(dbPlayer.display_name, message[1])){
                                    dbDiscord_playerObject.want_link_id = dbPlayer.id;
                                    DISCORD.discord_playerObjects.add(dbDiscord_playerObject);
                                    Objects.requireNonNull(MMORPG.Server.getPlayer(dbPlayer.player_name)).sendMessage(ChatColor.YELLOW + event.getMessage().getAuthor().getName() + " / " + String.valueOf(event.getMessage().getAuthor().getId()) + " wants to link with you! Type /discord link_agree <discord_id>");
                                    event.getMessage().getAuthor().asUser().get().sendMessage("Please agree the request in minecraft!");
                                }else{
                                    event.getMessage().getAuthor().asUser().get().sendMessage("This user does not excites!");
                                }
                            }
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
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
