package at.feddis08.tools;

import at.feddis08.Boot;
import at.feddis08.tools.io.database.Functions;
import at.feddis08.tools.io.database.objects.PlayerObject;
import at.feddis08.tools.io.database.objects.RankObject;
import at.feddis08.tools.io.database.objects.Rank_permissionObject;

import java.sql.SQLException;
import java.util.Objects;

public class Rank_api {
    public static RankObject get_rank_from_player(String player_id) throws SQLException {
        PlayerObject dbPlayer = Functions.getPlayer("id", player_id);
        RankObject dbRank = Functions.getRank("id", dbPlayer.player_rank);
        return dbRank;
    }

    public static boolean has_permission_from_rank(String rank_name, String permission) throws SQLException {
        Rank_permissionObject dbRank_permission = Functions.getRanksPermissionsWhereAnd("id", rank_name, "permission", permission);
        if (Objects.equals(dbRank_permission.permission, permission)){return true;}else{return false;}
    }

    public static boolean isPlayer_allowedTo(String player_id, String permission) throws SQLException {
        RankObject dbRank = get_rank_from_player(player_id);
        boolean result = false;
        if (has_permission_from_rank(dbRank.name, permission)){
            result = true;
        }else{
            if(has_permission_from_rank(dbRank.parent, permission)){
                result = true;
            }else{
                result = false;
            }
        }
        if (!result){
            if (has_permission_from_rank(dbRank.name, "*")){
                result = true;
            }else{
                if(has_permission_from_rank(dbRank.parent, "*")){
                    result = true;
                }else{
                    result = false;
                }
            }

        }
        return result;
    }

    public static boolean create_rank(String rank_name){
        RankObject dbRank = null;
        Boolean result = false;
        try {
            dbRank = Functions.getRank("name", rank_name);
            if(Objects.equals(dbRank.name, null)){
                dbRank.id = rank_name;
                dbRank.name = rank_name;
                Functions.createRank(dbRank);
                result = true;
            }else{
                result = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean set_prefix(String rank_name, String prefix){
        RankObject dbRank = null;
        boolean result = true;
        try {
            dbRank = Functions.getRank("name", rank_name);
            if (Objects.equals(dbRank.name, null)) {
                result = false;
            }
            if(result){
                Functions.update("ranks", "prefix", prefix, rank_name, "name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean set_parent(String rank_name, String parent_name){
        RankObject dbRank = null;
        boolean result = true;
        try {
            dbRank = Functions.getRank("name", rank_name);
            if (Objects.equals(dbRank.name, null)) {
                result = false;
            }
            if(result){
                Functions.update("ranks", "parent", parent_name, rank_name, "name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean set_prefix_color(String rank_name, String prefix_color){
        RankObject dbRank = null;
        boolean result = true;
        try {
            dbRank = Functions.getRank("name", rank_name);
            if (Objects.equals(dbRank.name, null)) {
                result = false;
            }
            if(result){
                Functions.update("ranks", "prefix_color", prefix_color, rank_name, "name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean add_rule(String rank_name, String permission){
        RankObject dbRank = null;
        Rank_permissionObject dbRank_permissions = null;
        boolean result = true;
        try {
            dbRank = Functions.getRank("name", rank_name);
            dbRank_permissions = Functions.getRanksPermissionsWhereAnd("permission", permission, "id", dbRank.id);
            if (Objects.equals(dbRank.name, null)) {
                result = false;
            }
            if (Objects.equals(dbRank_permissions.permission, permission)){
                result = false;
            }
            if(result){
                dbRank_permissions.id = rank_name;
                dbRank_permissions.permission = permission;
                Functions.createRank_permision(dbRank_permissions);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean set_rank_color(String rank_name, String rank_color){
        RankObject dbRank = null;
        boolean result = true;
        try {
            dbRank = Functions.getRank("name", rank_name);
            if (Objects.equals(dbRank.name, null)) {
                result = false;
            }
            if(result){
                Functions.update("ranks", "rank_color", rank_color, rank_name, "name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean set_player_rank_from(String rank_name, String player_id) {
        RankObject dbRank = null;
        PlayerObject dbPlayer2 = null;
        boolean result = true;
        try {
            dbRank = Functions.getRank("name", rank_name);
            dbPlayer2 = Functions.getPlayer("id", player_id);
            if (Objects.equals(dbRank.name, null)) {
                result = false;
            } else {
                if (Objects.equals(dbPlayer2.id, null)) {
                    result = false;
                }
            }
            if(result){
                Functions.update("players", "player_rank", rank_name, dbPlayer2.id, "id");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return result;
    }

    public static boolean remove_rule(String permission, String id){
        RankObject dbRank = null;
        Rank_permissionObject dbRank_permission = null;
        boolean result = true;
        try {
            dbRank = Functions.getRank("name", id);
            dbRank_permission = Functions.getRanksPermissionsWhereAnd("permission", permission, "id", id);
            if (Objects.equals(dbRank.name, null)) {
                result = false;
            }
            if (!(Objects.equals(dbRank_permission.permission, permission))) {
                result = false;
            }
            Boot.consoleLog(dbRank.name + " " + dbRank_permission.permission + " " + result);
            if(result){
                Functions.deleteWhereAnd("ranks_permissions", "permission", permission, "id", id);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
