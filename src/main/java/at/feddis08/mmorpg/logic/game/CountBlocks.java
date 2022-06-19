package at.feddis08.mmorpg.logic.game;

import at.feddis08.mmorpg.MMORPG;
import at.feddis08.mmorpg.database.Functions;
import at.feddis08.mmorpg.database.objects.Block_break_countObject;
import at.feddis08.mmorpg.scoreboads.BlocksInfoScoreboard;
import org.bukkit.event.block.BlockBreakEvent;

import java.sql.SQLException;
import java.util.Objects;

public class CountBlocks {
    public static void countBlock(BlockBreakEvent event) throws SQLException {
        String block_count = "0";
        String block_name = "";
        String level = "0";
        String missing_blocks = "0";
        if (checkIfBlockIsInDb(event.getBlock().getType().name(), event.getPlayer().getUniqueId().toString())){
            Block_break_countObject dbObj = Functions.getBlock_break_count("player_id", event.getPlayer().getUniqueId().toString(), "block_name", event.getBlock().getType().name());
            Functions.updateWhereAnd("block_break_counter", "count", String.valueOf(dbObj.count + 1), event.getPlayer().getUniqueId().toString(), "player_id", event.getBlock().getType().name(), "block_name");
            block_count = String.valueOf(dbObj.count);
            block_name = dbObj.block_name;
            level = String.valueOf(dbObj.level);
            if (String.valueOf(checkLevelUd(dbObj.count, dbObj.level)).equals(dbObj.level)){
                missing_blocks = String.valueOf(Math.pow((Integer.parseInt(level) + 1), 2) - Integer.parseInt(block_count));
            }else{
                Functions.updateWhereAnd("block_break_counter", "level", String.valueOf(checkLevelUd(dbObj.count, dbObj.level)), event.getPlayer().getUniqueId().toString(), "player_id", event.getBlock().getType().name(), "block_name");
                missing_blocks = String.valueOf(Math.pow((Integer.parseInt(level) + 1), 2) - Integer.parseInt(block_count));
            }
        }else{
            Block_break_countObject dbObj = new Block_break_countObject();
            dbObj.block_name = event.getBlock().getType().name();
            dbObj.count = 1;
            dbObj.level = 1;
            dbObj.player_id = event.getPlayer().getUniqueId().toString();
            Functions.createBlock_break_count(dbObj);
        }
        BlocksInfoScoreboard.setPlayerScoreboard(event.getPlayer(), block_count, level, block_name, missing_blocks);
    }

    public static Integer checkLevelUd(Integer count, Integer level){
        Integer currentLevel = Integer.parseInt(String.valueOf(Math.sqrt(count)).split("\\.")[0]);
        return currentLevel;
    }
    public static boolean checkIfBlockIsInDb(String block_name, String player_id) throws SQLException {
        Block_break_countObject dbObj = Functions.getBlock_break_count("player_id", player_id, "block_name", block_name);
        if (Objects.equals(dbObj.block_name, block_name)){
            return true;
        }else{
            return false;
        }
    }
}
