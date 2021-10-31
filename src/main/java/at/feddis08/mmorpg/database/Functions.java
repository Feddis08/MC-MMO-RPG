package at.feddis08.mmorpg.database;
import at.feddis08.mmorpg.MMORPG;
import sun.net.www.content.text.Generic;

import java.sql.*;

public class Functions {
    public static void createPlayer(DataObject dataObj) throws SQLException {
        Statement stmt = JDBC.myConn.createStatement();
        String sqlString = "'"
                + dataObj.realm
                + "', '"
                + dataObj.rank_level
                + "', '"
                + dataObj.level
                + "', '"
                + dataObj.player_uuid
                + "', '"
                + dataObj.player_name
                + "', '"
                + dataObj.display_name
                + "', '"
                + dataObj.player_position
                + "', '"
                + dataObj.didStartup
                + "'";
        String sql = "insert into players"
                + "(realm, rank_level, level, player_uuid, player_name, display_name, player_position, didStartup)"
                + "values (" + sqlString + ")";
        stmt.executeUpdate(sql);
        MMORPG.consoleLog("Player created: " + dataObj.player_name + "!");
    }
    public static DataObject searchWithPlayerName(String playerName) throws SQLException {
        Statement stmt = JDBC.myConn.createStatement();
        String sql = "select * from players where player_name = " + "'" + playerName + "'";
        ResultSet myRs = stmt.executeQuery(sql);
        DataObject dataObj = new DataObject();
        while (myRs.next()) {
            dataObj.realm = myRs.getInt("realm");
            dataObj.rank_level = myRs.getInt("rank_level");
            dataObj.level = myRs.getInt("level");
            dataObj.player_uuid = myRs.getString("player_uuid");
            dataObj.display_name = myRs.getString("display_name");
            dataObj.player_name = myRs.getString("player_name");
            dataObj.player_position = myRs.getString("player_position");
            dataObj.didStartup = myRs.getInt("didStartup");
        }
        MMORPG.consoleLog("DataBase read for " + playerName);
        return dataObj;
    }
    public static void updatePlayerWithName(String column, String newValue, String playerName) throws SQLException {
        Statement stmt = JDBC.myConn.createStatement();
        String sql = "update players set "
                + column
                + " = "
                + " '"
                + newValue
                + "'"
                + "where player_name="
                + " '"
                + playerName
                + "'";
        stmt.executeUpdate(sql);
        MMORPG.consoleLog("Updated " + column + " to " + newValue + " from " + playerName);

    }
}

