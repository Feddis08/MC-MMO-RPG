package at.feddis08.mmorpg.database;
import at.feddis08.mmorpg.MMORPG;
import at.feddis08.mmorpg.database.objects.*;
import org.bukkit.World;

import java.sql.*;
import java.util.ArrayList;

public class Functions {
    public static void createPlayer(PlayerObject playerObj) throws SQLException {
        Statement stmt = JDBC.myConn.createStatement();
        String sqlString = "'"
                + playerObj.realm
                + "', '"
                + playerObj.stage
                + "', '"
                + playerObj.current_world_id
                + "', '"
                + playerObj.gamemode
                + "', '"
                + playerObj.id
                + "', '"
                + playerObj.player_rank
                + "', '"
                + playerObj.player_name
                + "', '"
                + playerObj.display_name
                + "', '"
                + playerObj.online
                + "', '"
                + playerObj.job
                + "', '"
                + playerObj.didStartup
                + "'";
        String sql = "insert into players "
                + "(realm, stage, current_world_id, gamemode, id, player_rank, player_name, display_name, online, job, didStartup)"
                + "values (" + sqlString + ")";
        stmt.executeUpdate(sql);
        MMORPG.debugLog("Player created: " + playerObj.player_name + "!");
    }
    public static void createRank(RankObject dataObj) throws SQLException {
        Statement stmt = JDBC.myConn.createStatement();
        String sqlString = "'"
                + dataObj.name
                + "', '"
                + dataObj.rank_level
                + "', '"
                + dataObj.rank_color
                + "', '"
                + dataObj.id
                + "', '"
                + dataObj.parent
                + "', '"
                + dataObj.prefix
                + "', '"
                + dataObj.prefix_color
                + "'";
        String sql = "insert into ranks "
                + "(name, rank_level, rank_color, id, parent, prefix, prefix_color)"
                + "values (" + sqlString + ")";
        stmt.executeUpdate(sql);
        MMORPG.debugLog("Rank created: " + dataObj.name + "!");
    }
    public static void createData(DataObject dataObj) throws SQLException {
        Statement stmt = JDBC.myConn.createStatement();
        String sqlString = "'"
                + dataObj.name
                + "', '"
                + dataObj.owner_name
                + "', '"
                + dataObj.ranks
                + "', '"
                + dataObj.enabled
                + "', '"
                + dataObj.registerdPlayers
                + "', '"
                + dataObj.online
                + "'";
        String sql = "insert into data "
                + "(name, owner_name, ranks, enabled, registerdPlayers, onlinePlayers)"
                + "values (" + sqlString + ")";
        stmt.executeUpdate(sql);
        MMORPG.debugLog("DataTabel created: " + dataObj.name + "!");
    }
    public static void createWorld(WorldObject dataObj) throws SQLException {
        Statement stmt = JDBC.myConn.createStatement();
        String sqlString = "'"
                + dataObj.name
                + "', '"
                + dataObj.id
                + "', '"
                + dataObj.autoload
                + "', '"
                + dataObj.loaded
                + "', '"
                + dataObj.type
                + "', '"
                + dataObj.players_on
                + "'";
        String sql = "insert into worlds "
                + "(name, id, autoload, loaded, type, players_on)"
                + "values (" + sqlString + ")";
        stmt.executeUpdate(sql);
        MMORPG.debugLog("DataTabel created: " + dataObj.name + "!");
    }
    public static void createPlayerInWorlds(PlayerInWorlds dataObj) throws SQLException {
        Statement stmt = JDBC.myConn.createStatement();
        String sqlString = "'"
                + dataObj.world_id
                + "', '"
                + dataObj.id
                + "', '"
                + dataObj.x
                + "', '"
                + dataObj.y
                + "', '"
                + dataObj.z
                + "'";
        String sql = "insert into players_in_worlds "
                + "(world_id, id, x, y, z)"
                + "values (" + sqlString + ")";
        stmt.executeUpdate(sql);
        MMORPG.debugLog("DataTabel created: " + dataObj.world_id + "!");
    }
    public static void createRank_permision(Rank_permissionObject dataObj) throws SQLException {
        Statement stmt = JDBC.myConn.createStatement();
        String sqlString = "'"
                + dataObj.permission
                + "', '"
                + dataObj.id
                + "'";
        String sql = "insert into ranks_permissions "
                + "(permission, id)"
                + "values (" + sqlString + ")";
        stmt.executeUpdate(sql);
        MMORPG.debugLog("DataTabel created: " + dataObj.permission + "!");
    }
    public static void createBlock_break_count(Block_break_countObject dataObj) throws SQLException {
        Statement stmt = JDBC.myConn.createStatement();
        String sqlString = "'"
                + dataObj.block_name
                + "', '"
                + dataObj.player_id
                + "', '"
                + dataObj.count
                + "'";
        String sql = "insert into block_break_counter "
                + "(block_name, player_id, count)"
                + "values (" + sqlString + ")";
        stmt.executeUpdate(sql);
        MMORPG.debugLog("DataTabel created: " + dataObj.block_name + "!");
    }
    public static void createPlayer_stat(Player_statObject dataObj) throws SQLException {
        Statement stmt = JDBC.myConn.createStatement();
        String sqlString = "'"
                + dataObj.player_id
                + "', '"
                + dataObj.agil
                + "'"
                + dataObj.strength
                + "'"
                + dataObj.soul
                + "'"
                + dataObj.woodcut_experience
                + "'"
                + dataObj.general_experience
                + "'"
                + dataObj.endu
                + "'";
        String sql = "insert into players_stats "
                + "(player_id, agil, strength, soul, woodcut_experience, general_experience, endu)"
                + "values (" + sqlString + ")";
        stmt.executeUpdate(sql);
        MMORPG.debugLog("DataTabel created: " + dataObj.player_id + "!");
    }
    public static void createMail(MailObject dataObj) throws SQLException {
        Statement stmt = JDBC.myConn.createStatement();
        String sqlString = "'"
                + dataObj.sender_id
                + "', '"
                + dataObj.message
                + "', '"
                + dataObj.receiver_id
                + "', '"
                + dataObj.date
                + "', '"
                + dataObj.title
                + "', '"
                + dataObj.id
                + "', '"
                + dataObj.opened
                + "'";
        String sql = "insert into mails "
                + "(sender_id, message, receiver_id, date, title, id, opened)"
                + "values (" + sqlString + ")";
        stmt.executeUpdate(sql);
        MMORPG.debugLog("DataTabel created: " + dataObj.sender_id + "!");
    }
    public static PlayerObject getPlayer(String column, String value) throws SQLException {
        Statement stmt = JDBC.myConn.createStatement();
        String sql = "select * from players where " + column + " = " + "'" + value + "'";
        ResultSet myRs = stmt.executeQuery(sql);
        PlayerObject dataObj = new PlayerObject();
        while (myRs.next()) {
            dataObj.stage = myRs.getInt("stage");
            dataObj.id = myRs.getString("id");
            dataObj.job = myRs.getString("job");
            dataObj.online = myRs.getString("online");
            dataObj.player_rank = myRs.getString("player_rank");
            dataObj.display_name = myRs.getString("display_name");
            dataObj.player_name = myRs.getString("player_name");
            dataObj.current_world_id = myRs.getString("current_world_id");
            dataObj.gamemode = myRs.getInt("gamemode");
            dataObj.didStartup = myRs.getString("didStartup");
        }
        MMORPG.debugLog("DataBase read in players for " + dataObj.id);
        return dataObj;
    }
    public static RankObject getRank(String column, String value) throws SQLException {
        Statement stmt = JDBC.myConn.createStatement();
        String sql = "select * from ranks where " + column + " = " + "'" + value + "'";
        ResultSet myRs = stmt.executeQuery(sql);

            RankObject dataObj = new RankObject();
            while (myRs.next()) {
                dataObj.name = myRs.getString("name");
                dataObj.prefix = myRs.getString("prefix");
                dataObj.prefix_color = myRs.getString("prefix_color");
                dataObj.rank_color = myRs.getString("rank_color");
                dataObj.id = myRs.getString("id");
                dataObj.parent = myRs.getString("parent");
                dataObj.rank_level = myRs.getInt("rank_level");
        }
        MMORPG.debugLog("Database read in ranks for " + dataObj.name + " !");
        return dataObj;
    }
    public static DataObject getData(String column, String value) throws SQLException {
        Statement stmt = JDBC.myConn.createStatement();
        String sql = "select * from data where " + column + " = " + "'" + value + "'";
        ResultSet myRs = stmt.executeQuery(sql);

        DataObject dataObj = new DataObject();
        while (myRs.next()) {
            dataObj.name = myRs.getString("name");
            dataObj.owner_name = myRs.getString("owner_name");
            dataObj.ranks = myRs.getInt("ranks");
            dataObj.online = myRs.getInt("online");
            dataObj.enabled = myRs.getInt("enabled");
            dataObj.registerdPlayers = myRs.getInt("registerdPlayers");
        }
        MMORPG.debugLog("Database read in data for " + dataObj.name + " !");
        return dataObj;
    }
    public static WorldObject getWorld(String column, String value) throws SQLException {
        Statement stmt = JDBC.myConn.createStatement();
        String sql = "select * from worlds where " + column + " = " + "'" + value + "'";
        ResultSet myRs = stmt.executeQuery(sql);

        WorldObject dataObj = new WorldObject();
        while (myRs.next()) {
            dataObj.name = myRs.getString("name");
            dataObj.type = myRs.getString("type");
            dataObj.id = myRs.getString("id");
            dataObj.autoload = myRs.getString("autoload");
            dataObj.loaded = myRs.getString("loaded");
            dataObj.players_on = myRs.getInt("players_on");
        }
        MMORPG.debugLog("Database read in worlds for " + value + " !");
        return dataObj;
    }
    public static ArrayList<WorldObject> getWorlds() throws SQLException {
        Statement stmt = JDBC.myConn.createStatement();
        String sql = "select * from worlds";
        ResultSet myRs = stmt.executeQuery(sql);

        ArrayList<WorldObject> dataObjList = new ArrayList<>();
        while (myRs.next()) {
            WorldObject dataObj = new WorldObject();
            dataObj.name = myRs.getString("name");
            dataObj.type = myRs.getString("type");
            dataObj.id = myRs.getString("id");
            dataObj.autoload = myRs.getString("autoload");
            dataObj.loaded = myRs.getString("loaded");
            dataObj.players_on = myRs.getInt("players_on");
            dataObjList.add(dataObj);
            MMORPG.debugLog("Database read in worlds for " + dataObj.name + " !");
        }
        return dataObjList;
    }
    public static ArrayList<PlayerInWorlds> getPlayerInWorlds(String column, String value) throws SQLException {
        Statement stmt = JDBC.myConn.createStatement();
        String sql = "select * from players_in_worlds where " + column + " = " + "'" + value + "'";
        ResultSet myRs = stmt.executeQuery(sql);

        ArrayList<PlayerInWorlds> dataObjList = new ArrayList<>();
        while (myRs.next()) {
            PlayerInWorlds dataObj = new PlayerInWorlds();
            dataObj.world_id = myRs.getString("world_id");
            dataObj.id = myRs.getString("id");
            dataObj.x = myRs.getInt("x");
            dataObj.y = myRs.getInt("y");
            dataObj.z = myRs.getInt("z");
            dataObjList.add(dataObj);
            MMORPG.debugLog("Database read in worlds for " + dataObj.world_id + " !");
        }
        return dataObjList;
    }
    public static ArrayList<MailObject> getAllMails() throws SQLException {
        Statement stmt = JDBC.myConn.createStatement();
        String sql = "select * from mails";
        ResultSet myRs = stmt.executeQuery(sql);

        ArrayList<MailObject> dataObjList = new ArrayList<>();
        while (myRs.next()) {
            MailObject dataObj = new MailObject();
            dataObj.sender_id = myRs.getString("sender_id");
            dataObj.date = myRs.getString("date");
            dataObj.receiver_id = myRs.getString("receiver_id");
            dataObj.message = myRs.getString("message");
            dataObj.opened = myRs.getString("opened");
            dataObj.title = myRs.getString("title");
            dataObj.id = myRs.getInt("id");
            dataObjList.add(dataObj);
            MMORPG.debugLog("Database read in mails for " + dataObj.message + " !");
        }
        return dataObjList;
    }
    public static ArrayList<MailObject> getMails(String column, String value, String column2, String value2) throws SQLException {
        Statement stmt = JDBC.myConn.createStatement();
        String sql = "select * from mails where " + column + " = " + "'" + value + "' and " + column2 + "='" + value2 + "'";
        ResultSet myRs = stmt.executeQuery(sql);

        ArrayList<MailObject> dataObjList = new ArrayList<>();
        while (myRs.next()) {
            MailObject dataObj = new MailObject();
            dataObj.sender_id = myRs.getString("sender_id");
            dataObj.date = myRs.getString("date");
            dataObj.receiver_id = myRs.getString("receiver_id");
            dataObj.message = myRs.getString("message");
            dataObj.opened = myRs.getString("opened");
            dataObj.title = myRs.getString("title");
            dataObj.id = myRs.getInt("id");
            dataObjList.add(dataObj);
            MMORPG.debugLog("Database read in mails for " + dataObj.message + " !");
        }
        return dataObjList;
    }
    public static MailObject getMail(String column, String value, String column2, String value2) throws SQLException {
        Statement stmt = JDBC.myConn.createStatement();
        String sql = "select * from mails where " + column + " = " + "'" + value + "' and " + column2 + "='" + value2 + "'";
        ResultSet myRs = stmt.executeQuery(sql);

        MailObject dataObj = new MailObject();
        while (myRs.next()) {
            dataObj.sender_id = myRs.getString("sender_id");
            dataObj.date = myRs.getString("date");
            dataObj.title = myRs.getString("title");
            dataObj.id = myRs.getInt("id");
            dataObj.receiver_id = myRs.getString("receiver_id");
            dataObj.message = myRs.getString("message");
            dataObj.opened = myRs.getString("opened");
            MMORPG.debugLog("Database read in mails for " + dataObj.message + " !");
        }
        return dataObj;
    }
    public static ArrayList<Rank_permissionObject> getRanksPermissions(String column, String value) throws SQLException {
        Statement stmt = JDBC.myConn.createStatement();
        String sql = "select * from ranks_permission where " + column + " = " + "'" + value + "'";
        ResultSet myRs = stmt.executeQuery(sql);

        ArrayList<Rank_permissionObject> dataObjList = new ArrayList<>();
        while (myRs.next()) {
            Rank_permissionObject dataObj = new Rank_permissionObject();
            dataObj.permission = myRs.getString("permission");
            dataObj.id = myRs.getString("id");
            dataObjList.add(dataObj);
            MMORPG.debugLog("Database read in ranks_permissions for " + dataObj.permission + " !");
        }
        return dataObjList;
    }
    public static Rank_permissionObject getRanksPermissionsWhereAnd(String column, String value, String column2, String value2) throws SQLException {
        Statement stmt = JDBC.myConn.createStatement();
        String sql = "select * from ranks_permissions where " + column + "=" + "'" + value + "' and " + column2 + "='" + value2 + "';";
        MMORPG.debugLog(sql);
        ResultSet myRs = stmt.executeQuery(sql);
        Rank_permissionObject dataObj = new Rank_permissionObject();
        while (myRs.next()) {
            dataObj.permission = myRs.getString("permission");
            dataObj.id = myRs.getString("id");
            MMORPG.debugLog("Database read in ranks_permissions for " + dataObj.permission + " !");
        }
        return dataObj;
    }
    public static Block_break_countObject getBlock_break_count(String column, String value, String column2, String value2) throws SQLException {
        Statement stmt = JDBC.myConn.createStatement();
        String sql = "select * from block_break_counter where " + column + "=" + "'" + value + "' and " + column2 + "='" + value2 + "';";
        MMORPG.debugLog(sql);
        ResultSet myRs = stmt.executeQuery(sql);
        Block_break_countObject dataObj = new Block_break_countObject();
        while (myRs.next()) {
            dataObj.block_name = myRs.getString("block_name");
            dataObj.player_id = myRs.getString("player_id");
            dataObj.count = myRs.getInt("count");
            MMORPG.debugLog("Database read in block_break_counter for " + dataObj.block_name + " !");
        }
        return dataObj;
    }
    public static Player_statObject getPlayer_stat (String column, String value) throws SQLException {
        Statement stmt = JDBC.myConn.createStatement();
        String sql = "select * from players_stats where " + column + "=" + "'" + value + "';";
        MMORPG.debugLog(sql);
        ResultSet myRs = stmt.executeQuery(sql);
        Player_statObject dataObj = new Player_statObject();
        while (myRs.next()) {
            dataObj.player_id = myRs.getString("player_id");
            dataObj.agil = myRs.getInt("agil");
            dataObj.endu = myRs.getInt("endu");
            dataObj.general_experience = myRs.getInt("general_experience");
            dataObj.woodcut_experience = myRs.getInt("woodcut_experience");
            dataObj.soul = myRs.getInt("soul");
            dataObj.strength = myRs.getInt("strength");
            MMORPG.debugLog("Database read in player_stats for " + dataObj.player_id + " !");
        }
        return dataObj;
    }
    public static void update(String tabel, String column, String newValue, String identityValue, String identityColumn) throws SQLException {
        Statement stmt = JDBC.myConn.createStatement();
        String sql = "update " + tabel + " set "
                + column
                + " = "
                + " '"
                + newValue
                + "'"
                + "where "
                + identityColumn
                + " ="
                + " '"
                + identityValue
                + "'";
        stmt.executeUpdate(sql);
        MMORPG.debugLog("Database update: " + sql);

    }
    public static void updateWhereAnd(String tabel, String column, String newValue, String identityValue, String identityColumn, String identityValue2, String identityColumn2) throws SQLException {
        Statement stmt = JDBC.myConn.createStatement();
        String sql = "update " + tabel + " set "
                + column
                + " = "
                + " '"
                + newValue
                + "'"
                + "where "
                + identityColumn
                + " ="
                + " '"
                + identityValue
                + "'"
                + " and "
                + identityColumn2
                + " ="
                + " '"
                + identityValue2
                + "'";
        stmt.executeUpdate(sql);
        MMORPG.debugLog("Database update: " + sql);

    }
    public static void delete(String tabel, String column, String value) throws SQLException {
        Statement stmt = JDBC.myConn.createStatement();
        String sql = "delete from " + tabel + " where " + column + "='" + value + "'";
        Integer rowsAffected = stmt.executeUpdate(sql);
        MMORPG.debugLog("Deleted " + rowsAffected + " rows for " + value + "!");

    }
    public static void deleteWhereAnd(String tabel, String column, String value, String column2, String value2) throws SQLException {
        Statement stmt = JDBC.myConn.createStatement();
        String sql = "delete from " + tabel + " where " + column + "='" + value + "' and " + column2 + "='" + value2 + "'";
        Integer rowsAffected = stmt.executeUpdate(sql);
        MMORPG.debugLog("Deleted " + rowsAffected + " rows: " + sql + "!");

    }
    public static void resetDB() throws SQLException {
        Statement stmt = JDBC.myConn.createStatement();
        try {
            String sql = "drop table ranks";
            Integer rowsAffected = stmt.executeUpdate(sql);
            MMORPG.debugLog("Deleted table ranks!");
        }catch (Exception e){}
        try {
            String sql = "drop table players";
            Integer rowsAffected = stmt.executeUpdate(sql);
            MMORPG.debugLog("Deleted table players!");
        }catch (Exception e){}
        try {
            String sql = "drop table data";
            Integer rowsAffected = stmt.executeUpdate(sql);
            MMORPG.debugLog("Deleted table data!");
        }catch (Exception e){}
    }
}

