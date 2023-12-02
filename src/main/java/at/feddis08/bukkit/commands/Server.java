package at.feddis08.bukkit.commands;

import at.feddis08.Boot;
import at.feddis08.bukkit.cluster_com_client.Start_cluster_client;
import at.feddis08.tools.Rank_api;
import at.feddis08.tools.io.database.Functions;
import at.feddis08.tools.io.database.JDBC;
import at.feddis08.tools.io.database.objects.PlayerObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.json.JSONObject;

import java.io.*;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class Server implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("server")) {
            PlayerObject dbPlayer = null;
            try {
                dbPlayer = (PlayerObject) Functions.getPlayer("id", sender.getServer().getPlayer(sender.getName()).getUniqueId().toString());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (Objects.equals(dbPlayer.didStartup, "true")) {
                    try {
                        if (Rank_api.isPlayer_allowedTo(dbPlayer.id, "doServer") || Rank_api.isPlayer_allowedTo(dbPlayer.id, "*")) {
                            if (Objects.equals(args[0], "database") || Objects.equals(args[0], "db")){
                                if (Objects.equals(args[1], "save")){
                                    Boot.consoleLog(dbPlayer.id + "/" + dbPlayer.display_name + " saves the database!");
                                    sender.sendMessage("The name of your save: " + Functions.save_database_to_JSON());
                                }
                                if (Objects.equals(args[1], "restore") && args.length == 4) {
                                    sender.sendMessage("The name of your save: " + Functions.save_database_to_JSON());
                                    InputStream is = new FileInputStream("MMORPG/" + args[3]);
                                    BufferedReader buf = new BufferedReader(new InputStreamReader(is));
                                    String line = buf.readLine();
                                    StringBuilder sb = new StringBuilder();
                                    while(line != null){
                                        sb.append(line).append("\n");
                                        line = buf.readLine();
                                    }
                                    String save_json = sb.toString();
                                    if (Objects.equals(args[2], "merge")) {
                                        Boot.consoleLog(dbPlayer.id + "/" + dbPlayer.display_name + " restores the database with a merge! used save" + args[3]);
                                        Functions.restore_database_from_save(save_json);
                                    }
                                    if (Objects.equals(args[2], "clear")) {
                                        Boot.consoleLog(dbPlayer.id + "/" + dbPlayer.display_name + " restores the database with a clear! used save: " + args[3]);
                                        Statement stmt = JDBC.myConn.createStatement();
                                        String sql = "drop table data, inventory_tracks, mails, players, players_in_worlds, players_discord, players_balance, players_quests, portal_tracks, ranks, ranks_permissions, users, warps, worlds";
                                        Boot.debugLog(sql);
                                        int myRs = stmt.executeUpdate(sql);
                                        JDBC.check_tables();
                                        Functions.restore_database_from_save(save_json);
                                        sender.sendMessage(myRs + "");
                                    }
                                }
                            }
                            if (Objects.equals(args[0], "switch")) {

                                JSONObject json = new JSONObject();
                                json.put("player_id", dbPlayer.id);
                                json.put("server_name", args[1]);
                                try {
                                    json = Start_cluster_client.client.send_event(json, "send_player_to_server");
                                    Start_cluster_client.client.wait_for_response(json);
                                    Boot.consoleLog("Player sent via CLUSTER!");
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        } else {
                            sender.sendMessage(ChatColor.RED + "You need the permission: 'doServer'!");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
            } else{
                sender.sendMessage(ChatColor.RED + "Please do the startup. " + ChatColor.GOLD + "/startup");
            }
        }
        return false;
    }
}
