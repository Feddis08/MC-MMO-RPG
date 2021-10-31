package at.feddis08.mmorpg.commands;

import at.feddis08.mmorpg.database.DataObject;
import at.feddis08.mmorpg.database.Functions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.sql.SQLException;

public class StartUp implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("startUp")){
            DataObject dbPlayer = null;
            try {
                dbPlayer = Functions.searchWithPlayerName(sender.getName());
            } catch (SQLException e) {
                e.printStackTrace();
                sender.sendMessage("Sorry, but something went wrong with the database!");
            }
            if (dbPlayer.didStartup == 1){
                sender.sendMessage("You already did the startup!");
                return true;
            }else {
                if (args.length == 0 || args.length == 1) {
                    sender.sendMessage("For the startup you need to write this command. You must change the parameter:" +
                            " /startUp {your player_name} {your realm(MagicRealm/BuildingRealm/FightingRealm)}");
                }else{
                    if (args.length >= 1){
                        String DisplayName = args[0];
                        Integer realm = 0;
                        if (args[1] == "MagicRealm")realm = 1;
                        if (args[1] == "BuildingRealm")realm = 2;
                        if (args[1] == "FightingRealm")realm = 3;
                        if (realm == 0){
                            sender.sendMessage("For the startup you need to write this command. You must change the parameter:" +
                                    " /startUp {your player_name} {your realm(MagicRealm/BuildingRealm/FightingRealm)}");
                            return true;
                        }
                        try {
                            Functions.updatePlayerWithName("realm", realm.toString(), sender.getName());
                            Functions.updatePlayerWithName("display_name", DisplayName, sender.getName());
                            Functions.updatePlayerWithName("didStartup", "1", sender.getName());
                            return true;

                        } catch (SQLException e) {
                            e.printStackTrace();
                            sender.sendMessage("Sorry, but something went wrong with the database! Maybe your player_name is over 20 symbols");
                        }
                    }
                }
            }

        }
        return false;
    }
}
