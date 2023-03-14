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

public class SetWarp implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("setwarp")) {
            PlayerObject dbPlayer = null;
            try {
                dbPlayer = (PlayerObject) Functions.getPlayer("id", sender.getServer().getPlayer(sender.getName()).getUniqueId().toString());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (Objects.equals(dbPlayer.didStartup, "true")) {
                    if (args.length == 5) {
                        try {
                            if (Rank_api.isPlayer_allowedTo(dbPlayer.id, "doSetWarp") || Rank_api.isPlayer_allowedTo(dbPlayer.id, "*")) {
                                WarpObject dbWarpObject = Functions.getWarp("id", args[0]);
                                if (Objects.equals(dbWarpObject.id, args[0])){
                                    Functions.update("warps", "world_name", args[1], args[0], "id");
                                    Functions.update("warps", "x", args[2], args[0], "id");
                                    Functions.update("warps", "y", args[3], args[0], "id");
                                    Functions.update("warps", "z", args[4], args[0], "id");
                                    Boot.consoleLog("updated warp: " + dbWarpObject.id + " for " + dbPlayer.display_name + "!");
                                    sender.sendMessage(ChatColor.GREEN + "Done, updated old warp!");
                                }else{
                                    dbWarpObject.id = args[0];
                                    dbWarpObject.world_name = args[1];
                                    dbWarpObject.x = Integer.parseInt(args[2]);
                                    dbWarpObject.y = Integer.parseInt(args[3]);
                                    dbWarpObject.z = Integer.parseInt(args[4]);
                                    Functions.createWarp(dbWarpObject);
                                    Boot.consoleLog("Created warp: " + dbWarpObject.id + " for " + dbPlayer.display_name + "!");
                                    sender.sendMessage(ChatColor.GREEN + "Done, created new warp!");
                                }
                            } else {
                                sender.sendMessage(ChatColor.RED + "You need the permission: 'doSetWarp'!");
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }else{
                        sender.sendMessage("Wrong usage: /setWarp id world_name x y z");
                    }
            } else{
                sender.sendMessage(ChatColor.RED + "Please do the startup. " + ChatColor.GOLD + "/startup");
            }
        }
        return false;
    }

}
