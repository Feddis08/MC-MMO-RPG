package at.feddis08.mmorpg.commands;

import at.feddis08.mmorpg.MMORPG;
import at.feddis08.mmorpg.database.Functions;
import at.feddis08.mmorpg.database.PlayerObject;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.Objects;

public class TpWorld {
    public class tpWorld implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if (label.equalsIgnoreCase("toWorld")) {
                PlayerObject dbPlayer = null;
                try {
                    dbPlayer = (PlayerObject) Functions.getPlayer("id", sender.getServer().getPlayer(sender.getName()).getUniqueId().toString());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                if (Objects.equals(dbPlayer.didStartup, "true")) {
                    Player player = sender.getServer().getPlayer(sender.getName());
                    player.teleport(new Location(player.getServer().getWorld(""), 0, 100, 0));
                    if (args.length == 1) {
                    } else {
                        sender.sendMessage("Wrong usage: /toWorld worldName");
                    }
                }else{
                    sender.sendMessage(ChatColor.RED + "Please do the startup. " + ChatColor.GOLD + "/startup");
                }

            }
            return false;
        }
    }
}
