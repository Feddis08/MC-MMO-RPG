package at.feddis08.tools.io.database;

import at.feddis08.tools.io.database.objects.PlayerObject;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.SQLException;

public class AddPlayer {
    public static boolean addPlayer(PlayerJoinEvent event) throws SQLException {
        Player player = event.getPlayer();
        PlayerObject dataObj = new PlayerObject();
        dataObj.stage = 0;
        dataObj.id = player.getUniqueId().toString();
        dataObj.player_name = player.getName();
        dataObj.current_world_id = "world";
        Functions.createPlayer(dataObj);
        return true;
    }
}
