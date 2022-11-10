package at.feddis08.mmorpg.io.database;
import at.feddis08.mmorpg.MMORPG;

import java.sql.*;
public class JDBC {
    public static Connection myConn = null;
    public static void connectToDb(String ip, String port, String db, String user, String pw){
        try{
            MMORPG.debugLog("Try to connect with DataBase!");
            myConn = DriverManager.getConnection("jdbc:mysql://" + ip+  ":" + port + "/" + db, user, pw);
            MMORPG.debugLog("Connection succeed!");
            MMORPG.debugLog("Checking all Tables and create if needed ...");
            createDataTable();
            createPlayerTable();
            createRanksTable();
            createWorldsTable();
            createPlayers_in_worldsTable();
            createRanks_permissionsTable();
            createMailsTable();
            createBlock_break_countTable();
            createPlayers_stats_table();
            createInventoryTracksTable();
            createPlayers_balanceTable();
            createPortalTracksTable();
            createWarpsTable();
            createDiscordPlayerTable();
            createPlayerQuestTable();
            createUsersTable();
        }catch (Exception e){
            MMORPG.debugLog("Connection to DataBase failed!");
            e.printStackTrace();
        }
    }
    public static void createUsersTable() throws SQLException {
        String sqlCreate = "CREATE TABLE IF NOT EXISTS " + "users"
                + "  (time_created           VARCHAR(40) ,"
                + "   hash VARCHAR(256),"
                + "   first_name VARCHAR(40),"
                + "   last_name VARCHAR(40),"
                + "   id                VARCHAR(40))";

        Statement stmt = myConn.createStatement();
        stmt.execute(sqlCreate);
        MMORPG.debugLog("MYSQL players_quests table created!");
    }
    public static void createPlayerQuestTable() throws SQLException {
        String sqlCreate = "CREATE TABLE IF NOT EXISTS " + "players_quests"
                + "  (progress           INTEGER ,"
                + "   quest_name VARCHAR(256),"
                + "   id                VARCHAR(40))";

        Statement stmt = myConn.createStatement();
        stmt.execute(sqlCreate);
        MMORPG.debugLog("MYSQL players_quests table created!");
    }
    public static void createDiscordPlayerTable() throws SQLException {
        String sqlCreate = "CREATE TABLE IF NOT EXISTS " + "players_discord"
                + "  (online           INTEGER ,"
                + "   discord_id VARCHAR(40),"
                + "   id                VARCHAR(40),"
                + "   display_name     VARCHAR(20))";

        Statement stmt = myConn.createStatement();
        stmt.execute(sqlCreate);
        MMORPG.debugLog("MYSQL players_discord table created!");
    }
    public static void createPlayerTable() throws SQLException {
        String sqlCreate = "CREATE TABLE IF NOT EXISTS " + "players"
                + "  (realm           INTEGER ,"
                + "   stage           INTEGER,"
                + "   current_world_id VARCHAR(200),"
                + "   gamemode           INTEGER,"
                + "   id                VARCHAR(40),"
                + "   job                VARCHAR(40),"
                + "   player_rank                VARCHAR(20),"
                + "   online           VARCHAR(5),"
                + "   player_name     VARCHAR(20),"
                + "   display_name     VARCHAR(20),"
                + "   didStartup VARCHAR(5))";

        Statement stmt = myConn.createStatement();
        stmt.execute(sqlCreate);
        MMORPG.debugLog("MYSQL player tabel created!");
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
        MMORPG.debugLog("MYSQL ranks tabel created!");
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
        MMORPG.debugLog("MYSQL mails tabel created!");
    }
    public static void createWarpsTable() throws SQLException {
        String sqlCreate = "CREATE TABLE IF NOT EXISTS " + "warps"
                + "   (id VARCHAR(40),"
                + "  world_name VARCHAR(40),"
                + "  x INTEGER,"
                + "  y INTEGER,"
                + "  z INTEGER )";

        Statement stmt = myConn.createStatement();
        stmt.execute(sqlCreate);
        MMORPG.debugLog("MYSQL warps table created!");
    }
    public static void createPortalTracksTable() throws SQLException {
        String sqlCreate = "CREATE TABLE IF NOT EXISTS " + "portal_tracks"
                + "   (id VARCHAR(40),"
                + "  from_world VARCHAR(40),"
                + "  to_world VARCHAR(40),"
                + "  x1 INTEGER,"
                + "  x2 INTEGER,"
                + "  x3 INTEGER,"
                + "  y1 INTEGER,"
                + "  y2 INTEGER,"
                + "  y3 INTEGER,"
                + "  z1 INTEGER,"
                + "  z2 INTEGER,"
                + "  z3 INTEGER )";

        Statement stmt = myConn.createStatement();
        stmt.execute(sqlCreate);
        MMORPG.debugLog("MYSQL portal_tracks table created!");
    }
    public static void createRanks_permissionsTable() throws SQLException {
        String sqlCreate = "CREATE TABLE IF NOT EXISTS " + "ranks_permissions"
                + "   (id VARCHAR(40),"
                + "  permission VARCHAR(256))";

        Statement stmt = myConn.createStatement();
        stmt.execute(sqlCreate);
        MMORPG.debugLog("MYSQL ranks_permissions tabel created!");
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
        MMORPG.debugLog("MYSQL data tabel created!");
    }
    public static void createWorldsTable() throws SQLException {
        String sqlCreate = "CREATE TABLE IF NOT EXISTS " + "worlds"
                + "  (name VARCHAR(200),"
                + "   id VARCHAR(40),"
                + "   autoload VARCHAR(2),"
                + "   players_on INTEGER,"
                + "   type VARCHAR(20),"
                + "   loaded VARCHAR(5) )";

        Statement stmt = myConn.createStatement();
        stmt.execute(sqlCreate);
        MMORPG.debugLog("MYSQL worlds tabel created!");
    }
    public static void createBlock_break_countTable() throws SQLException {
        String sqlCreate = "CREATE TABLE IF NOT EXISTS " + "block_break_counter"
                + "  (block_name VARCHAR(40),"
                + "   player_id VARCHAR(40),"
                + "   count INTEGER)";

        Statement stmt = myConn.createStatement();
        stmt.execute(sqlCreate);
        MMORPG.debugLog("MYSQL block_break_count table created!");
    }
    public static void createPlayers_stats_table() throws SQLException {
        String sqlCreate = "CREATE TABLE IF NOT EXISTS " + "players_stats"
                + "  (player_id VARCHAR(40),"
                + "   strength INTEGER,"
                + "   endu INTEGER,"
                + "   soul INTEGER,"
                + "   general_experience INTEGER,"
                + "   agil INTEGER,"
                + "   woodcut_experience INTEGER,"
                + "   level INTEGER)";

        Statement stmt = myConn.createStatement();
        stmt.execute(sqlCreate);
        MMORPG.debugLog("MYSQL players_stats table created!");
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
        MMORPG.debugLog("MYSQL players_in_worlds table created!");
    }
    public static void createInventoryTracksTable() throws SQLException {
        String sqlCreate = "CREATE TABLE IF NOT EXISTS " + "inventory_tracks"
                + "   (id INTEGER,"
                + "   world_id VARCHAR(40),"
                + "   type VARCHAR(256),"
                + "   x INTEGER,"
                + "   y INTEGER,"
                + "   z INTEGER )";

        Statement stmt = myConn.createStatement();
        stmt.execute(sqlCreate);
        MMORPG.debugLog("MYSQL inventory_tracks table created!");
    }
    public static void createPlayers_balanceTable() throws SQLException {
        String sqlCreate = "CREATE TABLE IF NOT EXISTS " + "players_balance"
                + "   (player_id VARCHAR(40),"
                + "   pocket INTEGER,"
                + "   stock_market INTEGER)";

        Statement stmt = myConn.createStatement();
        stmt.execute(sqlCreate);
        MMORPG.debugLog("MYSQL players_balance table created!");
    }
}