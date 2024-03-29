package at.feddis08.bukkit.commands;

import at.feddis08.bukkit.MMORPG;
import at.feddis08.tools.Rank_api;
import at.feddis08.tools.io.database.Functions;
import at.feddis08.tools.io.database.objects.PlayerObject;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;

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
                    if (Rank_api.isPlayer_allowedTo(dbPlayerSender.id, "doGamemode") || Rank_api.isPlayer_allowedTo(dbPlayerSender.id, "*")) {

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
                            changeBukkitPlayerGamemode(player.getUniqueId().toString(), args[0]);
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
    public static void changeBukkitPlayerGamemode(String player_id, String gamemode) throws SQLException {
        String[] args = new String[1];
        args[0] = gamemode;
        Player player = null;
        player = MMORPG.Server.getPlayer(UUID.fromString(player_id));
        PlayerObject dbPlayer = Functions.getPlayer("id", player_id);
        if (args[0].equalsIgnoreCase("1")) {
            try {
                Functions.update("players", "gamemode", "1", dbPlayer.id, "id");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            player.setGameMode(GameMode.CREATIVE);
            player.sendMessage(ChatColor.GRAY + "Your gamemode changed to CREATIVE");
        }
        if (args[0].equalsIgnoreCase("0")) {
            try {
                Functions.update("players", "gamemode", "0", dbPlayer.id, "id");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            player.setGameMode(GameMode.SURVIVAL);
            player.sendMessage(ChatColor.GRAY + "Your gamemode changed to SURVIVAL");
        }
        if (args[0].equalsIgnoreCase("3")) {
            try {
                Functions.update("players", "gamemode", "3", dbPlayer.id, "id");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            player.setGameMode(GameMode.SPECTATOR);
            player.sendMessage(ChatColor.GRAY + "Your gamemode changed to SPECTATOR");
        }
        if (args[0].equalsIgnoreCase("2")) {
            try {
                Functions.update("players", "gamemode", "2", dbPlayer.id, "id");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            player.setGameMode(GameMode.ADVENTURE);
            player.sendMessage(ChatColor.GRAY + "Your gamemode changed to ADVENTURE");
        }
    }
}
