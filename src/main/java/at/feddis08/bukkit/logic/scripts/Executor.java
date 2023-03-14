package at.feddis08.bukkit.logic.scripts;

import at.feddis08.Boot;
import at.feddis08.tools.Rank_api;
import at.feddis08.tools.io.database.Functions;
import at.feddis08.tools.io.database.objects.PlayerObject;
import at.feddis08.tools.io.database.objects.Player_questObject;
import at.feddis08.tools.io.database.objects.RankObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

import static at.feddis08.tools.io.text_files.files.file_objects.ScriptFileObject.*;

public class Executor {
    public static ArrayList<VarObject> execute_functions(ArrayList<String> args, Integer index) throws SQLException, SQLException {
        ArrayList<VarObject> result = new ArrayList<>();
        if (Objects.equals(args.get(0), "rank.create_rank:")){
            Rank_api.create_rank(get_value(args.get(1)).get(0).value);
        }
        if (Objects.equals(args.get(0), "rank.add_rule:")){
            Rank_api.add_rule(get_value(args.get(1)).get(0).value, get_value(args.get(2)).get(0).value);
        }
        if (Objects.equals(args.get(0), "rank.set_parent:")){
            Rank_api.set_parent(get_value(args.get(1)).get(0).value, get_value(args.get(2)).get(0).value);
        }
        if (Objects.equals(args.get(0), "rank.set_rank_color:")){
            Rank_api.set_rank_color(get_value(args.get(1)).get(0).value, get_value(args.get(2)).get(0).value);
        }
        if (Objects.equals(args.get(0), "rank.set_prefix:")){
            Rank_api.set_prefix(get_value(args.get(1)).get(0).value, get_value(args.get(2)).get(0).value);
        }
        if (Objects.equals(args.get(0), "rank.set_prefix_color:")){
            Rank_api.set_prefix_color(get_value(args.get(1)).get(0).value, get_value(args.get(2)).get(0).value);
        }
        if (Objects.equals(args.get(0), "rank.get:")){
            RankObject dbRank = Rank_api.get_rank_from_player(get_value(args.get(1)).get(0).value);
            result.add(new VarObject("", "STRING", dbRank.id));
            result.add(new VarObject("", "STRING", dbRank.name));
            result.add(new VarObject("", "STRING", dbRank.parent));
            result.add(new VarObject("", "INTEGER", String.valueOf(dbRank.rank_level)));
            result.add(new VarObject("", "STRING", dbRank.rank_color));
            result.add(new VarObject("", "STRING", dbRank.prefix));
            result.add(new VarObject("", "STRING", dbRank.prefix_color));
        }
        if (Objects.equals(args.get(0), "rank.remove_rule:")){
            Rank_api.remove_rule(get_value(args.get(1)).get(0).value, get_value(args.get(2)).get(0).value);
        }
        if (Objects.equals(args.get(0), "rank.has_permission:")){
            Boolean permission = Rank_api.has_permission_from_rank(get_value(args.get(1)).get(0).value, get_value(args.get(2)).get(0).value);
            if (permission){
                result.add(new VarObject("", "INTEGER", String.valueOf(1)));
            }else{
                result.add(new VarObject("", "INTEGER", String.valueOf(0)));
            }
        }
        if (Objects.equals(args.get(0), "rank.set_player_rank:")){
            Rank_api.set_player_rank_from(get_value(args.get(1)).get(0).value, get_value(args.get(2)).get(0).value);
        }
        if (Objects.equals(args.get(0), "consoleLog:")){
            String str = "[" + name + "]: " + get_value(args.get(1)).get(0).value;
            Boot.consoleLog(str);
        }
        if (Objects.equals(args.get(0), "debugLog:")){
            String str = "[" + name + "]: " + get_value(args.get(1)).get(0).value;
            Boot.debugLog(str);
        }
        if (Objects.equals(args.get(0), "string.join:")){
            result.add(new VarObject("", "STRING", get_value(args.get(1)).get(0).value + get_value(args.get(2)).get(0).value ));
        }
        if (Objects.equals(args.get(0), "integer.+:")){
            result.add(new VarObject("", "INTEGER", String.valueOf(Integer.parseInt(get_value(args.get(1)).get(0).value) + Integer.parseInt(get_value(args.get(2)).get(0).value ))));
        }
        if (Objects.equals(args.get(0), "integer.-:")){
            result.add(new VarObject("", "INTEGER", String.valueOf(Integer.parseInt(get_value(args.get(1)).get(0).value) - Integer.parseInt(get_value(args.get(2)).get(0).value ))));
        }
        if (Objects.equals(args.get(0), "integer.*:")){
            result.add(new VarObject("", "INTEGER", String.valueOf(Integer.parseInt(get_value(args.get(1)).get(0).value) * Integer.parseInt(get_value(args.get(2)).get(0).value ))));
        }
        if (Objects.equals(args.get(0), "integer./:")){
            result.add(new VarObject("", "INTEGER", String.valueOf(Integer.parseInt(get_value(args.get(1)).get(0).value) / Integer.parseInt(get_value(args.get(2)).get(0).value ))));
        }
        if (Objects.equals(args.get(0), "ping:")){
            result.add(new VarObject("", "STRING", "pong!"));
        }
        if (Objects.equals(args.get(0), "get_player_by_id:")){
            PlayerObject dbPlayer = Functions.getPlayer("id", get_value(args.get(1)).get(0).value);
            result.add(new VarObject("", "STRING", String.valueOf(dbPlayer.id.toString())));
            result.add(new VarObject("", "INTEGER", String.valueOf(dbPlayer.online)));
            result.add(new VarObject("", "STRING", dbPlayer.didStartup));
            result.add(new VarObject("", "STRING", dbPlayer.display_name));
            result.add(new VarObject("", "STRING", dbPlayer.player_name));
            result.add(new VarObject("", "STRING", dbPlayer.player_rank));
        }
        if (Objects.equals(args.get(0), "get_quest:")){
            ArrayList<Player_questObject> player_questObjects = Functions.getPlayerQuest (get_value(args.get(1)).get(0).value, get_value(args.get(2)).get(0).value, get_value(args.get(3)).get(0).value);
            if (player_questObjects.size() > 0){
                Player_questObject player_questObject = player_questObjects.get(0);
                result.add(new VarObject("", "STRING", String.valueOf(player_questObject.id)));
                result.add(new VarObject("", "STRING", String.valueOf(player_questObject.quest_name)));
                result.add(new VarObject("", "INTEGER", String.valueOf(player_questObject.progress)));
            }else{
                result.add(new VarObject("", "STRING", String.valueOf("null")));
                result.add(new VarObject("", "STRING", String.valueOf("null")));
                result.add(new VarObject("", "INTEGER", String.valueOf(0)));
            }
        }
        if (Objects.equals(args.get(0), "create_quest:")){
            Player_questObject player_questObject = new Player_questObject();
            player_questObject.id = get_value(args.get(1)).get(0).value;
            player_questObject.quest_name = get_value(args.get(2)).get(0).value;
            player_questObject.progress = Integer.parseInt(get_value(args.get(3)).get(0).value);
            Functions.createPlayer_quest(player_questObject);
        }
        if (Objects.equals(args.get(0), "update_quest:")){
            Functions.update("players_quests", get_value(args.get(1)).get(0).value, get_value(args.get(2)).get(0).value,get_value(args.get(3)).get(0).value, get_value(args.get(4)).get(0).value);
        }
        if (Objects.equals(args.get(0), "var_pool.create:")){
            if (at.feddis08.bukkit.logic.scripts.Var.get_var_pool_by_name(get_value(args.get(1)).get(0).value) == null){
                Var_pool var_pool = new Var_pool();
                var_pool.name = get_value(args.get(1)).get(0).value;
                at.feddis08.bukkit.logic.scripts.Var.var_pools.add(var_pool);
            }else{
                throw_error("ERROR: Var_pool " + get_value(args.get(1)).get(0).value +" already defined!", index);
            }
        }
        if (Objects.equals(args.get(0), "var_pool.get:")){
            if (at.feddis08.bukkit.logic.scripts.Var.get_var_pool_by_name(get_value(args.get(1)).get(0).value) == null){
                throw_error("ERROR: Var_pool " + get_value(args.get(1)).get(0).value +" is not defined!", current_line);
            }else{
                Var_pool var_pool = at.feddis08.bukkit.logic.scripts.Var.get_var_pool_by_name(get_value(args.get(1)).get(0).value);
                result.add(var_pool.get_var_by_name(get_value(args.get(2)).get(0).value));
            }
        }
        if (Objects.equals(args.get(0), "var_pool.put:")){
            if (at.feddis08.bukkit.logic.scripts.Var.get_var_pool_by_name(get_value(args.get(1)).get(0).value) == null){
                throw_error("ERROR: Var_pool " + get_value(args.get(1)).get(0).value +" is not defined!", current_line);
            }else{
                Var_pool var_pool = at.feddis08.bukkit.logic.scripts.Var.get_var_pool_by_name(get_value(args.get(1)).get(0).value);
                var_pool.varObjects.add(get_value(args.get(2)).get(0));
            }
        }
        if (Objects.equals(args.get(0), "var_pool.change:")){
            if (at.feddis08.bukkit.logic.scripts.Var.get_var_pool_by_name(get_value(args.get(1)).get(0).value) == null){
                throw_error("ERROR: Var_pool " + get_value(args.get(1)).get(0).value +" is not defined!", current_line);
            }else{
                Var_pool var_pool = at.feddis08.bukkit.logic.scripts.Var.get_var_pool_by_name(get_value(args.get(1)).get(0).value);
                var_pool.change_value_of_var(get_value(args.get(2)).get(0).value, get_value(args.get(3)).get(0).value);
            }
        }
        return result;
    }
}
