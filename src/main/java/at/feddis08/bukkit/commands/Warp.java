package at.feddis08.bukkit.commands;

import at.feddis08.Boot;
import at.feddis08.bukkit.MMORPG;
import at.feddis08.tools.Rank_api;
import at.feddis08.tools.io.database.Functions;
import at.feddis08.tools.io.database.objects.PlayerObject;
import at.feddis08.tools.io.database.objects.WarpObject;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;

public class Warp implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("warp")) {
            PlayerObject dbPlayer = null;
            try {
                dbPlayer = (PlayerObject) Functions.getPlayer("id", sender.getServer().getPlayer(sender.getName()).getUniqueId().toString());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (Objects.equals(dbPlayer.didStartup, "true")) {
                    if (args.length == 1) {
                        try {
                            if (Rank_api.isPlayer_allowedTo(dbPlayer.id, "doWarp") || Rank_api.isPlayer_allowedTo(dbPlayer.id, "*")) {
                                WarpObject dbWarpObject = Functions.getWarp("id", args[0]);
                                if (warp_player(dbPlayer.id, args[0])){
                                    sender.sendMessage(ChatColor.GREEN + "Done");
                                }else{
                                    sender.sendMessage(ChatColor.RED + "Warp does not exist!");
                                }
                            } else {
                                sender.sendMessage(ChatColor.RED + "You need the permission: 'doWarp'!");
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }else{
                        sender.sendMessage("Wrong usage: /warp id");
                    }
            } else{
                sender.sendMessage(ChatColor.RED + "Please do the startup. " + ChatColor.GOLD + "/startup");
            }
        }
        return false;
    }

    public static Boolean warp_player(String player_id, String warp_id) throws SQLException {
        Boolean warped = false;
        WarpObject dbWarpObject = Functions.getWarp("id", warp_id);
        if (Objects.equals(dbWarpObject.id, warp_id)){
            Player player = MMORPG.Server.getPlayer(UUID.fromString(player_id));
            PlayerObject dbPlayer = (PlayerObject) Functions.getPlayer("id", MMORPG.Server.getPlayer(player.getName()).getUniqueId().toString());
            Functions.updateWhereAnd("players_in_worlds", "x", String.valueOf(player.getLocation().getBlockX()), dbPlayer.id, "id", dbPlayer.current_world_id, "world_id");
            Functions.updateWhereAnd("players_in_worlds", "y", String.valueOf(player.getLocation().getBlockY()), dbPlayer.id, "id", dbPlayer.current_world_id, "world_id");
            Functions.updateWhereAnd("players_in_worlds", "z", String.valueOf(player.getLocation().getBlockZ()), dbPlayer.id, "id", dbPlayer.current_world_id, "world_id");
            Functions.update("players", "current_world_id", dbWarpObject.world_name, dbPlayer.id, "id");
            player.teleport(new Location(MMORPG.Server.getWorld(dbWarpObject.world_name), Double.parseDouble(String.valueOf(dbWarpObject.x)), Double.parseDouble(String.valueOf(dbWarpObject.y)), Double.parseDouble(String.valueOf(dbWarpObject.z))));
            Functions.updateWhereAnd("players_in_worlds", "x", String.valueOf(player.getLocation().getBlockX()), dbPlayer.id, "id", dbPlayer.current_world_id, "world_id");
            Functions.updateWhereAnd("players_in_worlds", "y", String.valueOf(player.getLocation().getBlockY()), dbPlayer.id, "id", dbPlayer.current_world_id, "world_id");
            Functions.updateWhereAnd("players_in_worlds", "z", String.valueOf(player.getLocation().getBlockZ()), dbPlayer.id, "id", dbPlayer.current_world_id, "world_id");
            Boot.consoleLog(dbPlayer.display_name + " warped to " + dbWarpObject.id);
            warped = true;
        }
        return warped;
    }
}
