package at.feddis08.mmorpg.io.text_files.files.file_objects;

import at.feddis08.mmorpg.MMORPG;
import at.feddis08.mmorpg.commands.Rank;
import at.feddis08.mmorpg.io.database.Functions;
import at.feddis08.mmorpg.io.database.objects.PlayerObject;
import at.feddis08.mmorpg.io.database.objects.Player_questObject;
import at.feddis08.mmorpg.io.database.objects.RankObject;
import at.feddis08.mmorpg.logic.game.Var;
import at.feddis08.mmorpg.logic.scripts.VarObject;
import at.feddis08.mmorpg.minecraft.tools.Methods;
import at.feddis08.mmorpg.minecraft.tools.classes.Book;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.meta.BookMeta;
import org.checkerframework.checker.units.qual.A;

import javax.swing.text.html.parser.Entity;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

public class ScriptFileObject extends Thread {

    public ArrayList<ArrayList<String>> script = new ArrayList<>();
    public String name = "";
    public String start_event = "SERVER_START";
    public ArrayList<VarObject> varObjects = new ArrayList<>();
    public Thread t = new Thread("script");
    public Boolean error = false;
    public Boolean show_enabling_message = true;
    public String author = "";
    public void run(){
        MMORPG.consoleLog("dd");
        start();
    }

