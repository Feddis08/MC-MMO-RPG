package at.feddis08.mmorpg.database;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.SQLException;

public class AddPlayer {
    public static boolean addPlayer(PlayerJoinEvent event) throws SQLException {
        Player player = event.getPlayer();
        DataObject dataObj = new DataObject();
        dataObj.realm = 0;
        dataObj.rank_level = 1;
        dataObj.level = 0;
        dataObj.player_uuid = player.getUniqueId().toString();
        dataObj.player_name = player.getName();
        dataObj.player_position = "";
        dataObj.didStartup = 0;
        dataObj.online = 1;
        Functions.createPlayer(dataObj);
        return true;
    }
}
