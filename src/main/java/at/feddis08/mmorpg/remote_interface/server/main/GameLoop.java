package at.feddis08.mmorpg.remote_interface.server.main;

import at.feddis08.mmorpg.MMORPG;
import at.feddis08.mmorpg.commands.Rank;
import at.feddis08.mmorpg.io.database.Functions;
import at.feddis08.mmorpg.io.database.objects.PlayerObject;
import at.feddis08.mmorpg.io.database.objects.UserObject;
import at.feddis08.mmorpg.minecraft.tools.Methods;
import at.feddis08.mmorpg.remote_interface.server.Start;
import at.feddis08.mmorpg.remote_interface.server.socket.Client;
import at.feddis08.mmorpg.remote_interface.server.socket.Server;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.SystemUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.nio.file.Files;
import java.sql.Array;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;
import java.util.Objects;

public class GameLoop extends Thread {
    public void run(){
        while (true){
            try {
                loop();
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void loop() throws IOException, SQLException {
        String spacing = Start.spacing;
        Integer index = 0;
        try {
        while (index < Server.clients.size()) {
            Client client = Server.clients.get(index);
            Integer index2 = 0;
            try {
            while (index2 < client.requests.size()) {
                String request = client.requests.get(index2);
                String[] command = request.split(Start.spacing);
                if (Objects.equals(command[0], "join_server_with_u&t")) {
                    if (!(client.player.logged_in)) {
                        UserObject userObject = Functions.getUser("id", Functions.getPlayer("display_name", command[1]).id);
                        if (Objects.equals(userObject.id, "")) {
                            client.sendMessage("join_not_allowed" + Start.spacing + "User was not found!");
                        } else {
                            if (Objects.equals(userObject.hash, Methods.get_hash(command[2]))) {
                                if (Objects.equals(userObject.first_name, "")) {
                                    client.player.id = userObject.id;
                                    client.player.logged_in = true;
                                    client.sendMessage("join_allowed" + Start.spacing + "Account not ready!");
                                    client.sendMessage("start_setup");
                                } else {
                                    client.player.id = userObject.id;
                                    client.player.logged_in = true;
                                    client.sendMessage("join_allowed" + Start.spacing + "Hi, " + userObject.first_name + "!");
                                }
                            } else {
                                client.sendMessage("join_not_allowed" + Start.spacing + "Password is wrong!");
                            }
                        }
                    }
                }
                if (Objects.equals(command[0], "try_join")) {
                        client.sendMessage("try_join" + Start.spacing + command[1]);
                }
                if (Objects.equals(command[0], "audio_input_stream")) {
                    if (client.player.logged_in){
                        Thread t = new Thread(){
                            @Override
                            public void run(){
                                if (client.in_chat) {
                                    try {
                                        Server.send_audio_to_channel_by_client(client, Base64.getDecoder().decode(command[1]));
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            }
                        };
                        t.start();
                    }
                }
                if (Objects.equals(command[0], "setup_account")) {
                    if (client.player.logged_in){
                        Boolean pass = true;
                        if (!(command.length == 4)){
                            pass = false;
                        }
                        if (Objects.equals(command[1], "")){
                            pass = false;
                        }
                        if (pass){
                            Functions.update("users", "first_name", command[1], client.player.id, "id");
                            Functions.update("users", "last_name", command[2], client.player.id, "id");
                            Functions.update("users", "data_json", command[3], client.player.id, "id");
                            client.sendMessage("setup_account_passed");
                        }else{
                            client.sendMessage("setup_account_not_passed");
                        }
                    }
                }
                if (Objects.equals(command[0], "get_own_user")) {
                    if (client.player.logged_in){
                        UserObject userObject = Functions.getUser("id", client.player.id);
                        client.sendMessage("get_own_user" + spacing + userObject.id + spacing + userObject.first_name + spacing + userObject.last_name + spacing + userObject.time_created + spacing + userObject.data_json);
                    }
                }
                if (Objects.equals(command[0], "go_in_chat")) {
                    if (client.player.logged_in){
                        client.in_chat = true;
                    }
                }
                if (Objects.equals(command[0], "go_out_chat")) {
                    if (client.player.logged_in){
                        client.in_chat = false;
                    }
                }
                if (Objects.equals(command[0], "send_chat_message")) {
                    if (client.player.logged_in){
                        PlayerObject dbPlayer = Functions.getPlayer("id", client.player.id);
                        dbPlayer.send_chat_message(command[1]);
                    }
                }
                if (Objects.equals(command[0], "reload_server")) {
                    if (client.player.logged_in){
                        PlayerObject dbPlayer = Functions.getPlayer("id", client.player.id);
                        if (Rank.has_permission_from_rank(dbPlayer.player_rank, "do_reload_server") || Rank.has_permission_from_rank(dbPlayer.player_rank, "*")){
                            MMORPG.consoleLog("Do reload by: " + dbPlayer.display_name + ":" + dbPlayer.id);
                            MMORPG.Server.reload();
                        }
                    }
                }
                if (Objects.equals(command[0], "post_file")) {
                    if (client.player.logged_in){
                        PlayerObject dbPlayer = Functions.getPlayer("id", client.player.id);
                        if (Rank.has_permission_from_rank(dbPlayer.player_rank, "do_post_file") || Rank.has_permission_from_rank(dbPlayer.player_rank, "*")){
                            FileUtils.writeByteArrayToFile(new File(command[1]), Base64.getDecoder().decode(command[2]));
                            client.sendMessage("post_file_finished" + spacing + command[1]);
                        }
                    }
                }
                if (Objects.equals(command[0], "get_file")) {
                    if (client.player.logged_in){
                        PlayerObject dbPlayer = Functions.getPlayer("id", client.player.id);
                        if (Rank.has_permission_from_rank(dbPlayer.player_rank, "do_get_file") || Rank.has_permission_from_rank(dbPlayer.player_rank, "*")){
                            File f = new File(command[1]);
                            client.sendMessage("send_file" + spacing + f.getName() + spacing + Base64.getEncoder().encodeToString(Files.readAllBytes(f.toPath())));
                        }
                    }
                }
                if (Objects.equals(command[0], "get_server_info")) {
                    if (client.player.logged_in){
                        PlayerObject dbPlayer = Functions.getPlayer("id", client.player.id);
                        if (Rank.has_permission_from_rank(dbPlayer.player_rank, "do_get_server_info") || Rank.has_permission_from_rank(dbPlayer.player_rank, "*")){
                            JSONObject jsonObject = new JSONObject();
                            RuntimeMXBean rb = ManagementFactory.getRuntimeMXBean();
                            long uptime = rb.getUptime();
                            jsonObject.put("server_name", MMORPG.Server.getMotd());
                            jsonObject.put("server_type_name", MMORPG.Server.getName());
                            jsonObject.put("online_players", MMORPG.Server.getOnlinePlayers().size());
                            jsonObject.put("server_up_time", uptime);
                            jsonObject.put("os_name", SystemUtils.OS_NAME);
                            jsonObject.put("os_user_name", SystemUtils.USER_NAME);
                            client.sendMessage("send_server_info" + spacing + jsonObject);
                        }
                    }
                }
                if (Objects.equals(command[0], "get_list_files")) {
                    if (client.player.logged_in){
                        PlayerObject dbPlayer = Functions.getPlayer("id", client.player.id);
                        if (Rank.has_permission_from_rank(dbPlayer.player_rank, "do_get_list_file") || Rank.has_permission_from_rank(dbPlayer.player_rank, "*")){
                            File f = new File(command[1]);
                            client.sendMessage("send_list_files" + spacing + Methods.serializeArray(f.list()));
                        }
                    }
                }
                if (Objects.equals(command[0], "ping")) {
                    if (client.player.logged_in){
                        client.sendMessage("pong");
                    }
                }
                index2 = index2 + 1;
            }
            } catch (Exception e) {
                index2 = index2 + 1;
                e.printStackTrace();
            }
            client.requests.clear();
            index = index + 1;
        }
        } catch (Exception e) {
            index = index + 1;
            e.printStackTrace();
        }
    }
}