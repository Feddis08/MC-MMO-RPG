package at.feddis08.bukkit.minecraft.scoreboads;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class BlocksInfoScoreboard {
    private static Scoreboard board;
    public static void setPlayerScoreboard(Player player, String block_count, String level, String block_name, String missing_blocks){
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();
        Objective obj = board.registerNewObjective("BlocksInfoBoard-01", "dummy", "Blocks");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        Score score1 = obj.getScore(ChatColor.AQUA + "Block_name: " + block_name);
        score1.setScore(4);
        Score score2 = obj.getScore(ChatColor.AQUA + "Level: " + level);
        score2.setScore(3);
        Score score3 = obj.getScore(ChatColor.AQUA + "Block_count " + block_count);
        score3.setScore(2);
        Score score4 = obj.getScore(ChatColor.AQUA + "Missing_blocks " + missing_blocks);
        score4.setScore(1);
        BlocksInfoScoreboard.board = board;
        player.setScoreboard(BlocksInfoScoreboard.board);
    }
}
