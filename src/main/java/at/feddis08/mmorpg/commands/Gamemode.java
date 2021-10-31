package at.feddis08.mmorpg.commands;

import at.feddis08.mmorpg.MMORPG;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Gamemode implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("gm")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("You need to be a Player to run this command!");
                return true;
            }
            if (((Player) sender).getPlayer().isOp()) {
                if (!(args.length == 2 || args.length <=2)) {
                    sender.sendMessage("Wrong usage: /gm 1/2/3");
                    return true;
                }
                Player player = null;
                if (args.length == 1) {
                    player = sender.getServer().getPlayer(sender.getName());
                }else if(args.length == 2){
                    player = sender.getServer().getPlayer(args[1]);
                }
                if (args[0].equalsIgnoreCase("1")) {
                    player.setGameMode(GameMode.CREATIVE);
                    player.sendMessage(ChatColor.GRAY + "Your gamemode changed to CREATIVE");
                    return true;
                }
                if (args[0].equalsIgnoreCase("0")) {
                    player.setGameMode(GameMode.SURVIVAL);
                    player.sendMessage(ChatColor.GRAY + "Your gamemode changed to SURVIVAL");
                    return true;
                }
                if (args[0].equalsIgnoreCase("3")) {
                    player.setGameMode(GameMode.SPECTATOR);
                    player.sendMessage(ChatColor.GRAY + "Your gamemode changed to SPECTATOR");
                    return true;
                }
                if (args[0].equalsIgnoreCase("2")) {
                    player.setGameMode(GameMode.ADVENTURE);
                    player.sendMessage(ChatColor.GRAY + "Your gamemode changed to ADVENTURE");
                    return true;
                }
                sender.sendMessage("Wrong usage: /gm 1/2/3");

            } else {
                sender.sendMessage("you need to be op to perform this command!");
            }
        }
        return false;
    }
}
