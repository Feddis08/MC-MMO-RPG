package at.feddis08.mmorpg.commands;

import at.feddis08.mmorpg.database.Functions;
import at.feddis08.mmorpg.database.objects.PlayerObject;
import at.feddis08.mmorpg.scoreboads.PlayerInfoScoreboard;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.sql.SQLException;
import java.util.Objects;

public class SetScoreboard implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("setScoreboard")) {
            PlayerObject dbPlayer = null;
            try {
                dbPlayer = Functions.getPlayer("id", sender.getServer().getPlayer(sender.getName()).getUniqueId().toString());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (Objects.equals(dbPlayer.didStartup, "true")) {
                if (args.length == 1){
                    if (Objects.equals(args[0], "playerInfo")){
                        try {
                            if (Rank.isPlayer_allowedTo(dbPlayer.id, "doSetPlayerInfoScoreboard") || Rank.isPlayer_allowedTo(dbPlayer.id, "*")) {
                                PlayerInfoScoreboard.setPlayerScoreboard(sender.getServer().getPlayer(sender.getName()));
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return false;
    }
}