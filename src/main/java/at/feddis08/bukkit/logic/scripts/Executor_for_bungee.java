package at.feddis08.bukkit.logic.scripts;

import at.feddis08.Boot;
import at.feddis08.bungeecord.BUNGEE;
import at.feddis08.bungeecord.Methods;
import at.feddis08.tools.Rank_api;
import at.feddis08.tools.io.database.Functions;
import at.feddis08.tools.io.database.objects.PlayerObject;
import at.feddis08.tools.io.database.objects.Player_questObject;
import at.feddis08.tools.io.database.objects.RankObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

import static at.feddis08.tools.io.text_files.files.file_objects.ScriptFileObject.*;

public class Executor_for_bungee {
    public static ArrayList<VarObject> execute_functions(ArrayList<String> args, Integer index) throws SQLException, SQLException {
        ArrayList<VarObject> result = new ArrayList<>();
        if (Objects.equals(args.get(0), "bungee.teleport_player_to_server:")){
            PlayerObject dbPlayer = Functions.getPlayer("id", get_value(args.get(1)).get(0).value);
            if (dbPlayer != null){
                if (Methods.sendPlayerToServer(dbPlayer, get_value(args.get(2)).get(0).value)){
                    result.add(new VarObject("", "STRING", "TRUE")) ;
                }else{
                    result.add(new VarObject("", "STRING", "FALSE")) ;
                }
            }else{
                result.add(new VarObject("", "STRING", "FALSE")) ;
            }
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
        return result;
    }
}
