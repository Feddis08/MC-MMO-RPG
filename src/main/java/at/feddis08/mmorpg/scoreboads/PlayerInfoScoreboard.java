package at.feddis08.mmorpg.scoreboads;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class PlayerInfoScoreboard {
    private static Scoreboard board;
    public static void setPlayerScoreboard(Player player){
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();
        Objective obj = board.registerNewObjective("BlocksInfoBoard-01", "dummy", "Blocks");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        Score score1 = obj.getScore(ChatColor.AQUA + "Block_name: " + block_name);
        score1.setScore(4);
        PlayerInfoScoreboard.board = board;
        player.setScoreboard(PlayerInfoScoreboard.board);
    }
}
