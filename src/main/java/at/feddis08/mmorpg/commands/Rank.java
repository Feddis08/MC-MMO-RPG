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
            if (Objects.equals(dbPlayer.didStartup, "true")) {
                try {
                    if (Rank.has_permission_from_rank_name(Rank.get_rank_from_player(dbPlayer.id).name, "doRank") ||Rank.has_permission_from_rank_name(Rank.get_rank_from_player(dbPlayer.id).name, "*")) {
                        if (args.length == 0) {
                            sender.sendMessage(ChatColor.RED + "Wrong syntax! /rank {{rank_name}} {{create_rank|delete_rank|set_rank_color|set_rank_level|set_prefix|set_prefix_color|add_rule|remove_rule|set_player_rank_from}} {{args}}");
                        } else {
                            boolean validCommand = false;
                            if (args[1].equalsIgnoreCase("create_rank")) {
                                if (create_rank(args[0])) {
                                    sender.sendMessage(ChatColor.DARK_GREEN + "Created new rank: " + ChatColor.GREEN + args[0]);
                                    validCommand = true;
                                } else {
                                    validCommand = false;
                                }
                            }
                            if (Objects.equals(args[1], "set_prefix")) {
                                if (set_prefix(args[0], args[2])) {
                                    sender.sendMessage(ChatColor.DARK_GREEN + "Set the rank_prefix of " + ChatColor.GREEN + args[0] + ChatColor.DARK_GREEN + " to " + ChatColor.GREEN + args[2]);
                                    validCommand = true;
                                } else {
                                    validCommand = false;
                                }
                            }
                            if (Objects.equals(args[1], "set_prefix_color")) {
                                if (set_prefix_color(args[0], args[2])) {
                                    sender.sendMessage(ChatColor.DARK_GREEN + "Set the rank_prefix_color of " + ChatColor.GREEN + args[0] + ChatColor.DARK_GREEN + " to " + ChatColor.GREEN + args[2]);
                                    validCommand = true;
                                } else {
                                    validCommand = false;
                                }
                            }
                            if (Objects.equals(args[1], "add_rule")) {
                                if (add_rule(args[0], args[2])) {
                                    sender.sendMessage(ChatColor.DARK_GREEN + "Add the rank_permission of " + ChatColor.GREEN + args[0] + ChatColor.DARK_GREEN + " to " + ChatColor.GREEN + args[2]);
                                    validCommand = true;
                                } else {

                                    validCommand = false;
                                }
                            }
                            if (Objects.equals(args[1], "set_rank_color")) {
                                if (set_rank_color(args[0], args[2])) {
                                    sender.sendMessage(ChatColor.DARK_GREEN + "Set the rank_color of " + ChatColor.GREEN + args[0] + ChatColor.DARK_GREEN + " to " + ChatColor.GREEN + args[2]);
                                    validCommand = true;
                                } else {
                                    validCommand = false;
                                }
                            }
                            if (Objects.equals(args[1], "set_player_rank_from")) {
                                try {
                                    if (set_player_rank_from(args[0], Functions.getPlayer("display_name", args[2]).id)) {
                                        sender.sendMessage(ChatColor.DARK_GREEN + "Set the rank of " + ChatColor.GREEN + args[2] + ChatColor.DARK_GREEN + " to " + ChatColor.GREEN + args[0]);
                                        validCommand = true;
                                    } else {
                                        validCommand = false;
                                    }
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (Objects.equals(args[1], "remove_rule")) {
                                if (remove_rule(args[2], args[0])) {
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
                        sender.sendMessage(ChatColor.RED + "You need the permission: 'doChat'!");
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
    public static RankObject get_rank_from_player(String player_id) throws SQLException {
        PlayerObject dbPlayer = Functions.getPlayer("id", player_id);
        RankObject dbRank = Functions.getRank("id", dbPlayer.player_rank);
        return dbRank;
    }
    public static boolean has_permission_from_rank_name(String rank_name, String permission) throws SQLException {
        Rank_permissionObject dbRank_permission = Functions.getRanksPermissionsWhereAnd("id", rank_name, "permission", permission);
        if (Objects.equals(dbRank_permission.permission, permission)){return true;}else{return false;}
    }
    public static boolean create_rank(String rank_name){
        RankObject dbRank = null;
        Boolean result = false;
        try {
            dbRank = Functions.getRank("name", rank_name);
            if(Objects.equals(dbRank.name, null)){
                dbRank.id = rank_name;
                dbRank.name = rank_name;
                Functions.createRank(dbRank);
                result = true;
            }else{
                result = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    public static boolean set_prefix(String rank_name, String prefix){
        RankObject dbRank = null;
        boolean result = true;
        try {
            dbRank = Functions.getRank("name", rank_name);
            if (Objects.equals(dbRank.name, null)) {
                result = false;
            }
            if(result){
                Functions.update("ranks", "prefix", prefix, rank_name, "name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    public static boolean set_prefix_color(String rank_name, String prefix_color){
        RankObject dbRank = null;
        boolean result = true;
        try {
            dbRank = Functions.getRank("name", rank_name);
            if (Objects.equals(dbRank.name, null)) {
                result = false;
            }
            if(result){
                Functions.update("ranks", "prefix_color", prefix_color, rank_name, "name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    public static boolean add_rule(String rank_name, String permission){
        RankObject dbRank = null;
        Rank_permissionObject dbRank_permissions = null;
        boolean result = true;
        try {
            dbRank = Functions.getRank("name", rank_name);
            dbRank_permissions = Functions.getRanksPermissionsWhereAnd("permission", permission, "id", dbRank.id);
            if (Objects.equals(dbRank.name, null)) {
                result = false;
            }
            if (Objects.equals(dbRank_permissions.permission, permission)){
                result = false;
            }
            if(result){
                dbRank_permissions.id = rank_name;
                dbRank_permissions.permission = permission;
                Functions.createRank_permision(dbRank_permissions);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    public static boolean set_rank_color(String rank_name, String rank_color){
        RankObject dbRank = null;
        boolean result = true;
        try {
            dbRank = Functions.getRank("name", rank_name);
            if (Objects.equals(dbRank.name, null)) {
                result = false;
            }
            if(result){
                Functions.update("ranks", "rank_color", rank_color, rank_name, "name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    public static boolean set_player_rank_from(String rank_name, String player_id) {
        RankObject dbRank = null;
        PlayerObject dbPlayer2 = null;
        boolean result = true;
        try {
            dbRank = Functions.getRank("name", rank_name);
            dbPlayer2 = Functions.getPlayer("id", player_id);
            if (Objects.equals(dbRank.name, null)) {
                result = false;
            } else {
                if (Objects.equals(dbPlayer2.id, null)) {
                    result = false;
                }
            }
            if(result){
                Functions.update("players", "player_rank", rank_name, dbPlayer2.id, "id");
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

