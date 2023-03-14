package at.feddis08.bukkit.commands;

import at.feddis08.tools.Rank_api;
import at.feddis08.tools.io.database.Functions;
import at.feddis08.tools.io.database.objects.PlayerObject;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.sql.SQLException;
import java.util.Objects;

public class Rank implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("rank")){
            PlayerObject dbPlayer = null;
            try {
                dbPlayer = Functions.getPlayer("id", sender.getServer().getPlayer(sender.getName()).getUniqueId().toString());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (Objects.equals(dbPlayer.didStartup, "true")) {
                try {
                    if (Rank_api.isPlayer_allowedTo(dbPlayer.id, "doRank") || Rank_api.isPlayer_allowedTo(dbPlayer.id, "*")) {
                        if (args.length == 0) {
                            sender.sendMessage(ChatColor.RED + "Wrong syntax! /rank {{rank_name}} {{create_rank|delete_rank|set_rank_color|set_rank_level|set_prefix|set_prefix_color|add_rule|remove_rule|set_player_rank_from}} {{args}}");
                        } else {
                            boolean validCommand = false;
                            if (args[1].equalsIgnoreCase("create_rank")) {
                                if (Rank_api.create_rank(args[0])) {
                                    sender.sendMessage(ChatColor.DARK_GREEN + "Created new rank: " + ChatColor.GREEN + args[0]);
                                    validCommand = true;
                                } else {
                                    validCommand = false;
                                }
                            }
                            if (Objects.equals(args[1], "set_prefix")) {
                                if (Rank_api.set_prefix(args[0], args[2])) {
                                    sender.sendMessage(ChatColor.DARK_GREEN + "Set the rank_prefix of " + ChatColor.GREEN + args[0] + ChatColor.DARK_GREEN + " to " + ChatColor.GREEN + args[2]);
                                    validCommand = true;
                                } else {
                                    validCommand = false;
                                }
                            }
                            if (Objects.equals(args[1], "set_prefix_color")) {
                                if (Rank_api.set_prefix_color(args[0], args[2])) {
                                    sender.sendMessage(ChatColor.DARK_GREEN + "Set the rank_prefix_color of " + ChatColor.GREEN + args[0] + ChatColor.DARK_GREEN + " to " + ChatColor.GREEN + args[2]);
                                    validCommand = true;
                                } else {
                                    validCommand = false;
                                }
                            }
                            if (Objects.equals(args[1], "add_rule")) {
                                if (Rank_api.add_rule(args[0], args[2])) {
                                    sender.sendMessage(ChatColor.DARK_GREEN + "Add the rank_permission of " + ChatColor.GREEN + args[0] + ChatColor.DARK_GREEN + " to " + ChatColor.GREEN + args[2]);
                                    validCommand = true;
                                } else {

                                    validCommand = false;
                                }
                            }
                            if (Objects.equals(args[1], "set_rank_color")) {
                                if (Rank_api.set_rank_color(args[0], args[2])) {
                                    sender.sendMessage(ChatColor.DARK_GREEN + "Set the rank_color of " + ChatColor.GREEN + args[0] + ChatColor.DARK_GREEN + " to " + ChatColor.GREEN + args[2]);
                                    validCommand = true;
                                } else {
                                    validCommand = false;
                                }
                            }
                            if (Objects.equals(args[1], "set_player_rank")) {
                                try {
                                    if (Rank_api.set_player_rank_from(args[0], Functions.getPlayer("display_name", args[2]).id)) {
                                        sender.sendMessage(ChatColor.DARK_GREEN + "Set the rank of " + ChatColor.GREEN + args[2] + ChatColor.DARK_GREEN + " to " + ChatColor.GREEN + args[0]);
                                        validCommand = true;
                                    } else {
                                        validCommand = false;
                                    }
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (Objects.equals(args[1], "set_parent")) {
                                if (Rank_api.set_parent(args[0], args[2])) {
                                    sender.sendMessage(ChatColor.DARK_GREEN + "Set the parent_rank from " + ChatColor.GREEN + args[0] + ChatColor.DARK_GREEN + " by " + ChatColor.GREEN + args[2]);
                                    validCommand = true;
                                } else {
                                    validCommand = false;
                                }
                            }
                            if (Objects.equals(args[1], "remove_rule")) {
                                if (Rank_api.remove_rule(args[2], args[0])) {
                                    sender.sendMessage(ChatColor.DARK_GREEN + "Removed the rank_permission from " + ChatColor.GREEN + args[0] + ChatColor.DARK_GREEN + " by " + ChatColor.GREEN + args[2]);
                                    validCommand = true;
                                } else {
                                    validCommand = false;
                                }
                            }
                            if (!(validCommand)) {
                                sender.sendMessage(ChatColor.RED + "Wrong command!");
                            }
                        }
                    }else{
                        sender.sendMessage(ChatColor.RED + "You need the permission: 'doRank'!");
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }else{
                sender.sendMessage(ChatColor.RED + "Please do the startup! " + ChatColor.GOLD + "/startup");
                return true;
            }
        }
        return false;
    }
}

