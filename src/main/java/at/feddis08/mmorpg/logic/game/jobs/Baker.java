package at.feddis08.mmorpg.logic.game.jobs;

import at.feddis08.mmorpg.database.Functions;

import java.sql.SQLException;

public class Baker {

    public static void setPlayerJob(String id) throws SQLException {
        Functions.update("players", "job", "Baker", id, "id");
    }

}
