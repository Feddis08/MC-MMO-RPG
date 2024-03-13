package at.feddis08.tools.discord;

import at.feddis08.Boot;
import at.feddis08.bukkit.MMORPG;
import at.feddis08.tools.Rank_api;
import at.feddis08.tools.io.database.Functions;
import at.feddis08.tools.io.database.objects.Discord_playerObject;
import at.feddis08.tools.io.database.objects.PlayerObject;
import at.feddis08.tools.io.database.objects.RankObject;
import at.feddis08.bukkit.minecraft.tools.Methods;
import at.feddis08.tools.remote_interface.server.socket.Server;
import org.bukkit.ChatColor;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class MessageListeners {
    public static void create_message_listener() {
        DISCORD.api.addMessageCreateListener(event -> {
            if (!(String.valueOf(event.getMessage().getChannel().getId()).equals(DISCORD.config.server_log))) {
                if (event.isPrivateMessage()) {
                    Boot.consoleLog("DISCORD: got private_message: ; From: " + event.getMessage().getAuthor().getName() + "#" + event.getMessage().getAuthor().asUser().get().getMentionTag() + " ; Content: " + event.getMessage().getContent());
                } else {
                    Boot.consoleLog("DISCORD: got server_message: ; Server: " + event.getServer().get().getName() + " ; Channel: " + event.getMessage().getChannel().asServerChannel().get().getName() + " ; From: " + event.getMessage().getAuthor().getName() + "#" + event.getMessage().getAuthor().asUser().get().getMentionTag() + " ; Content: " + event.getMessage().getContent());
                }
            }
        });
        DISCORD.api.addMessageCreateListener(event -> {
            if (event.getMessageContent().equals("!help")) {
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
        DISCORD.api.addMessageCreateListener(event -> {
            if (!(event.getMessage().getAuthor().getId() == DISCORD.api.getYourself().getId())) {
                if (event.isPrivateMessage()) {
                    String[] message = event.getMessage().getContent().split(" ");
                    if (Objects.equals(message[0], "!startup")) {
                        if (message.length == 2) {
                            try {
                                Discord_playerObject dbDiscord_playerObject = Functions.getDiscordPlayer("discord_id", String.valueOf(event.getMessage().getAuthor().getId()));
                                if (dbDiscord_playerObject.discord_id == null) {
                                    dbDiscord_playerObject.discord_id = String.valueOf(event.getMessage().getAuthor().getId());
                                    dbDiscord_playerObject.display_name = message[1];
                                    Functions.createDiscordPlayer(dbDiscord_playerObject);
                                } else {
                                    event.getMessage().getAuthor().asUser().get().sendMessage("You already did the startup!");
                                }
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                    if (Objects.equals(message[0], "!link")) {
                        if (message.length == 2) {
                            try {
                                Discord_playerObject dbDiscord_playerObject = Functions.getDiscordPlayer("discord_id", String.valueOf(event.getMessage().getAuthor().getId()));
                                if (dbDiscord_playerObject.discord_id == null) {
                                    event.getMessage().getAuthor().asUser().get().sendMessage("You have to do the startup with: !startup <a_name>");
                                } else {
                                    PlayerObject dbPlayer = Functions.getPlayer("display_name", message[1]);
                                    if (Objects.equals(dbPlayer.display_name, message[1])) {
                                        dbDiscord_playerObject.want_link_id = dbPlayer.id;
                                        DISCORD.discord_playerObjects.add(dbDiscord_playerObject);
                                        Objects.requireNonNull(MMORPG.Server.getPlayer(dbPlayer.player_name)).sendMessage(ChatColor.YELLOW + event.getMessage().getAuthor().getName() + " / " + String.valueOf(event.getMessage().getAuthor().getId()) + " wants to link with you! Type /discord link_agree <discord_id>");
                                        event.getMessage().getAuthor().asUser().get().sendMessage("Please agree the request in minecraft!");
                                    } else {
                                        event.getMessage().getAuthor().asUser().get().sendMessage("This user does not excites!");
                                    }
                                }
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                } else {
                    try {
                        Discord_playerObject dbDiscord_playerObject = Functions.getDiscordPlayer("discord_id", String.valueOf(event.getMessage().getAuthor().getId()));
                        PlayerObject dbPlayer1 = Functions.getPlayer("id", dbDiscord_playerObject.id);
                        RankObject dbRank = Functions.getRank("name", dbPlayer1.player_rank);
                        if (dbPlayer1.id == null) {
                            event.getMessage().delete();
                            event.getMessage().getAuthor().asUser().get().sendMessage("Please link your minecraft account and your discord account with: !link <in_game_name>");
                        } else {
                            if (Rank_api.isPlayer_allowedTo(dbPlayer1.id, "doChatDc") || Rank_api.isPlayer_allowedTo(dbPlayer1.id, "*")) {
                                String[] message = event.getMessage().getContent().split(" ");
                                if (event.getMessageContent().equalsIgnoreCase("ping")) {
                                    event.getChannel().sendMessage("Pong!");
                                }
                                if (event.getMessageContent().equals("!help")) {
                                    event.getChannel().sendMessage(
                                            "```" +
                                                    "commands: " +
                                                    "\n" +
                                                    "!play <youtube_link> #You have to be in a channel" +
                                                    "\n" +
                                                    "```"
                                    );
                                }
                                if (String.valueOf(event.getChannel().getId()).equals(DISCORD.config.chat)) {
                                    if (!(event.getMessage().getAuthor().getId() == DISCORD.api.getYourself().getId())) {
                                        Server.broadcast_chat_message("[" + dbRank.prefix + "][" + dbPlayer1.display_name + "]" + ": "
                                                + event.getMessage().getContent() + " [" + Methods.getTime() + "]");
                                        MMORPG.Server.broadcastMessage(ChatColor.GRAY + "[" + ChatColor.BLUE + "DISCORD" + ChatColor.GRAY + "][" + ChatColor.GREEN + event.getMessage().getAuthor().getName() + ChatColor.GRAY + "]: " + ChatColor.YELLOW + event.getMessage().getContent() + ChatColor.GRAY + " [" + Methods.getTime() + "]");
                                    }
                                }
                                if (event.getMessageContent().equalsIgnoreCase("!showOnlineMinecraftPlayers")) {
                                    if (Rank_api.isPlayer_allowedTo(dbPlayer1.id, "doShowOnlineMinecraftPlayers") || Rank_api.isPlayer_allowedTo(dbPlayer1.id, "*")) {
                                        try {
                                            ArrayList<PlayerObject> dbPlayers = Functions.getPlayers("online", "1");
                                            String result = "There are: \n ```";
                                            Integer index = 0;
                                            while (index < dbPlayers.size()) {
                                                PlayerObject dbPlayer = dbPlayers.get(index);
                                                result = result + "\n   [" + dbRank.prefix + "][" + dbPlayer.getDisplay_name() + "]";
                                                index = index + 1;
                                            }
                                            result = result + "\n ```";
                                            event.getChannel().sendMessage(result);
                                        } catch (SQLException e) {
                                            throw new RuntimeException(e);
                                        }
                                    } else {
                                        event.getMessage().delete();
                                        event.getMessage().getAuthor().asUser().get().sendMessage("Request perms from an admin!");
                                    }
                                }
                                if (Objects.equals(message[0], "!play")) {
                                    if (Rank_api.isPlayer_allowedTo(dbPlayer1.id, "doPlayMusic") || Rank_api.isPlayer_allowedTo(dbPlayer1.id, "*")) {
                                        dcFunctions.join_voice_channel_and_stream_audio(event.getMessage().getAuthor().getConnectedVoiceChannel().get().getId(), message[1]);
                                        event.getChannel().sendMessage("Join channel");
                                    } else {
                                        event.getMessage().delete();
                                        event.getMessage().getAuthor().asUser().get().sendMessage("Request perms from an admin!");
                                    }
                                }
                            } else {
                                event.getMessage().delete();
                                event.getMessage().getAuthor().asUser().get().sendMessage("Request perms from an admin!");
                            }
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        DISCORD.api.addMessageCreateListener(event -> {
            if (!(event.getMessage().getAuthor().getId() == DISCORD.api.getYourself().getId())) {
                try {
                    Discord_playerObject dbDiscord_playerObject = Functions.getDiscordPlayer("discord_id", String.valueOf(event.getMessage().getAuthor().getId()));
                    PlayerObject dbPlayer1 = Functions.getPlayer("id", dbDiscord_playerObject.id);
                    RankObject dbRank = Functions.getRank("name", dbPlayer1.player_rank);
                    if (dbPlayer1.id == null) {
                        event.getMessage().delete();
                        event.getMessage().getAuthor().asUser().get().sendMessage("Please link your minecraft account and your discord account with: !link <in_game_name>");
                    } else {
                        if (Rank_api.isPlayer_allowedTo(dbPlayer1.id, "mcban") || Rank_api.isPlayer_allowedTo(dbPlayer1.id, "*")) {
                            String[] message = event.getMessage().getContent().split(" ");
                            if (message[0].equalsIgnoreCase("!mcban")) {
                                if (message.length == 2) {
                                    try {
                                        PlayerObject targetPlayer = Functions.getPlayer("display_name", message[1]);
                                        if (targetPlayer.id != null) {
                                            // Ban the player
                                            Functions.banPlayer(targetPlayer);
                                            event.getChannel().sendMessage("Player " + targetPlayer.display_name + " has been banned.");
                                        } else {
                                            event.getChannel().sendMessage("Player " + message[1] + " was not found.");
                                        }
                                    } catch (SQLException e) {
                                        event.getChannel().sendMessage("There was an error while processing the command.");
                                        e.printStackTrace();
                                    }
                                } else {
                                    event.getChannel().sendMessage("Usage: !mcban <player>");
                                }
                            }
                        } else {
                            event.getMessage().delete();
                            event.getMessage().getAuthor().asUser().get().sendMessage("You don't have the permissions to do that");
                        }
                    }
                } catch (SQLException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        DISCORD.api.addMessageCreateListener(event -> {
            if (!(event.getMessage().getAuthor().getId() == DISCORD.api.getYourself().getId())) {
                try {
                    Discord_playerObject dbDiscord_playerObject = Functions.getDiscordPlayer("discord_id", String.valueOf(event.getMessage().getAuthor().getId()));
                    PlayerObject dbPlayer1 = Functions.getPlayer("id", dbDiscord_playerObject.id);
                    RankObject dbRank = Functions.getRank("name", dbPlayer1.player_rank);
                    if (dbPlayer1.id == null) {
                        event.getMessage().delete();
                        event.getMessage().getAuthor().asUser().get().sendMessage("Please link your minecraft account and your discord account with: !link <in_game_name>");
                    } else {
                        if (Rank_api.isPlayer_allowedTo(dbPlayer1.id, "ban") || Rank_api.isPlayer_allowedTo(dbPlayer1.id, "*")) {
                            String[] message = event.getMessage().getContent().split(" ");
                            if (message[0].equalsIgnoreCase("!ban")) {
                                if (message.length == 2) {
                                    try {
                                        User targetUser = DISCORD.api.getUserById(message[1]); // Assuming message[1] contains the user ID
                                        if (targetUser != null) {
                                            banUser(targetUser, event);
                                        } else {
                                            event.getChannel().sendMessage("User with ID " + message[1] + " was not found.");
                                        }
                                    } catch (NumberFormatException e) {
                                        event.getChannel().sendMessage("Invalid user ID.");
                                    }
                                } else {
                                    event.getChannel().sendMessage("Usage: !ban <user_id>");
                                }
                            }
                        } else {
                            event.getMessage().delete();
                            event.getMessage().getAuthor().asUser().get().sendMessage("You don't have the permissions to do that");
                        }
                    }
                } catch (SQLException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private static void banUser(User user, MessageCreateEvent event) {
        // Assuming DISCORD.api is your JDA instance
        DISCORD.api.getGuildById("your_guild_id").ban(user, 0).queue(
                success -> event.getChannel().sendMessage("User " + user.getAsTag() + " has been banned from the server.").queue(),
                error -> event.getChannel().sendMessage("An error occurred while trying to ban the user.").queue()
        );
    }
}
