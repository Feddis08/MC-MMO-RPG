package at.feddis08.mmorpg.io.database;

import at.feddis08.mmorpg.io.database.objects.PlayerObject;
import at.feddis08.mmorpg.logic.game.trade.invObjects.PlayerInvObject;

import java.util.ArrayList;
import java.util.Objects;

public class Var {

    public static ArrayList<PlayerObject> playerObjects = new ArrayList<>();


    public static PlayerObject get_Player_by_id(String player_id){
        Integer index = 0;
        PlayerObject result = null;
        while (index < playerObjects.size()){
            if (Objects.equals(playerObjects.get(index).id, player_id))
                result = playerObjects.get(index);
            index = index + 1;
        }
        return result;
    }
    public static PlayerObject remove_Player_by_id(String player_id){
        Integer index = 0;
        while (index < playerObjects.size()){
            if (Objects.equals(playerObjects.get(index).id, player_id))
                playerObjects.remove(index);
            index = index + 1;
        }
    }

}
