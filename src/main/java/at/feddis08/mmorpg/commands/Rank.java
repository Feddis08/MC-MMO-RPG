package at.feddis08.mmorpg.commands;

import at.feddis08.mmorpg.MMORPG;
import at.feddis08.mmorpg.database.Functions;
import at.feddis08.mmorpg.database.objects.PlayerObject;
import at.feddis08.mmorpg.database.objects.RankObject;
import at.feddis08.mmorpg.database.objects.Rank_permissionObject;
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
            if (Objects.equals(dbPlayer.didStartup, "true")){
                if (args.length == 0){
                    sender.sendMessage(ChatColor.RED + "Wrong syntax! /rank {{rank_name}} {{create_rank|delete_rank|set_rank_color|set_rank_level|set_prefix|set_prefix_color|add_rule|remove_rule|set_player_rank_from}} {{args}}");
                }else{
                    boolean validCommand = false;
                    if (args[1].equalsIgnoreCase("create_rank")){
                        RankObject dbRank = null;
                        try {
                            dbRank = Functions.getRank("name", args[0]);
                            if(Objects.equals(dbRank.name, null)){
                                dbRank.id = args[0];
                                dbRank.name = args[0];
                                Functions.createRank(dbRank);
                                sender.sendMessage(ChatColor.DARK_GREEN + "Created new rank: " + ChatColor.GREEN + args[0]);
                                validCommand = true;
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
                                validCommand = true;
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    if(Objects.equals(args[1], "set_prefix_color")) {
                        RankObject dbRank = null;
                        boolean result = true;
                        try {
                            dbRank = Functions.getRank("name", args[0]);
                            if (Objects.equals(dbRank.name, null)) {
                                sender.sendMessage(ChatColor.RED + "The rank is not in the system! " + ChatColor.DARK_RED + args[0]);
                                result = false;
                            }
                            if(result){
                                Functions.update("ranks", "prefix_color", args[2], args[0], "name");
                                sender.sendMessage(ChatColor.DARK_GREEN + "Set the rank_prefix_color of " + ChatColor.GREEN + args[0] + ChatColor.DARK_GREEN + " to " + ChatColor.GREEN + args[2]);
                                validCommand = true;
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    if(Objects.equals(args[1], "add_rule")) {
                        RankObject dbRank = null;
                        Rank_permissionObject dbRank_permissions = null;
                        boolean result = true;
                        try {
                            dbRank = Functions.getRank("name", args[0]);
                            dbRank_permissions = Functions.getRanksPermissionsWhereAnd("permission", args[2], "id", dbRank.id);
                            if (Objects.equals(dbRank.name, null)) {
                                sender.sendMessage(ChatColor.RED + "The rank is not in the system! " + ChatColor.DARK_RED + args[0]);
                                result = false;
                            }
                            if (Objects.equals(dbRank_permissions.permission, args[2])){
                                sender.sendMessage(ChatColor.RED + "The permission is already in the system! " + ChatColor.DARK_RED + dbRank_permissions.permission);
                                result = false;
                            }
                            if(result){
                                dbRank_permissions.id = args[0];
                                dbRank_permissions.permission = args[2];
                                Functions.createRank_permision(dbRank_permissions);
                                sender.sendMessage(ChatColor.DARK_GREEN + "Add the rank_permission of " + ChatColor.GREEN + args[0] + ChatColor.DARK_GREEN + " to " + ChatColor.GREEN + args[2]);
                                validCommand = true;
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    if(Objects.equals(args[1], "set_rank_color")) {
                        RankObject dbRank = null;
                        boolean result = true;
                        try {
                            dbRank = Functions.getRank("name", args[0]);
                            if (Objects.equals(dbRank.name, null)) {
                                sender.sendMessage(ChatColor.RED + "The rank is not in the system! " + ChatColor.DARK_RED + args[0]);
                                result = false;
                            }
                            if(result){
                                Functions.update("ranks", "rank_color", args[2], args[0], "name");
                                sender.sendMessage(ChatColor.DARK_GREEN + "Set the rank_color of " + ChatColor.GREEN + args[0] + ChatColor.DARK_GREEN + " to " + ChatColor.GREEN + args[2]);
                                validCommand = true;
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    if(Objects.equals(args[1], "set_player_rank_from")) {
                        if (set_player_rank_from(args[0], args[2])) {
                            sender.sendMessage(ChatColor.DARK_GREEN + "Set the rank of " + ChatColor.GREEN + args[2] + ChatColor.DARK_GREEN + " to " + ChatColor.GREEN + args[0]);
                            validCommand = true;
                        }else{
                            validCommand = false;
                        }
                    }
                    if(Objects.equals(args[1], "remove_rule")) {
                        if (remove_rule(args[2], args[0])) {
                            sender.sendMessage(ChatColor.DARK_GREEN + "Removed the rank_permission from " + ChatColor.GREEN + args[0] + ChatColor.DARK_GREEN + " by " + ChatColor.GREEN + args[2]);
                            validCommand = true;
                        }else {
                            validCommand = false;
                        }
                    }
                    if(!(validCommand)){
                        sender.sendMessage(ChatColor.RED + "Wrong command!");
                    }
                }
            }else{
                sender.sendMessage(ChatColor.RED + "Please do the startup! " + ChatColor.GOLD + "/startup");
                return true;
            }
        }
        return false;
    }
    public  static boolean set_player_rank_from(String id, String player_id) {
        RankObject dbRank = null;
        PlayerObject dbPlayer2 = null;
        boolean result = true;
        try {
            dbRank = Functions.getRank("name", id);
            dbPlayer2 = Functions.getPlayer("display_name", player_id);
            if (!(Objects.equals(dbPlayer2.didStartup, "true"))) {
               result = false;
                MMORPG.consoleLog("1");
            }
            if (Objects.equals(dbRank.name, null)) {
                result = false;
                MMORPG.consoleLog("2");
            } else {
                if (Objects.equals(dbPlayer2.id, null)) {
                    MMORPG.consoleLog("3");
                    result = false;
                }
            }
            if(result){
                Functions.update("players", "player_rank", id, dbPlayer2.id, "id");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return result;
    }
    public static boolean remove_rule(String permission, String id){
        RankObject dbRank = null;
        Rank_permissionObject dbRank_permission = null;
        boolean result = true;
        try {
            dbRank = Functions.getRank("name", id);
            dbRank_permission = Functions.getRanksPermissionsWhereAnd("permission", permission, "id", id);
            if (Objects.equals(dbRank.name, null)) {
                result = false;
            }
            if (!(Objects.equals(dbRank_permission.permission, permission))) {
                result = false;
            }
            MMORPG.consoleLog(dbRank.name + " " + dbRank_permission.permission + " " + result);
            if(result){
                Functions.deleteWhereAnd("ranks_permissions", "permission", permission, "id", id);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}

