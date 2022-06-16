package at.feddis08.mmorpg.commands;

import at.feddis08.mmorpg.MMORPG;
import at.feddis08.mmorpg.database.Functions;
import at.feddis08.mmorpg.database.objects.PlayerObject;
import at.feddis08.mmorpg.database.objects.WorldObject;
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
                            WorldCreator wc = new WorldCreator(args[0]);

                            wc.environment(World.Environment.NORMAL);
                            wc.type(WorldType.NORMAL);
                            MMORPG.consoleLog("Loading world: " + args[0]);
                            sender.sendMessage("Loading world: " + args[0]);
                            wc.createWorld();
                            sender.sendMessage("World load complete");
                            MMORPG.consoleLog("World load complete");
                            WorldObject dbWorld = null;
                            try {
                                dbWorld = Functions.getWorld("name", args[0]);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            if (dbWorld.id == null) {
                                dbWorld = new WorldObject();
                                dbWorld.loaded = "true";
                                dbWorld.id = args[0];
                                dbWorld.name = args[0];
                                try {
                                    Functions.createWorld(dbWorld);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                dbWorld.players_on = dbWorld.players_on + 1;
                                dbWorld.loaded = "true";
                                try {
                                    Functions.update("worlds", "players_on", dbWorld.players_on.toString(), dbWorld.id, "id");
                                    Functions.update("worlds", "loaded", dbWorld.loaded, dbWorld.id, "id");
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }

                        } else {
                            sender.sendMessage("Wrong usage: /loadWorld worldName");
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "You need the permission: 'doTpWorld'!");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }else{
                sender.sendMessage(ChatColor.RED + "Please do the startup. " + ChatColor.GOLD + "/startup");
            }
        }
        return false;
    }
}
