package at.feddis08.tools.discord;
import at.feddis08.Boot;
import at.feddis08.tools.io.database.objects.Discord_playerObject;
import at.feddis08.tools.io.text_files.files.file_objects.Discord_ConfigFileObject;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandInteraction;

import java.util.ArrayList;
import java.util.Objects;

public class DISCORD {
    public static  DiscordApi api;

    public static Discord_ConfigFileObject config;
    public static ArrayList<Discord_playerObject>  discord_playerObjects = new ArrayList<>();

    public static void start_bot(){
        api = new DiscordApiBuilder()
                .setToken(Boot.config.discord_bot_token)
                .setAllIntents()
                .login()
                .join();
        MessageListeners.create_message_listener();

        SlashCommand.with("ping", "A simple ping pong1 command!").createGlobal(api).join();

        api.addSlashCommandCreateListener(event -> {
            SlashCommandInteraction slashCommandInteraction = event.getSlashCommandInteraction();
            if (slashCommandInteraction.getCommandName().equals("ping")) {
                slashCommandInteraction.createImmediateResponder()
                        .setContent("Pong!")
                        .respond();
            }
        });
    }
    public static Discord_playerObject get_discordPlayer_by_discord_id(String discord_id){
        Integer index = 0;
        Discord_playerObject result = null;
        while (index < discord_playerObjects.size()){
            if (Objects.equals(discord_playerObjects.get(index).discord_id, discord_id)){
                result = discord_playerObjects.get(index);
            }
            index = index + 1;
        }
        return  result;
    }
    public static void remove_discordPlayer_in_want_list_by_discord_id(String discord_id){
        int index = 0;
        Discord_playerObject result = null;
        while (index < discord_playerObjects.size()) {
            if (Objects.equals(discord_playerObjects.get(index).discord_id, discord_id)) {
                discord_playerObjects.remove(index);
            }
            index = index + 1;
        }
    }

}
