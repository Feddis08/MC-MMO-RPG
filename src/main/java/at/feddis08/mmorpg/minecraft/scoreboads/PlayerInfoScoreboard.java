package at.feddis08.mmorpg.minecraft.scoreboads;

import at.feddis08.mmorpg.database.Functions;
import at.feddis08.mmorpg.database.objects.PlayerObject;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.sql.SQLException;

public class PlayerInfoScoreboard {
    private static Scoreboard board;
    public static void setPlayerScoreboard(Player player) throws SQLException {
        PlayerObject dbPlayer = Functions.getPlayer("id", player.getUniqueId().toString());
            ScoreboardManager manager = Bukkit.getScoreboardManager();
            Scoreboard board = manager.getNewScoreboard();
            Objective obj = board.registerNewObjective("PlayerInfo-01", "dummy", "Info");
            obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        Score score5 = obj.getScore(ChatColor.AQUA + "ping: " + player.getPing());
        score5.setScore(5);
            Score score1 = obj.getScore(ChatColor.AQUA + "current world: " + dbPlayer.current_world_id);
            score1.setScore(4);
            Score score2 = obj.getScore(ChatColor.AQUA + "your rank: " + dbPlayer.player_rank);
            score2.setScore(3);
            Score score3 = obj.getScore(ChatColor.AQUA + "your name: " + dbPlayer.display_name);
            score3.setScore(2);
            Score score4 = obj.getScore(ChatColor.AQUA + "online: " + Bukkit.getOnlinePlayers().size());
            score4.setScore(1);
        PlayerInfoScoreboard.board = board;
            player.setScoreboard(PlayerInfoScoreboard.board);

    }
}
