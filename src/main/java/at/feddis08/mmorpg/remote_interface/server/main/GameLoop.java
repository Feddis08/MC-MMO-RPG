package at.feddis08.mmorpg.remote_interface.server.main;

import at.feddis08.mmorpg.MMORPG;
import at.feddis08.mmorpg.io.database.Functions;
import at.feddis08.mmorpg.io.database.objects.PlayerObject;
import at.feddis08.mmorpg.io.database.objects.UserObject;
import at.feddis08.mmorpg.minecraft.tools.Methods;
import at.feddis08.mmorpg.remote_interface.server.Start;
import at.feddis08.mmorpg.remote_interface.server.socket.Client;
import at.feddis08.mmorpg.remote_interface.server.socket.Server;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
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
                if (Objects.equals(command[0], "setup_account")) {
                    if (client.player.logged_in){
                        Boolean pass = true;
                        if (!(command.length == 3)){
                            pass = false;
                        }
                        if (Objects.equals(command[1], "")){
                            pass = false;
                        }
                        if (pass){
                            Functions.update("users", "first_name", command[1], client.player.id, "id");
                            Functions.update("users", "last_name", command[2], client.player.id, "id");
                            client.sendMessage("setup_account_passed");
                        }else{
                            client.sendMessage("setup_account_not_passed");
                        }
                    }
                }
                if (Objects.equals(command[0], "get_own_user")) {
                    if (client.player.logged_in){
                        UserObject userObject = Functions.getUser("id", client.player.id);
                        client.sendMessage("get_own_user" + spacing + userObject.id + spacing + userObject.first_name + spacing + userObject.last_name + spacing + userObject.time_created);
                    }
                }
                if (Objects.equals(command[0], "send_chat_message")) {
                    if (client.player.logged_in){
                        PlayerObject dbPlayer = Functions.getPlayer("id", client.player.id);
                        dbPlayer.send_chat_message(command[1]);
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