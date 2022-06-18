package at.feddis08.mmorpg.database;
import at.feddis08.mmorpg.MMORPG;

import java.sql.*;
public class JDBC {
    public static Connection myConn = null;
    public static void connectToDb(String ip, String port, String db, String user, String pw){
        try{
            MMORPG.consoleLog("Try to connect with DataBase!");
            myConn = DriverManager.getConnection("jdbc:mysql://" + ip+  ":" + port + "/" + db, user, pw);
            MMORPG.consoleLog("Connection succeed!");
            MMORPG.consoleLog("Checking all Tables ...");
            createDataTable();
            createPlayerTable();
            createRanksTable();
            createWorldsTable();
            createPlayers_in_worldsTable();
            createRanks_permissionsTable();
            createMailsTable();
            createBlock_break_countTable();
        }catch (Exception e){
            MMORPG.consoleLog("Connection to DataBase failed!");
            e.printStackTrace();
        }
    }
    public static void createPlayerTable() throws SQLException {
        String sqlCreate = "CREATE TABLE IF NOT EXISTS " + "players"
                + "  (realm           INTEGER ,"
                + "   stage           INTEGER,"
                + "   current_world_id VARCHAR(200),"
                + "   gamemode           INTEGER,"
                + "   id                VARCHAR(40),"
                + "   player_rank                VARCHAR(20),"
                + "   online           VARCHAR(5),"
                + "   player_name     VARCHAR(20),"
                + "   display_name     VARCHAR(20),"
                + "   didStartup VARCHAR(5))";

        Statement stmt = myConn.createStatement();
        stmt.execute(sqlCreate);
        MMORPG.consoleLog("MYSQL player tabel created!");
    }
    public static void createRanksTable() throws SQLException {
        String sqlCreate = "CREATE TABLE IF NOT EXISTS " + "ranks"
                + "   (name VARCHAR(20),"
                + "   parent VARCHAR(40),"
                + "   id VARCHAR(40),"
                + "   rank_level INTEGER,"
                + "   rank_color VARCHAR(20),"
                + "   prefix_color VARCHAR(20) ,"
                + "   prefix VARCHAR (10))";

        Statement stmt = myConn.createStatement();
        stmt.execute(sqlCreate);
        MMORPG.consoleLog("MYSQL ranks tabel created!");
    }
    public static void createMailsTable() throws SQLException {
        String sqlCreate = "CREATE TABLE IF NOT EXISTS " + "mails"
                + "   (sender_id VARCHAR(40),"
                + "  receiver_id VARCHAR(40),"
                + "  date VARCHAR(40),"
                + "  opened VARCHAR(5),"
                + "  title VARCHAR(256),"
                + "  id INTEGER,"
                + "  message VARCHAR(256))";

        Statement stmt = myConn.createStatement();
        stmt.execute(sqlCreate);
        MMORPG.consoleLog("MYSQL mails tabel created!");
    }
    public static void createRanks_permissionsTable() throws SQLException {
        String sqlCreate = "CREATE TABLE IF NOT EXISTS " + "ranks_permissions"
                + "   (id VARCHAR(40),"
                + "  permission VARCHAR(20))";

        Statement stmt = myConn.createStatement();
        stmt.execute(sqlCreate);
        MMORPG.consoleLog("MYSQL ranks_permissions tabel created!");
    }
    public static void createDataTable() throws SQLException {
        String sqlCreate = "CREATE TABLE IF NOT EXISTS " + "data"
                + "  (reloads INTEGER,"
                + "   ranks INTEGER,"
                + "   enabled INTEGER,"
                + "   name VARCHAR(20),"
                + "   registerdPlayers INTEGER,"
                + "   owner_name VARCHAR(20),"
                + "   onlinePlayers INTEGER )";

        Statement stmt = myConn.createStatement();
        stmt.execute(sqlCreate);
        MMORPG.consoleLog("MYSQL data tabel created!");
    }
    public static void createWorldsTable() throws SQLException {
        String sqlCreate = "CREATE TABLE IF NOT EXISTS " + "worlds"
                + "  (name VARCHAR(200),"
                + "   id VARCHAR(40),"
                + "   players_on INTEGER,"
                + "   type VARCHAR(20),"
                + "   loaded VARCHAR(5) )";

        Statement stmt = myConn.createStatement();
        stmt.execute(sqlCreate);
        MMORPG.consoleLog("MYSQL worlds tabel created!");
    }
    public static void createBlock_break_countTable() throws SQLException {
        String sqlCreate = "CREATE TABLE IF NOT EXISTS " + "block_break_counter"
                + "  (block_name VARCHAR(40),"
                + "   player_id VARCHAR(40),"
                + "   count INTEGER,"
                + "   level INTEGER)";

        Statement stmt = myConn.createStatement();
        stmt.execute(sqlCreate);
        MMORPG.consoleLog("MYSQL block_break_count tabel created!");
    }
    public static void createPlayers_in_worldsTable() throws SQLException {
        String sqlCreate = "CREATE TABLE IF NOT EXISTS " + "players_in_worlds"
                + "   (id VARCHAR(40),"
                + "   world_id VARCHAR(40),"
                + "   x INTEGER,"
                + "   y INTEGER,"
                + "   z INTEGER )";

        Statement stmt = myConn.createStatement();
        stmt.execute(sqlCreate);
        MMORPG.consoleLog("MYSQL players_in_worlds table created!");
    }
}