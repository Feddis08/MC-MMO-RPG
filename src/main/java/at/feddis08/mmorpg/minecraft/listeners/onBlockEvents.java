package at.feddis08.mmorpg.minecraft.listeners;

import at.feddis08.mmorpg.commands.Rank;
import at.feddis08.mmorpg.database.Functions;
import at.feddis08.mmorpg.database.objects.PlayerObject;
import at.feddis08.mmorpg.logic.game.CountBlocks;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import java.sql.SQLException;


public class onBlockEvents{
    public static void onBlockBreak(BlockBreakEvent event) throws SQLException {
        Player player = event.getPlayer();
        PlayerObject dbPlayer = Functions.getPlayer("id", player.getUniqueId().toString());
        if (Rank.isPlayer_allowedTo(dbPlayer.id, "doBreakBlock") || Rank.isPlayer_allowedTo(dbPlayer.id, "*")) {
            event.setCancelled(false);
            if (event.getPlayer().getWorld().getName().equals("main")){
                CountBlocks.countBlock(event);
            }
        }else{
            if (event.getPlayer().getWorld().getName().equals("main")){
                if (Rank.isPlayer_allowedTo(dbPlayer.id, "doBreakBlockInMain")){
                    CountBlocks.countBlock(event);
                }else{
                    player.sendMessage(ChatColor.RED + "You need the permission: 'doBreakBlock' or 'doBreakBlockInMain'!");
                    event.setCancelled(true);
                }
            }else{
                player.sendMessage(ChatColor.RED + "You need the permission: 'doBreakBlock'!");
                event.setCancelled(true);
            }
        }
    }
}
