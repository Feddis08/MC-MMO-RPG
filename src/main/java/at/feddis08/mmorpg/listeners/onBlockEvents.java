package at.feddis08.mmorpg.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

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