    public void register_new_var(String name, String type){
        Integer index = 0;
        Boolean result = false;
        while (index < varObjects.size()){
            if (Objects.equals(varObjects.get(index).name, name)){
                result = true;
            }
            index = index + 1;
        }
        if (result){
            error = true;
            MMORPG.consoleLog("[" + this.name + "]: ERROR: Var " + name + " is already defined!");
        }else{
            varObjects.add(new VarObject(name, type, ""));
        }
    }
    public VarObject get_var_by_name(String name){
        Integer index = 0;
        VarObject result = null;
        while (index < varObjects.size()){
            if (Objects.equals(varObjects.get(index).name, name)){
                result = varObjects.get(index);
            }
            index = index + 1;
        }
        if (result == null){
            error = true;
            MMORPG.consoleLog("[" + this.name + "]: ERROR: Var " + name + " is not defined!");
        }
        return result;
    }
    public ArrayList<VarObject> get_value(String arg){
        ArrayList<VarObject> result = new ArrayList<>();
        if (arg.contains("<@v>")){
            result.add(get_var_by_name(arg.split("<@v>")[1]));
        }
        if (arg.contains("<@s>")) result.add(new VarObject("", "STRING", arg.split("<@s>")[1]));
        if (result.size() == 0){
        }
        return result;
    }
    public Boolean change_value_of_var(String name, String value){
        Integer index = 0;
        Boolean result = false;
        while (index < varObjects.size()){
            if (Objects.equals(varObjects.get(index).name, name)){
                result = true;
                varObjects.get(index).value = value;
            }
            index = index + 1;
        }
        if (!(result)){
            error = true;
            MMORPG.consoleLog("[" + this.name + "]: ERROR: Var " + name + " is not defined!");
        }
        return result;
    }
    public void start(){
        MMORPG.consoleLog("Enabling script: " + name);
        Integer index = 0;
        if (!error) {
            while (index < script.size()) {
                ArrayList<String> cmd = script.get(index);
                if (error) {
                    MMORPG.consoleLog("Script " + name + " stopped! Error at line: " + index);
                    varObjects.clear();
                    index = script.size() - 1;
                    break;
                }
                if (Objects.equals(script.get(index).get(0), "<*v>")) {
                    register_new_var(cmd.get(1), cmd.get(2));
                }
                if (Objects.equals(script.get(index).get(0), "<=v>")) {
                    change_value_of_var(cmd.get(1), get_value(cmd.get(2)).get(0).value);
                }
                if (Objects.equals(script.get(index).get(0), "<=v,f>")) {
                    ArrayList<String> args = new ArrayList<>();
                    Integer count_c = 0;
                    Integer index2 = 0;
                    while (index2 < cmd.size()) {
                        if (cmd.get(index2).contains(":")) {
                            index2 = cmd.size();
                        } else {
                            if (cmd.get(index2).contains("<@v>")) {
                                count_c = count_c + 1;
                            }
                        }
                        index2 = index2 + 1;
                    }
                    index2 = count_c + 1;
                    while (index2 < cmd.size()) {
                        args.add(cmd.get((index2)));
                        index2 = index2 + 1;
                    }
                    ArrayList<VarObject> result = new ArrayList<>();
                    try {
                        result = execute_functions(args);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    index2 = 0;
                    if (result.size() == 0) {
                        error = true;
                        MMORPG.consoleLog("ERROR: function " + args.get(0) + " didn't returned any values!");
                        MMORPG.consoleLog(result.size() + " " + args.size() + " " + count_c);
                    } else {
                        while (index2 < count_c) {
                            if (cmd.get(index2 + 1).contains("<@v>")) {
                                change_value_of_var(cmd.get(index2 + 1).split("<@v>")[1], result.get(index2).value);
                            }
                            index2 = index2 + 1;
                        }
                    }
                    index2 = 0;

                }
                if (Objects.equals(script.get(index).get(0), "<@v>")) {
                    get_var_by_name(cmd.get(1));
                }
                if (Objects.equals(script.get(index).get(0), "<?->")) {
                    Boolean pass = false;
                    if (Objects.equals(cmd.get(2), "<_==_>")) {
                        if (Objects.equals(get_value(cmd.get(3)).get(0).type, "STRING")) {
                            if (Objects.equals(get_value(cmd.get(3)).get(0).value, get_value(cmd.get(4)).get(0).value))
                                pass = true;
                        } else {
                            if (Integer.parseInt(get_value(cmd.get(3)).get(0).value) == Integer.parseInt(get_value(cmd.get(4)).get(0).value))
                                pass = true;
                        }
                    }
                    if (Objects.equals(cmd.get(2), "<_<_>")) {
                        if (Integer.parseInt(get_value(cmd.get(3)).get(0).value) < Integer.parseInt(get_value(cmd.get(4)).get(0).value))
                            pass = true;
                    }
                    if (Objects.equals(cmd.get(2), "<_>_>")) {
                        if (Integer.parseInt(get_value(cmd.get(3)).get(0).value) > Integer.parseInt(get_value(cmd.get(4)).get(0).value))
                            pass = true;
                    }
                    if (Objects.equals(cmd.get(2), "<_!=_>")) {
                        if (Objects.equals(get_value(cmd.get(3)).get(0).type, "STRING")) {
                            if (!(Objects.equals(get_value(cmd.get(3)).get(0).value, get_value(cmd.get(4)).get(0).value)))
                                pass = true;
                        } else {
                            if (!(Integer.parseInt(get_value(cmd.get(3)).get(0).value) == Integer.parseInt(get_value(cmd.get(4)).get(0).value)))
                                pass = true;
                        }
                    }
                    if (!(pass)) {
                        Integer index2 = index;
                        while (index2 < script.size()) {
                            ArrayList<String> cmd2 = script.get(index2);
                            if (Objects.equals(script.get(index2).get(0), "<-?>") && Objects.equals(script.get(index).get(1), script.get(index2).get(1))) {
                                index = index2;
                            }
                            index2 = index2 + 1;
                        }
                    }
                }
                try {
                    execute_functions(cmd);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                index = index + 1;
            }
        }
        varObjects.clear();
        t.stop();
    }
    public ArrayList<VarObject> execute_functions(ArrayList<String> args) throws SQLException {
        ArrayList<VarObject> result = new ArrayList<>();
        if (Objects.equals(args.get(0), "rank.create_rank:")){
            Rank.create_rank(get_value(args.get(1)).get(0).value);
        }
        if (Objects.equals(args.get(0), "rank.add_rule:")){
            Rank.add_rule(get_value(args.get(1)).get(0).value, get_value(args.get(2)).get(0).value);
        }
        if (Objects.equals(args.get(0), "rank.set_parent:")){
            Rank.set_parent(get_value(args.get(1)).get(0).value, get_value(args.get(2)).get(0).value);
        }
        if (Objects.equals(args.get(0), "rank.set_rank_color:")){
            Rank.set_rank_color(get_value(args.get(1)).get(0).value, get_value(args.get(2)).get(0).value);
        }
        if (Objects.equals(args.get(0), "rank.set_prefix:")){
            Rank.set_prefix(get_value(args.get(1)).get(0).value, get_value(args.get(2)).get(0).value);
        }
        if (Objects.equals(args.get(0), "rank.set_prefix_color:")){
            Rank.set_prefix_color(get_value(args.get(1)).get(0).value, get_value(args.get(2)).get(0).value);
        }
        if (Objects.equals(args.get(0), "rank.add_rule:")){
            RankObject dbRank = Rank.get_rank_from_player(get_value(args.get(1)).get(0).value);
            result.add(new VarObject("", "STRING", dbRank.id));
            result.add(new VarObject("", "STRING", dbRank.name));
            result.add(new VarObject("", "STRING", dbRank.parent));
            result.add(new VarObject("", "INTEGER", String.valueOf(dbRank.rank_level)));
            result.add(new VarObject("", "STRING", dbRank.rank_color));
            result.add(new VarObject("", "STRING", dbRank.prefix));
            result.add(new VarObject("", "STRING", dbRank.prefix_color));
        }
        if (Objects.equals(args.get(0), "rank.remove_rule:")){
            Rank.remove_rule(get_value(args.get(1)).get(0).value, get_value(args.get(2)).get(0).value);
        }
        if (Objects.equals(args.get(0), "rank.has_permission:")){
            Boolean permission = Rank.has_permission_from_rank(get_value(args.get(1)).get(0).value, get_value(args.get(2)).get(0).value);
            if (permission){
                result.add(new VarObject("", "INTEGER", String.valueOf(1)));
            }else{
                result.add(new VarObject("", "INTEGER", String.valueOf(0)));
            }
        }
        if (Objects.equals(args.get(0), "rank.set_player_rank:")){
            Rank.set_player_rank_from(get_value(args.get(1)).get(0).value, get_value(args.get(2)).get(0).value);
        }
        if (Objects.equals(args.get(0), "consoleLog:")){
            String str = "[" + name + "]: " + get_value(args.get(1)).get(0).value;
            MMORPG.consoleLog(str);
        }
        if (Objects.equals(args.get(0), "debugLog:")){
            String str = "[" + name + "]: " + get_value(args.get(1)).get(0).value;
            MMORPG.debugLog(str);
        }
        if (Objects.equals(args.get(0), "open_inv_on_minecraft_player:")){
            Methods.open_inv_on_minecraft_player(get_value(args.get(1)).get(0).value, get_value(args.get(2)).get(0).value);
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
        if (Objects.equals(args.get(0), "minecraft.send_message_to_player:")){
            Methods.send_minecraft_message_by_id(get_value(args.get(1)).get(0).value, get_value(args.get(2)).get(0).value);
        }
        if (Objects.equals(args.get(0), "minecraft.book.open:")){
            MMORPG.Server.getPlayer(UUID.fromString(get_value(args.get(1)).get(0).value)).openBook(Var.get_book_by_display_name(get_value(args.get(2)).get(0).value).book);
        }
        if (Objects.equals(args.get(0), "minecraft.book.create:")){
            Var.books.add(new Book(get_value(args.get(1)).get(0).value, get_value(args.get(2)).get(0).value, get_value(args.get(2)).get(0).value));
        }
        if (Objects.equals(args.get(0), "minecraft.book.add_page:")){
            BookMeta itemMeta = (BookMeta) Var.get_book_by_display_name(get_value(args.get(1)).get(0).value).book.getItemMeta();
            itemMeta.addPage(get_value(args.get(2)).get(0).value);
            Var.get_book_by_display_name(get_value(args.get(1)).get(0).value).book.setItemMeta(itemMeta);
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
        return result;
    }


    public void parse_config_file(ArrayList<String> lines) {
        MMORPG.debugLog("Parsing Scripts file ...");
        Integer index = 0;
        Boolean parse_ok = false;
        while ((index + 1) <= lines.size()) {
            parse_ok = false;
            String line = lines.get(index);
            String[] params = line.split("__");
            if (Objects.equals(params[0], "#")) {
                parse_ok = true;
            }
            if (Objects.equals(params[0], "")) {
                parse_ok = true;
            }
            if (Objects.equals(params[0], "<AUTHOR>")) {
                if (params.length == 2){
                    author = params[1];
                    parse_ok = true;
                }else{
                    parse_ok = true;
                    MMORPG.consoleLog("ERROR: Could not parse config file. Missing argument. Load default value. Error at line: " + String.valueOf(index + 1));
                }
            }
            if (Objects.equals(params[0], "<SHOW_ENABLING_MESSAGE>")) {
                if (params.length == 1){
                    show_enabling_message = true;
                    parse_ok = true;
                }else{
                    parse_ok = true;
                    MMORPG.consoleLog("ERROR: Could not parse config file. Missing argument. Load default value. Error at line: " + String.valueOf(index + 1));
                }
            }
            if (Objects.equals(params[0], "<SCRIPT_NAME>")) {
                if (params.length == 2){
                    name = params[1];
                    parse_ok = true;
                }else{
                    parse_ok = true;
                    MMORPG.consoleLog("ERROR: Could not parse config file. Missing argument. Load default value. Error at line: " + String.valueOf(index + 1));
                }
            }
            if (Objects.equals(params[0], "<START_EVENT>")) {
                if (params.length == 2){
                    start_event = params[1];
                    parse_ok = true;
                }else{
                    parse_ok = true;
                    MMORPG.consoleLog("ERROR: Could not parse config file. Missing argument. Load default value. Error at line: " + String.valueOf(index + 1));
                }
            }
            if (Objects.equals(params[0], "<$>")) {
                ArrayList<String> command = new ArrayList<>();
                Integer index2 = 1;
                while (index2 < params.length){
                    command.add(params[index2]);
                    index2 = index2 + 1;
                }
                script.add(command);
                parse_ok = true;
            }
            index = index + 1;
            if (!(parse_ok))
                MMORPG.consoleLog("ERROR: Could not parse config file. Load default value. Error at line: " + String.valueOf(index));

        }
        MMORPG.consoleLog("[" + name +"]: By " + author + ", parsed and loaded!");
    }
}
