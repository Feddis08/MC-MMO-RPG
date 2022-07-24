package at.feddis08.mmorpg.discord;

public class MessageListeners {
    public static void create_message_listener(){
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
