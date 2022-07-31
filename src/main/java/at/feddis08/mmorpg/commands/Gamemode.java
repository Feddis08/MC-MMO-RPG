package at.feddis08.mmorpg.commands;

import at.feddis08.mmorpg.MMORPG;
import at.feddis08.mmorpg.database.Functions;
import at.feddis08.mmorpg.database.objects.PlayerObject;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.Objects;

public class Gamemode implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("gm")) {
            PlayerObject dbPlayerSender = null;
            try {
                dbPlayerSender = Functions.getPlayer("id", sender.getServer().getPlayer(sender.getName()).getUniqueId().toString());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (Objects.equals(dbPlayerSender.didStartup, "true")) {
                try {
                    if (Rank.isPlayer_allowedTo(dbPlayerSender.id, "doGamemode") || Rank.isPlayer_allowedTo(dbPlayerSender.id, "*")) {

                        if (!(sender instanceof Player)) {
                            sender.sendMessage("You need to be a Player to run this command!");
                            return true;
                        }
                            if (!(args.length == 2 || args.length <= 2)) {
                                sender.sendMessage("Wrong usage: /gm 1/2/3");
                                return true;
                            }
                            Player player = null;
                            PlayerObject dbPlayer = dbPlayerSender;
                            if (args.length == 1) {
                                player = sender.getServer().getPlayer(sender.getName());
                            } else if (args.length == 2) {
                                player = sender.getServer().getPlayer(args[1]);
                                dbPlayer = Functions.getPlayer("display_name", args[1]);
                            }
                            if (args[0].equalsIgnoreCase("1")) {
                                try {
                                    Functions.update("players", "gamemode", "1", dbPlayer.id, "id");
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                player.setGameMode(GameMode.CREATIVE);
                                player.sendMessage(ChatColor.GRAY + "Your gamemode changed to CREATIVE");
                                return true;
                            }
                            if (args[0].equalsIgnoreCase("0")) {
                                try {
                                    Functions.update("players", "gamemode", "0", dbPlayer.id, "id");
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                player.setGameMode(GameMode.SURVIVAL);
                                player.sendMessage(ChatColor.GRAY + "Your gamemode changed to SURVIVAL");
                                return true;
                            }
                            if (args[0].equalsIgnoreCase("3")) {
                                try {
                                    MMORPG.consoleLog("ddd " + dbPlayer.id);
                                    Functions.update("players", "gamemode", "3", dbPlayer.id, "id");
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                player.setGameMode(GameMode.SPECTATOR);
                                player.sendMessage(ChatColor.GRAY + "Your gamemode changed to SPECTATOR");
                                return true;
                            }
                            if (args[0].equalsIgnoreCase("2")) {
                                try {
                                    Functions.update("players", "gamemode", "2", dbPlayer.id, "id");
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                player.setGameMode(GameMode.ADVENTURE);
                                player.sendMessage(ChatColor.GRAY + "Your gamemode changed to ADVENTURE");
                                return true;
                            }
                            sender.sendMessage("Wrong usage: /gm 1/2/3");
                    }else{
                        sender.sendMessage(ChatColor.RED + "You need the permission: 'doGamemode'!");
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }return false;
    }
}
