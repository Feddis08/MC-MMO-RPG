package at.feddis08.mmorpg.listeners;

import at.feddis08.mmorpg.MMORPG;
import at.feddis08.mmorpg.commands.Rank;
import at.feddis08.mmorpg.database.Functions;
import at.feddis08.mmorpg.database.objects.PlayerObject;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.sql.SQLException;
import java.util.Objects;


public class onBlockEvents{
    public static void onBlockBreak(BlockBreakEvent event) throws SQLException {
        Player player = event.getPlayer();
        PlayerObject dbPlayer = Functions.getPlayer("id", player.getUniqueId().toString());
        if (Rank.isPlayer_allowedTo(dbPlayer.id, "doBreakBlock") || Rank.isPlayer_allowedTo(dbPlayer.id, "*")) {
            event.setCancelled(false);
        }else{
            player.sendMessage("You cannot do this!");
            player.sendMessage(ChatColor.RED + "You need the permission: 'doBreakBlock'!");
            event.setCancelled(true);
        }
    }
}
