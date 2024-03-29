package at.feddis08.bukkit.commands;

import at.feddis08.tools.Rank_api;
import at.feddis08.tools.io.database.Functions;
import at.feddis08.tools.io.database.objects.PlayerObject;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.sql.SQLException;
import java.util.Objects;

public class TemplateCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("gm")) {
            PlayerObject dbPlayer = null;
            try {
                dbPlayer = Functions.getPlayer("id", sender.getServer().getPlayer(sender.getName()).getUniqueId().toString());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (Objects.equals(dbPlayer.didStartup, "true")) {
                try {
                    if (Rank_api.isPlayer_allowedTo(dbPlayer.id, "doGamemode") || Rank_api.isPlayer_allowedTo(dbPlayer.id, "*")) {

                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
}