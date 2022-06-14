package at.feddis08.mmorpg.commands;

import at.feddis08.mmorpg.database.Functions;
import at.feddis08.mmorpg.database.objects.PlayerObject;
import at.feddis08.mmorpg.database.objects.RankObject;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.sql.SQLException;
import java.util.Objects;
import java.util.function.Function;

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
            if (Objects.equals(dbPlayer.didStartup, "true")){
                if (args.length == 0){
                    sender.sendMessage(ChatColor.RED + "Wrong syntax! /rank {{rank_name}} {{create_rank|delete_rank|set_rank_color|set_rank_level|set_prefix|set_prefix_color|add_rule|remove_rule|set_player_rank_from}} {{args}}");
                }else{
                    if (args[1].equalsIgnoreCase("create_rank")){
                        RankObject dbRank = null;
                        try {
                            dbRank = Functions.getRank("name", args[0]);
                            if(Objects.equals(dbRank.name, null)){
                                dbRank.id = args[0];
                                dbRank.name = args[0];
                                Functions.createRank(dbRank);
                                sender.sendMessage(ChatColor.DARK_GREEN + "Created new rank: " + ChatColor.GREEN + args[0]);
                            }else{
                                sender.sendMessage(ChatColor.RED + "The rank is already in the system! " + ChatColor.DARK_RED + dbRank.name);
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    if(Objects.equals(args[1], "set_prefix")) {
                        RankObject dbRank = null;
                        boolean result = true;
                        try {
                            dbRank = Functions.getRank("name", args[0]);
                            if (Objects.equals(dbRank.name, null)) {
                                sender.sendMessage(ChatColor.RED + "The rank is not in the system! " + ChatColor.DARK_RED + args[0]);
                                result = false;
                            }
                            if(result){
                                Functions.update("ranks", "prefix", args[2], args[0], "name");
                                sender.sendMessage(ChatColor.DARK_GREEN + "Set the rank_prefix of " + ChatColor.GREEN + args[0] + ChatColor.DARK_GREEN + " to " + ChatColor.GREEN + args[2]);
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    if(Objects.equals(args[1], "set_player_rank_from")) {
                        RankObject dbRank = null;
                        PlayerObject dbPlayer2 = null;
                        try {
                            dbRank = Functions.getRank("name", args[0]);
                            dbPlayer2 = Functions.getPlayer("display_name", args[2]);
                            boolean result = true;
                            if (Objects.equals(dbRank.name, null)) {
                                sender.sendMessage(ChatColor.RED + "The rank is not in the system! " + ChatColor.DARK_RED + args[0]);
                                result = false;
                            } else {
                                if (Objects.equals(dbPlayer2.id, null)) {
                                    result = false;
                                    sender.sendMessage(ChatColor.RED + "The player is not in the system! Maybe he didn't do the startup?" + ChatColor.DARK_RED + args[2]);
                                }
                            }
                            if(result){
                                Functions.update("players", "player_rank", args[0], dbPlayer.id, "id");
                                sender.sendMessage(ChatColor.DARK_GREEN + "Set the rank of " + ChatColor.GREEN + args[2] + ChatColor.DARK_GREEN + " to " + ChatColor.GREEN + args[0]);
                            }else{
                                sender.sendMessage(ChatColor.RED + "Please check your rank_name/player_name: " + ChatColor.DARK_RED + args[0] + " ,  " + args[2]);
                            }

                        }catch(SQLException e){
                            e.printStackTrace();
                        }
                    }
                }
            }else{
                sender.sendMessage(ChatColor.RED + "Please do the startup! " + ChatColor.GOLD + "/startup");
                return true;
            }
        }
        return false;
    }
}
