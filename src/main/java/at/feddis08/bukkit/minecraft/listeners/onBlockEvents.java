package at.feddis08.bukkit.minecraft.listeners;

import at.feddis08.tools.Rank_api;
import at.feddis08.tools.io.database.Functions;
import at.feddis08.tools.io.database.objects.PlayerObject;
import at.feddis08.bukkit.logic.game.CountBlocks;
import at.feddis08.bukkit.logic.game.ore_mine.Main;
import at.feddis08.bukkit.logic.game.ore_mine.Mine;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import java.io.IOException;
import java.sql.SQLException;


public class onBlockEvents{
    public static void onBlockBreak(BlockBreakEvent event) throws SQLException, IOException, InterruptedException {
        Player player = event.getPlayer();
        PlayerObject dbPlayer = Functions.getPlayer("id", player.getUniqueId().toString());
        Mine mine = Main.get_mine_by_cords(event.getBlock().getWorld().getName(), event.getBlock().getX(), event.getBlock().getY(), event.getBlock().getZ());
        if (!(mine == null)){
            mine.break_block(dbPlayer.id);
        }else {
            if (Rank_api.isPlayer_allowedTo(dbPlayer.id, "doBreakBlock") || Rank_api.isPlayer_allowedTo(dbPlayer.id, "*")) {
                event.setCancelled(false);
                if (event.getPlayer().getWorld().getName().equals("main")) {
                    CountBlocks.countBlock(event);
                }
            } else {
                if (event.getPlayer().getWorld().getName().equals("main")) {
                    if (Rank_api.isPlayer_allowedTo(dbPlayer.id, "doBreakBlockInMain")) {
                        CountBlocks.countBlock(event);
                    } else {
                        player.sendMessage(ChatColor.RED + "You need the permission: 'doBreakBlock' or 'doBreakBlockInMain'!");
                        event.setCancelled(true);
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "You need the permission: 'doBreakBlock'!");
                    event.setCancelled(true);
                }
            }
        }
    }
}
