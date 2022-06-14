package at.feddis08.mmorpg.listeners;

import at.feddis08.mmorpg.MMORPG;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class onBlockEvents{
    public static void onBlockBreak(BlockBreakEvent event){
        Player player = event.getPlayer();
        if (player.isOp()){
            event.setCancelled(false);
        }else{
            player.sendMessage("You cannot do this!");
            event.setCancelled(true);
        }
    }
}
