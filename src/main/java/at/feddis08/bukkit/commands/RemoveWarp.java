package at.feddis08.bukkit.commands;

import at.feddis08.Boot;
import at.feddis08.tools.Rank_api;
import at.feddis08.tools.io.database.Functions;
import at.feddis08.tools.io.database.objects.PlayerObject;
import at.feddis08.tools.io.database.objects.WarpObject;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.sql.SQLException;
import java.util.Objects;

public class RemoveWarp implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("removewarp")) {
            PlayerObject dbPlayer = null;
            try {
                dbPlayer = (PlayerObject) Functions.getPlayer("id", sender.getServer().getPlayer(sender.getName()).getUniqueId().toString());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (Objects.equals(dbPlayer.didStartup, "true")) {
                    if (args.length == 1) {
                        try {
                            if (Rank_api.isPlayer_allowedTo(dbPlayer.id, "doRemoveWarp") || Rank_api.isPlayer_allowedTo(dbPlayer.id, "*")) {
                                WarpObject dbWarpObject = Functions.getWarp("id", args[0]);
                                if (Objects.equals(dbWarpObject.id, args[0])){
                                    Functions.delete("warps", "id", dbWarpObject.id);
                                    Boot.consoleLog(dbPlayer.id + " deleted warp: " + args[0]);
                                    sender.sendMessage(ChatColor.GREEN + "Done, deleted warp!");
                                }else{
                                    sender.sendMessage(ChatColor.RED + "Warp does not exist!");
                                }
                            } else {
                                sender.sendMessage(ChatColor.RED + "You need the permission: 'doRemoveWarp'!");
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }else{
                        sender.sendMessage("Wrong usage: /removeWarp id");
                    }
            } else{
                sender.sendMessage(ChatColor.RED + "Please do the startup. " + ChatColor.GOLD + "/startup");
            }
        }
        return false;
    }

}
