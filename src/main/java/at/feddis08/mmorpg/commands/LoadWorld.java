package at.feddis08.mmorpg.commands;

import at.feddis08.mmorpg.io.database.Functions;
import at.feddis08.mmorpg.io.database.objects.PlayerObject;
import at.feddis08.mmorpg.minecraft.tools.StartLoadWorld;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.sql.SQLException;
import java.util.Objects;

public class LoadWorld implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("loadWorld")) {
            PlayerObject dbPlayer = null;
            try {
                dbPlayer = (PlayerObject) Functions.getPlayer("id", sender.getServer().getPlayer(sender.getName()).getUniqueId().toString());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (Objects.equals(dbPlayer.didStartup, "true")) {
                    if (args.length == 1) {
                        try {
                            if (Rank.isPlayer_allowedTo(dbPlayer.id, "doLoadWorld") || Rank.isPlayer_allowedTo(dbPlayer.id, "*")) {
                                sender.sendMessage("Loading world: " + args[0]);
                                StartLoadWorld.loadWorld(args[0]);
                                sender.sendMessage("World load complete");

                            } else {
                                sender.sendMessage(ChatColor.RED + "You need the permission: 'doLoadWorld'!");
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } else if (args.length == 2) {
                        try {
                            if ((Rank.isPlayer_allowedTo(dbPlayer.id, "doLoadWorld") && Rank.isPlayer_allowedTo(dbPlayer.id, "doSetAutoloadWorld")) || Rank.isPlayer_allowedTo(dbPlayer.id, "*")) {
                                sender.sendMessage("Loading world: " + args[0]);
                                StartLoadWorld.startLoadWorldWithAutoload(args[0], args[1]);
                                sender.sendMessage("World load complete");
                            } else {
                                sender.sendMessage(ChatColor.RED + "You need the permission: 'doLoadWorld' and 'doSetAutoloadWorld'!");
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }else{
                        sender.sendMessage("Wrong usage: /loadWorld worldName  or  /loadWorld worldName autoload 0/1");
                    }
            } else{
                sender.sendMessage(ChatColor.RED + "Please do the startup. " + ChatColor.GOLD + "/startup");
            }
        }
        return false;
    }

}
