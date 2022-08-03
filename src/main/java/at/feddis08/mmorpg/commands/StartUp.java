package at.feddis08.mmorpg.commands;

import at.feddis08.mmorpg.io.database.*;
import at.feddis08.mmorpg.io.database.objects.PlayerObject;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.sql.SQLException;

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
            if (dbPlayer.didStartup == "true"){
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
                        if (args[1].equalsIgnoreCase("MagicRealm"))realm = 1;
                        if (args[1].equalsIgnoreCase("BuildingRealm"))realm = 2;
                        if (args[1].equalsIgnoreCase("FightingRealm"))realm = 3;
                        if (realm == 0){
                            sender.sendMessage("For the startup you need to write this command. You must change the parameter:" +
                                    " /startUp {your player_name} {your realm(MagicRealm/BuildingRealm/FightingRealm)}");
                            return true;
                        }
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
                        sender.sendMessage("You are now in the Realm: " + args[1] + " and your name is " + DisplayName
                                    + ". You can change it with: /change player_name");
                            return true;
                        }
                    }
                }
            }

        return false;
    }
}
