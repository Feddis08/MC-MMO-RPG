package at.feddis08.mmorpg.discord;
import at.feddis08.mmorpg.MMORPG;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

public class DISCORD {
    public static  DiscordApi api;

    public static String text_channel_Allgemein_id = "861647318017507351";
    public static String server_log = "1000897762327547944";
    public static String read_only_chat = "1001122143112790046";
    public static String chat = "1001122305814040577";
    public static String voice1 = "1002536373225529365";
    public static String voice_channel = "861647318017507352";

    public static void start_bot(){
    //public static void main(String[] args) {
        api = new DiscordApiBuilder()
                .setToken("MTAwMDgxMzg3OTYwMjk4Mjk5Mw.GObeoF.pEnqkRk9SEsS6s_k0ZFNIWTXHasr1kYjpCaLKk")
                .login().join();
        MessageListeners.create_message_listener();
        //dcFunctions.send_message_in_channel(DISCORD.text_channel_Allgemein_id, "I'm back!");
    }
}