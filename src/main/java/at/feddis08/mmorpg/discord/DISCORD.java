package at.feddis08.mmorpg.discord;
import at.feddis08.mmorpg.MMORPG;
import at.feddis08.mmorpg.io.files.file_objects.Discord_ConfigFileObject;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

public class DISCORD {
    public static  DiscordApi api;

    public static Discord_ConfigFileObject config;

    public static void start_bot(){
    //public static void main(String[] args) {
        api = new DiscordApiBuilder()
                .setToken(MMORPG.config.discord_bot_token)
                .login().join();
        MessageListeners.create_message_listener();
        //dcFunctions.send_message_in_channel(DISCORD.text_channel_Allgemein_id, "I'm back!");
    }
}
