package at.feddis08.mmorpg.commands;

import at.feddis08.mmorpg.database.Functions;
import at.feddis08.mmorpg.database.objects.PlayerObject;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.sql.SQLException;

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
            if (dbPlayer.didStartup == "1"){
                if (args.length == 0){

                }else{
                    if (args[0].equalsIgnoreCase("info")){

                    }
                }
            }else{
                sender.sendMessage("You need the startup, run /rank!");
                return true;
            }
        }
        return false;
    }
}
