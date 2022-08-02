package at.feddis08.mmorpg.commands;

import at.feddis08.mmorpg.MMORPG;
import at.feddis08.mmorpg.database.Functions;
import at.feddis08.mmorpg.database.objects.InventoryTrackObject;
import at.feddis08.mmorpg.database.objects.PlayerObject;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.sql.SQLException;
import java.util.Objects;

public class SetInventoryTrack implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("setInventoryTrack")) {
            PlayerObject dbPlayer = null;
            try {
                dbPlayer = Functions.getPlayer("id", sender.getServer().getPlayer(sender.getName()).getUniqueId().toString());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (Objects.equals(dbPlayer.didStartup, "true")) {
                try {
                    if (Rank.isPlayer_allowedTo(dbPlayer.id, "doSetInventoryTrack") || Rank.isPlayer_allowedTo(dbPlayer.id, "*")) {

                        InventoryTrackObject inventoryTrackObject = Functions.getInventoryTrack("id", args[0]);
                        if (Objects.equals(String.valueOf(inventoryTrackObject.id), args[0])){
                            Functions.update("inventory_tracks", "world_id", args[1], args[0], "id");
                            Functions.update("inventory_tracks", "type", args[2], args[0], "id");
                            Functions.update("inventory_tracks", "x", args[3], args[0], "id");
                            Functions.update("inventory_tracks", "y", args[4], args[0], "id");
                            Functions.update("inventory_tracks", "z", args[5], args[0], "id");
                            MMORPG.consoleLog("Created/Updated a InventoryTrack: " + inventoryTrackObject.id + "; for " + dbPlayer.display_name);
                            sender.sendMessage(ChatColor.GREEN + "Done!");
                        }else {
                            inventoryTrackObject.id = Integer.parseInt(args[0]);
                            inventoryTrackObject.world_id = args[1];
                            inventoryTrackObject.type = args[2];
                            inventoryTrackObject.x = Integer.parseInt(args[3]);
                            inventoryTrackObject.y = Integer.parseInt(args[4]);
                            inventoryTrackObject.z = Integer.parseInt(args[5]);
                            Functions.createInventoryTrack(inventoryTrackObject);
                            MMORPG.consoleLog("Created/Updated a InventoryTrack: " + inventoryTrackObject.id + "; for " + dbPlayer.display_name);
                            sender.sendMessage(ChatColor.GREEN + "Done!");
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
}