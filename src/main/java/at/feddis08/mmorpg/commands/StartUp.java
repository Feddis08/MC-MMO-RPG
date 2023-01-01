package at.feddis08.mmorpg.commands;

import at.feddis08.mmorpg.io.database.*;
import at.feddis08.mmorpg.io.database.objects.PlayerObject;
import at.feddis08.mmorpg.minecraft.tools.Methods;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
                if (args.length > 2 || args.length == 1) {
                    sender.sendMessage("For the startup you need to write this command. You must change the parameters:" +
                            " /startUp {your player_name} {password}");
                }else{
                    if (args.length == 2){
                        String DisplayName = args[0];
                        try {
                            Functions.update("players" , "display_name", DisplayName, dbPlayer.id, "id");
                            Functions.update("players", "didStartup", "true", dbPlayer.id, "id");
                            Functions.update("users", "hash", Methods.get_hash(args[1]), dbPlayer.id, "id");
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        Player p = (Player) sender;
                        p.kickPlayer("You are now " + DisplayName
                                    + ". You can change the player_name/password by redoing the startup. Write  /reset  to start!");
                            return true;
                        }
                    }
                }
            }

        return false;
    }
}
