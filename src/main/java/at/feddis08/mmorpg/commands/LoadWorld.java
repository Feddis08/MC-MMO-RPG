package at.feddis08.mmorpg.commands;

import at.feddis08.mmorpg.MMORPG;
import at.feddis08.mmorpg.database.Functions;
import at.feddis08.mmorpg.database.objects.PlayerObject;
import at.feddis08.mmorpg.database.objects.WorldObject;
import at.feddis08.mmorpg.tools.StartLoadWorld;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;

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
                try {
                    if (Rank.isPlayer_allowedTo(dbPlayer.id, "doLoadWorld") || Rank.isPlayer_allowedTo(dbPlayer.id, "*")) {
                        if (args.length == 1) {
                            sender.sendMessage("Loading world: " + args[0]);
                            StartLoadWorld.loadWorld(args[0]);
                            sender.sendMessage("World load complete");

                        } else {
                            sender.sendMessage("Wrong usage: /loadWorld worldName");
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "You need the permission: 'doLoadWorld'!");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            } else {
                sender.sendMessage(ChatColor.RED + "Please do the startup. " + ChatColor.GOLD + "/startup");
            }
        }
        return false;
    }

}
