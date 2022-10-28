package at.feddis08.mmorpg.commands;

import at.feddis08.mmorpg.io.database.*;
import at.feddis08.mmorpg.io.database.objects.PlayerObject;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.sql.SQLException;
import java.util.Objects;

public class StartUp implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("startUp")){
            PlayerObject dbPlayer = null;
            try {
                dbPlayer = (PlayerObject) Functions.getPlayer("id", sender.getServer().getPlayer(sender.getName()).getUniqueId().toString());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (Objects.equals(dbPlayer.didStartup, "true")){
                sender.sendMessage("You already did the startup!");
                return true;
            }else {
                if (args.length > 1) {
                    sender.sendMessage("For the startup you need to write this command. You must change the parameter:" +
                            " /startUp {your player_name}");
                }else{
                    if (args.length == 1){
                        String DisplayName = args[0];
                        try {
                            Functions.update("players" , "display_name", DisplayName, dbPlayer.id, "id");
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        try {
                            Functions.update("players", "didStartup", "true", dbPlayer.id, "id");
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        sender.sendMessage("You are now " + DisplayName
                                    + ". You can change the player_name by resetting your player. /reset");
                            return true;
                        }
                    }
                }
            }

        return false;
    }
}
