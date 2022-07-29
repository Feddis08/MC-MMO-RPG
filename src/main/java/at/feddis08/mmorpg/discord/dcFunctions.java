package at.feddis08.mmorpg.discord;

import at.feddis08.mmorpg.MMORPG;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.javacord.api.audio.AudioSource;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.channel.ServerVoiceChannel;

public class dcFunctions {
    public static void send_message_in_channel(String channel_id, String message){
        ServerTextChannel t = DISCORD.api.getServerTextChannelById(channel_id).get();
        t.sendMessage(message);
    }
    public static void join_voice_channel_and_stream_audio(String channel_id){
        ServerVoiceChannel channel = DISCORD.api.getServerVoiceChannelById(channel_id).get();
        channel.connect().thenAccept(audioConnection -> {
            DISCORD.api.getServerById("861647318017507348").get().unmuteYourself();
            if (DISCORD.api.getYourself().isMuted(DISCORD.api.getServerById("861647318017507348").get())){
                MMORPG.consoleLog("dawd");
            }
            // Create a player manager
            AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
            playerManager.registerSourceManager(new YoutubeAudioSourceManager());
            AudioPlayer player = playerManager.createPlayer();

            // Create an audio source and add it to the audio connection's queue
            AudioSource source = new LavaplayerAudioSource(DISCORD.api, player);
            audioConnection.setAudioSource(source);

            // You can now use the AudioPlayer like you would normally do with Lavaplayer, e.g.,
            playerManager.loadItem("https://www.youtube.com/watch?v=mWebB6zby6Y", new AudioLoadResultHandler() {
                @Override
                public void trackLoaded(AudioTrack track) {
                    player.playTrack(track);
                }

                @Override
                public void playlistLoaded(AudioPlaylist playlist) {
                    for (AudioTrack track : playlist.getTracks()) {
                        player.playTrack(track);
                    }
                }

                @Override
                public void noMatches() {
                    // Notify the user that we've got nothing
                    MMORPG.consoleLog("dafwewd");
                }

                @Override
                public void loadFailed(FriendlyException throwable) {
                    MMORPG.consoleLog("dwefsujawd");
                    // Notify the user that everything exploded
                }
            });
        }).exceptionally(e -> {
            // Failed to connect to voice channel (no permissions?)
            e.printStackTrace();
            return null;
        });
    }
}
