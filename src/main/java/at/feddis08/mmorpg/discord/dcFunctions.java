package at.feddis08.mmorpg.discord;

import org.javacord.api.entity.channel.ServerTextChannel;

public class dcFunctions {
    public static void send_message_in_channel(String channel_id, String message){
        ServerTextChannel t = DISCORD.api.getServerTextChannelById(channel_id).get();
        t.sendMessage(message);
    }
}
