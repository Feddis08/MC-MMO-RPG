package at.feddis08.bukkit.commands;

import at.feddis08.bukkit.MMORPG;
import at.feddis08.tools.Rank_api;
import at.feddis08.tools.discord.DISCORD;
import at.feddis08.tools.io.database.Functions;
import at.feddis08.tools.io.database.objects.Discord_playerObject;
import at.feddis08.tools.io.database.objects.PlayerObject;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.sql.SQLException;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class Discord implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("discord")) {
            PlayerObject dbPlayer = null;
            try {
                dbPlayer = (PlayerObject) Functions.getPlayer("id", sender.getServer().getPlayer(sender.getName()).getUniqueId().toString());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (Objects.equals(dbPlayer.didStartup, "true")) {
                    try {
                        if (Rank_api.isPlayer_allowedTo(dbPlayer.id, "doDiscord") || Rank_api.isPlayer_allowedTo(dbPlayer.id, "*")) {
                           if (Objects.equals(args[0], "link_agree")) {
                               if (args.length == 2){
                                   Discord_playerObject dbDiscord_playerObject = DISCORD.get_discordPlayer_by_discord_id(args[1]);
                                   if (!(dbDiscord_playerObject == null)){
                                       if (Objects.equals(dbDiscord_playerObject.want_link_id, dbPlayer.id)){
                                           Functions.update("players_discord", "id", dbPlayer.id, args[1], "discord_id");
                                           DISCORD.remove_discordPlayer_in_want_list_by_discord_id(dbDiscord_playerObject.discord_id);
                                           DISCORD.api.getUserById(dbDiscord_playerObject.discord_id).get().sendMessage("You are linked with " + dbPlayer.display_name);
                                           Objects.requireNonNull(MMORPG.Server.getPlayer(dbPlayer.player_name)).sendMessage("You are linked with " + dbDiscord_playerObject.display_name);
                                       }else{
                                           sender.sendMessage(ChatColor.RED + "The discord user your trying to link don't want to link with you!");
                                       }
                                   }else{
                                       sender.sendMessage(ChatColor.RED + "That users was not found!");
                                   }
                               }else{
                                   sender.sendMessage(ChatColor.RED + "Wrong usage: /agree <discord_user_id>");
                               }
                           }else{
                               sender.sendMessage(ChatColor.RED + "Command not found!");
                           }
                        } else {
                            sender.sendMessage(ChatColor.RED + "You need the permission: 'doDiscord'!");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        throw new RuntimeException(e);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
            } else{
                sender.sendMessage(ChatColor.RED + "Please do the startup. " + ChatColor.GOLD + "/startup");
            }
        }
        return false;
    }

}
