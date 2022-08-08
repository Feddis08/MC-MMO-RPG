package at.feddis08.mmorpg.io.database;

import at.feddis08.mmorpg.io.database.objects.PlayerObject;
import at.feddis08.mmorpg.io.database.objects.Request;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class Object_Manager {

    public static ArrayList<Request> requests = new ArrayList<>();


    public static void addRequest(String type, String function, String identifier){
        Request request = new Request();
        request.type = type;
        request.function = function;
        request.identifier = identifier;
        requests.add(request);
    }

    public static void checkRequests() throws SQLException {
        Integer index = 0;
        if (!(requests.size() == 0)){
            while (index < requests.size()) {
                Request request = requests.get(index);
                request_for_player(request);
                requests.remove(index);
                index = index + 1;
            }
        }
    }
    public static void request_for_player(Request request) throws SQLException {
        if (Objects.equals(request.type, "player")){
            Var.remove_Player_by_id(request.identifier);
            PlayerObject dbPlayer = Functions.getPlayer("id", request.identifier);
            Var.playerObjects.add(dbPlayer);
        }
    }

}
