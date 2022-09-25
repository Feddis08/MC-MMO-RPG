package at.feddis08.mmorpg.commands;

import at.feddis08.mmorpg.MMORPG;
import at.feddis08.mmorpg.io.database.Functions;
import at.feddis08.mmorpg.io.database.objects.PlayerObject;
import at.feddis08.mmorpg.io.database.objects.WarpObject;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.Objects;

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
                            if (Rank.isPlayer_allowedTo(dbPlayer.id, "doWarp") || Rank.isPlayer_allowedTo(dbPlayer.id, "*")) {
                                WarpObject dbWarpObject = Functions.getWarp("id", args[0]);
                                if (Objects.equals(dbWarpObject.id, args[0])){
                                    Player player = sender.getServer().getPlayer(sender.getName());
                                    Functions.updateWhereAnd("players_in_worlds", "x", String.valueOf(player.getLocation().getBlockX()), dbPlayer.id, "id", dbPlayer.current_world_id, "world_id");
                                    Functions.updateWhereAnd("players_in_worlds", "y", String.valueOf(player.getLocation().getBlockY()), dbPlayer.id, "id", dbPlayer.current_world_id, "world_id");
                                    Functions.updateWhereAnd("players_in_worlds", "z", String.valueOf(player.getLocation().getBlockZ()), dbPlayer.id, "id", dbPlayer.current_world_id, "world_id");
                                    Functions.update("players", "current_world_id", dbWarpObject.world_name, dbPlayer.id, "id");
                                    player.teleport(new Location(MMORPG.Server.getWorld(dbWarpObject.world_name), Double.parseDouble(String.valueOf(dbWarpObject.x)), Double.parseDouble(String.valueOf(dbWarpObject.y)), Double.parseDouble(String.valueOf(dbWarpObject.z))));
                                    Functions.updateWhereAnd("players_in_worlds", "x", String.valueOf(player.getLocation().getBlockX()), dbPlayer.id, "id", dbPlayer.current_world_id, "world_id");
                                    Functions.updateWhereAnd("players_in_worlds", "y", String.valueOf(player.getLocation().getBlockY()), dbPlayer.id, "id", dbPlayer.current_world_id, "world_id");
                                    Functions.updateWhereAnd("players_in_worlds", "z", String.valueOf(player.getLocation().getBlockZ()), dbPlayer.id, "id", dbPlayer.current_world_id, "world_id");
                                    MMORPG.consoleLog(dbPlayer.display_name + " warped to " + dbWarpObject.id);
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

}
