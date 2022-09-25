package at.feddis08.mmorpg.commands;

import at.feddis08.mmorpg.MMORPG;
import at.feddis08.mmorpg.io.database.Functions;
import at.feddis08.mmorpg.io.database.objects.PlayerObject;
import at.feddis08.mmorpg.io.database.objects.WarpObject;
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
                            if (Rank.isPlayer_allowedTo(dbPlayer.id, "doRemoveWarp") || Rank.isPlayer_allowedTo(dbPlayer.id, "*")) {
                                WarpObject dbWarpObject = Functions.getWarp("id", args[0]);
                                if (Objects.equals(dbWarpObject.id, args[0])){
                                    Functions.delete("warps", "id", dbWarpObject.id);
                                    MMORPG.consoleLog(dbPlayer.id + " deleted warp: " + args[0]);
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
