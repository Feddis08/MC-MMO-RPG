package at.feddis08.mmorpg.commands;

import at.feddis08.mmorpg.io.database.Functions;
import at.feddis08.mmorpg.io.database.objects.PlayerInWorlds;
import at.feddis08.mmorpg.io.database.objects.PlayerObject;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class TpWorld implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("tpWorld")) {
            PlayerObject dbPlayer = null;
            try {
                dbPlayer = (PlayerObject) Functions.getPlayer("id", sender.getServer().getPlayer(sender.getName()).getUniqueId().toString());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (Objects.equals(dbPlayer.didStartup, "true")) {
                try {
                    if (Rank.isPlayer_allowedTo(dbPlayer.id, "doTpWorld") || Rank.isPlayer_allowedTo(dbPlayer.id, "*")) {
                        if (args.length == 1) {
                            Player player = sender.getServer().getPlayer(sender.getName());
                            sender.sendMessage("Do tp.");
                            try {
                                ArrayList<PlayerInWorlds> worlds = Functions.getPlayerInWorlds("id", dbPlayer.id);
                                if (worlds.size() == 0) {
                                    PlayerInWorlds world = new PlayerInWorlds();
                                    world.world_id = args[0];
                                    world.id = dbPlayer.id;
                                    world.x = 10;
                                    world.y = 100;
                                    world.z = 1;
                                    Functions.createPlayerInWorlds(world);
                                    Functions.updateWhereAnd("players_in_worlds", "x", String.valueOf(player.getLocation().getBlockX()), dbPlayer.id, "id", dbPlayer.current_world_id, "world_id");
                                    Functions.updateWhereAnd("players_in_worlds", "y", String.valueOf(player.getLocation().getBlockY()), dbPlayer.id, "id", dbPlayer.current_world_id, "world_id");
                                    Functions.updateWhereAnd("players_in_worlds", "z", String.valueOf(player.getLocation().getBlockZ()), dbPlayer.id, "id", dbPlayer.current_world_id, "world_id");
                                    Functions.update("players", "current_world_id", args[0], dbPlayer.id, "id");
                                    Boolean rightTeleported = player.teleport(new Location(player.getServer().getWorld(args[0]), 0, 100, 0));
                                } else {
                                    Integer i = 1;
                                    Boolean foundWorld = false;
                                    while (i <= worlds.size()) {
                                        if (Objects.equals(worlds.get(i - 1).world_id, args[0])) {
                                            foundWorld = true;
                                            Functions.updateWhereAnd("players_in_worlds", "x", String.valueOf(player.getLocation().getBlockX()), dbPlayer.id, "id", dbPlayer.current_world_id, "world_id");
                                            Functions.updateWhereAnd("players_in_worlds", "y", String.valueOf(player.getLocation().getBlockY()), dbPlayer.id, "id", dbPlayer.current_world_id, "world_id");
                                            Functions.updateWhereAnd("players_in_worlds", "z", String.valueOf(player.getLocation().getBlockZ()), dbPlayer.id, "id", dbPlayer.current_world_id, "world_id");
                                            Functions.update("players", "current_world_id", args[0], dbPlayer.id, "id");
                                            player.teleport(new Location(player.getServer().getWorld(args[0]), worlds.get(i - 1).x, worlds.get(i - 1).y, worlds.get(i - 1).z));
                                        }
                                        i = i + 1;
                                    }

                                    if (!(foundWorld)) {
                                        PlayerInWorlds world = new PlayerInWorlds();
                                        world.world_id = args[0];
                                        world.id = dbPlayer.id;
                                        world.x = 0;
                                        world.y = 100;
                                        world.z = 0;
                                        Functions.createPlayerInWorlds(world);
                                        Functions.updateWhereAnd("players_in_worlds", "x", String.valueOf(player.getLocation().getBlockX()), dbPlayer.id, "id", dbPlayer.current_world_id, "world_id");
                                        Functions.updateWhereAnd("players_in_worlds", "y", String.valueOf(player.getLocation().getBlockY()), dbPlayer.id, "id", dbPlayer.current_world_id, "world_id");
                                        Functions.updateWhereAnd("players_in_worlds", "z", String.valueOf(player.getLocation().getBlockZ()), dbPlayer.id, "id", dbPlayer.current_world_id, "world_id");
                                        Functions.update("players", "current_world_id", args[0], dbPlayer.id, "id");
                                        Boolean rightTeleported = player.teleport(new Location(player.getServer().getWorld(args[0]), 0, 100, 0));
                                    }
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                    }else{

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
