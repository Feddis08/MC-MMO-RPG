package at.feddis08.mmorpg.commands;

import at.feddis08.mmorpg.MMORPG;
import at.feddis08.mmorpg.database.Functions;
import at.feddis08.mmorpg.database.objects.PlayerObject;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.Objects;

public class reset implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("reset")){
            PlayerObject dbPlayer = null;
            try {
                dbPlayer = Functions.getPlayer("id", sender.getServer().getPlayer(sender.getName()).getUniqueId().toString());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (Objects.equals(dbPlayer.didStartup, "true")){
                if (args.length == 0) {
                    sender.sendMessage("Wrong usage: /reset {your mincraft player_name} {yes/no}");
                    return true;
                }
                if (args.length == 2){
                    if(args[0].equalsIgnoreCase("database")){
                        if(args[1].equalsIgnoreCase("yes")){
                            Player player = sender.getServer().getPlayer(dbPlayer.player_name);
                            if(player.isOp()){
                                sender.sendMessage("Reset database!");
                                try {
                                    Functions.resetDB();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                sender.getServer().reload();
                            }
                        }
                    }
                }
                if ((args.length < 2)) {
                    if (args[0].equalsIgnoreCase(sender.getName())) {
                        sender.sendMessage("Do you realy want to reset your player? If you want run " +
                                "/reset " + dbPlayer.display_name + " yes");
                        return true;
                    }
                }
                if (args[0].equalsIgnoreCase(dbPlayer.player_name) || args.length == 2){
                    if (args[1].equalsIgnoreCase("yes")){
                        try {
                            Functions.delete("players", "id", dbPlayer.id);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        Player player = sender.getServer().getPlayer(dbPlayer.player_name);
                        player.kickPlayer("We deleted your player successfully. You need to reconnect the server!");
                        return true;
                    }
                }
            }else{
                sender.sendMessage("Before you can do something, you have to run the startup with: /startUp");
                MMORPG.consoleLog(sender.getName() + " tried to run: /reset , with none startup!");
                return true;
            }
        }
        return false;
    }
}
