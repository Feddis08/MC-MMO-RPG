package at.feddis08.bukkit.commands;

import at.feddis08.Boot;
import at.feddis08.tools.Rank_api;
import at.feddis08.tools.io.database.Functions;
import at.feddis08.tools.io.database.objects.PlayerObject;
import at.feddis08.tools.io.database.objects.PortalTrackObject;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.sql.SQLException;
import java.util.Objects;

public class SetPortalTrack implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("setportaltrack")) {
            PlayerObject dbPlayer = null;
            try {
                dbPlayer = (PlayerObject) Functions.getPlayer("id", sender.getServer().getPlayer(sender.getName()).getUniqueId().toString());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (Objects.equals(dbPlayer.didStartup, "true")) {
                    if (args.length == 12) {
                        try {
                            if (Rank_api.isPlayer_allowedTo(dbPlayer.id, "doSetPortalTrack") || Rank_api.isPlayer_allowedTo(dbPlayer.id, "*")) {
                                PortalTrackObject dbPortalTrackObject = Functions.getPortalTrackById(args[0]);
                                if (Objects.equals(dbPortalTrackObject.id, args[0])){
                                    Functions.update("portal_tracks", "from_world", args[1], args[0], "id");
                                    Functions.update("portal_tracks", "to_world", args[2], args[0], "id");
                                    Functions.update("portal_tracks", "x1", args[3], args[0], "id");
                                    Functions.update("portal_tracks", "x2", args[4], args[0], "id");
                                    Functions.update("portal_tracks", "x3", args[5], args[0], "id");
                                    Functions.update("portal_tracks", "y1", args[6], args[0], "id");
                                    Functions.update("portal_tracks", "y2", args[7], args[0], "id");
                                    Functions.update("portal_tracks", "y3", args[8], args[0], "id");
                                    Functions.update("portal_tracks", "z1", args[9], args[0], "id");
                                    Functions.update("portal_tracks", "z2", args[10], args[0], "id");
                                    Functions.update("portal_tracks", "z3", args[11], args[0], "id");
                                    Boot.consoleLog("updated portal_track: " + dbPortalTrackObject.id + " for " + dbPlayer.display_name + "!");
                                    sender.sendMessage(ChatColor.GREEN + "Done, updated old portal_track!");
                                }else{
                                    dbPortalTrackObject.id = args[0];
                                    dbPortalTrackObject.from_world = args[1];
                                    dbPortalTrackObject.to_world = args[2];
                                    dbPortalTrackObject.x1 = Integer.parseInt(args[3]);
                                    dbPortalTrackObject.x2 = Integer.parseInt(args[4]);
                                    dbPortalTrackObject.x3 = Integer.parseInt(args[5]);
                                    dbPortalTrackObject.y1 = Integer.parseInt(args[6]);
                                    dbPortalTrackObject.y2 = Integer.parseInt(args[7]);
                                    dbPortalTrackObject.y3 = Integer.parseInt(args[8]);
                                    dbPortalTrackObject.z1 = Integer.parseInt(args[9]);
                                    dbPortalTrackObject.z2 = Integer.parseInt(args[10]);
                                    dbPortalTrackObject.z3 = Integer.parseInt(args[11]);
                                    Functions.createPortalTrack(dbPortalTrackObject);
                                    Boot.consoleLog("Created portal_track: " + dbPortalTrackObject.id + " for " + dbPlayer.display_name + "!");
                                    sender.sendMessage(ChatColor.GREEN + "Done, created new portal_track!");
                                }
                            } else {
                                sender.sendMessage(ChatColor.RED + "You need the permission: 'doSetPortalTrack'!");
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }else{
                        sender.sendMessage("Wrong usage: /setPortalTrack id/name from_world to_world x1 x2 x3 y1 y2 y3 z1 z2 z3");
                    }
            } else{
                sender.sendMessage(ChatColor.RED + "Please do the startup. " + ChatColor.GOLD + "/startup");
            }
        }
        return false;
    }

}
