package at.feddis08.mmorpg.database;
import at.feddis08.mmorpg.MMORPG;

import java.sql.*;
public class JDBC {
    public static Connection myConn = null;
    public static void connectToDb(String ip, String port, String db, String user, String pw){
        try{
            MMORPG.consoleLog("Try to connect with DataBase!");
            //get connection
            myConn = DriverManager.getConnection("jdbc:mysql://" + ip+  ":" + port + "/" + db, user, pw);
            MMORPG.consoleLog("Connection succeed!");
            createPlayerTable();

        }catch (Exception e){
            MMORPG.consoleLog("Connection to DataBase failed!");
            e.printStackTrace();
        }
    }
    public static void createPlayerTable() throws SQLException {
        String sqlCreate = "CREATE TABLE IF NOT EXISTS " + "players"
                + "  (realm           INTEGER ,"
                + "   rank_level          INTEGER,"
                + "   level           INTEGER,"
                + "   player_uuid      VARCHAR(40),"
                + "   player_name     VARCHAR(20),"
                + "   display_name     VARCHAR(20),"
                + "   player_position VARCHAR(20),"
                + "   didStartup INTEGER )";

        Statement stmt = myConn.createStatement();
        stmt.execute(sqlCreate);
        MMORPG.consoleLog("MYSQL table created!");
    }
}