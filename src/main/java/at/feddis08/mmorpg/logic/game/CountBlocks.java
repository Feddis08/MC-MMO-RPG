package at.feddis08.mmorpg.logic.game;

import at.feddis08.mmorpg.database.Functions;
import at.feddis08.mmorpg.database.objects.Block_break_countObject;
import at.feddis08.mmorpg.minecraft.scoreboads.BlocksInfoScoreboard;
import org.bukkit.event.block.BlockBreakEvent;

import java.sql.SQLException;
import java.util.Objects;

public class CountBlocks {
    public static void countBlock(BlockBreakEvent event) throws SQLException {
        String block_count = "0";
        String block_name = "";
        if (checkIfBlockIsInDb(event.getBlock().getType().name(), event.getPlayer().getUniqueId().toString())){
            Block_break_countObject dbObj = Functions.getBlock_break_count("player_id", event.getPlayer().getUniqueId().toString(), "block_name", event.getBlock().getType().name());
            Functions.updateWhereAnd("block_break_counter", "count", String.valueOf(dbObj.count + 1), event.getPlayer().getUniqueId().toString(), "player_id", event.getBlock().getType().name(), "block_name");
        }else{
            Block_break_countObject dbObj = new Block_break_countObject();
            dbObj.block_name = event.getBlock().getType().name();
            dbObj.count = 1;
            dbObj.player_id = event.getPlayer().getUniqueId().toString();
            Functions.createBlock_break_count(dbObj);
        }
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
