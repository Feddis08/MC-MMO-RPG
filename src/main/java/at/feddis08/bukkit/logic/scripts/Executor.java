package at.feddis08.bukkit.logic.scripts;
import at.feddis08.Boot;
import at.feddis08.bukkit.cluster_com_client.Start_cluster_client;
import at.feddis08.tools.Rank_api;
import at.feddis08.tools.io.database.Functions;
import at.feddis08.tools.io.database.objects.PlayerObject;
import at.feddis08.tools.io.database.objects.Player_questObject;
import at.feddis08.tools.io.database.objects.RankObject;
import at.feddis08.tools.io.text_files.files.file_objects.ScriptFileObject;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Executor {
    public interface Command {
        ArrayList<VarObject> execute(ArrayList<String> args, ScriptFileObject scriptFileObject)
                throws SQLException, IOException;

        Info getInfo();
    }

    public class Info {
        public String description;
        public List<Parameter> parameters;

        public Info(String description, List<Parameter> parameters) {
            this.description = description;
            this.parameters = parameters;
        }
    }

    public class Parameter {
        public boolean isReturnValue;
        public String name;
        public String typeDescription;

        public Parameter(boolean isReturnValue, String name, String typeDescription) {
            this.isReturnValue = isReturnValue;
            this.name = name;
            this.typeDescription = typeDescription;
        }
    }
    public class ConsoleLogCommand implements Command {
        private final Info info;

        public ConsoleLogCommand() {
            List<Parameter> parameters = new ArrayList<>();
            parameters.add(new Parameter(false, "message", "STRING"));
            this.info = new Info("Logs a message to the console.", parameters);
        }

        @Override
        public ArrayList<VarObject> execute(ArrayList<String> args, ScriptFileObject scriptFileObject) {
            String str = "[" + scriptFileObject.name + "]: " + scriptFileObject.get_value(args.get(1)).get(0).value;
            Boot.consoleLog(str);
            return new ArrayList<>();
        }

        @Override
        public Info getInfo() {
            return this.info;
        }
    }
    public class DebugLogCommand implements Command {
        private final Info info;

        public DebugLogCommand() {
            List<Parameter> parameters = List.of(
                    new Parameter(false, "message", "STRING")
            );
            this.info = new Info("Logs a message to the debug console.", parameters);
        }

        @Override
        public ArrayList<VarObject> execute(ArrayList<String> args, ScriptFileObject scriptFileObject) {
            String str = "[" + scriptFileObject + "]: " + scriptFileObject.get_value(args.get(1)).get(0).value;
            Boot.debugLog(str);
            return new ArrayList<>();
        }

        @Override
        public Info getInfo() {
            return this.info;
        }
    }

    public class StringJoinCommand implements Command {
        private final Info info;

        public StringJoinCommand() {
            List<Parameter> parameters = List.of(
                    new Parameter(false, "string1", "STRING"),
                    new Parameter(false, "string2", "STRING"),
                    new Parameter(true, "resultString", "STRING")
            );
            this.info = new Info("Joins two strings and returns the result.", parameters);
        }

        @Override
        public ArrayList<VarObject> execute(ArrayList<String> args, ScriptFileObject scriptFileObject) {
            ArrayList<VarObject> result = new ArrayList<>();
            result.add(new VarObject("", "STRING", scriptFileObject.get_value(args.get(1)).get(0).value + scriptFileObject.get_value(args.get(2)).get(0).value));
            return result;
        }

        @Override
        public Info getInfo() {
            return this.info;
        }
    }
    public class IntegerMultiplyCommand implements Command {
        private final Info info;

        public IntegerMultiplyCommand() {
            List<Parameter> parameters = List.of(
                    new Parameter(false, "int1", "INTEGER"),
                    new Parameter(false, "int2", "INTEGER"),
                    new Parameter(true, "result", "INTEGER")
            );
            this.info = new Info("Multiplies two integers and returns the result.", parameters);
        }

        @Override
        public ArrayList<VarObject> execute(ArrayList<String> args, ScriptFileObject scriptFileObject) {
            ArrayList<VarObject> result = new ArrayList<>();
            result.add(new VarObject("", "INTEGER", String.valueOf(Integer.parseInt(scriptFileObject.get_value(args.get(1)).get(0).value) * Integer.parseInt(scriptFileObject.get_value(args.get(2)).get(0).value))));
            return result;
        }

        @Override
        public Info getInfo() {
            return this.info;
        }
    }
    public class SetPrefixCommand implements Command {
        private final Info info;

        public SetPrefixCommand() {
            List<Parameter> parameters = List.of(
                    new Parameter(false, "rank_id", "STRING"),
                    new Parameter(false, "prefix", "STRING")
            );
            this.info = new Info("Sets the prefix of a specific rank.", parameters);
        }

        @Override
        public ArrayList<VarObject> execute(ArrayList<String> args, ScriptFileObject scriptFileObject) {
            Rank_api.set_prefix(scriptFileObject.get_value(args.get(1)).get(0).value,
                    scriptFileObject.get_value(args.get(2)).get(0).value);
            return new ArrayList<>();
        }

        @Override
        public Info getInfo() {
            return this.info;
        }
    }

    public class SetPrefixColorCommand implements Command {
        private final Info info;

        public SetPrefixColorCommand() {
            List<Parameter> parameters = List.of(
                    new Parameter(false, "rank_id", "STRING"),
                    new Parameter(false, "color", "STRING")
            );
            this.info = new Info("Sets the color of the prefix for a specific rank.", parameters);
        }

        @Override
        public ArrayList<VarObject> execute(ArrayList<String> args, ScriptFileObject scriptFileObject) {
            Rank_api.set_prefix_color(scriptFileObject.get_value(args.get(1)).get(0).value,
                    scriptFileObject.get_value(args.get(2)).get(0).value);
            return new ArrayList<>();
        }

        @Override
        public Info getInfo() {
            return this.info;
        }
    }
    public class GetRankCommand implements Command {
        private final Info info;

        public GetRankCommand() {
            List<Parameter> parameters = List.of(
                    new Parameter(false, "player_id", "STRING"),
                    new Parameter(true, "id", "STRING"),
                    new Parameter(true, "name", "STRING"),
                    new Parameter(true, "parent", "STRING"),
                    new Parameter(true, "rank_level", "INTEGER"),
                    new Parameter(true, "rank_color", "STRING"),
                    new Parameter(true, "prefix", "STRING"),
                    new Parameter(true, "prefix_color", "STRING")
            );
            this.info = new Info("Retrieves various properties of a rank associated with a player.", parameters);
        }

        @Override
        public ArrayList<VarObject> execute(ArrayList<String> args, ScriptFileObject scriptFileObject) {
            ArrayList<VarObject> result = new ArrayList<>();
            RankObject dbRank = null;
            try {
                dbRank = Rank_api.get_rank_from_player(scriptFileObject.get_value(args.get(1)).get(0).value);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            result.add(new VarObject("", "STRING", dbRank.id));
            result.add(new VarObject("", "STRING", dbRank.name));
            result.add(new VarObject("", "STRING", dbRank.parent));
            result.add(new VarObject("", "INTEGER", String.valueOf(dbRank.rank_level)));
            result.add(new VarObject("", "STRING", dbRank.rank_color));
            result.add(new VarObject("", "STRING", dbRank.prefix));
            result.add(new VarObject("", "STRING", dbRank.prefix_color));
            return result;
        }

        @Override
        public Info getInfo() {
            return this.info;
        }
    }


    public class RemoveRuleCommand implements Command {
        private final Info info;

        public RemoveRuleCommand() {
            List<Parameter> parameters = List.of(
                    new Parameter(false, "rank_id", "STRING"),
                    new Parameter(false, "rule", "STRING")
            );
            this.info = new Info("Removes a rule from a specific rank.", parameters);
        }

        @Override
        public ArrayList<VarObject> execute(ArrayList<String> args, ScriptFileObject scriptFileObject) {
            Rank_api.remove_rule(scriptFileObject.get_value(args.get(1)).get(0).value, scriptFileObject.get_value(args.get(2)).get(0).value);
            return new ArrayList<>();
        }

        @Override
        public Info getInfo() {
            return this.info;
        }
    }

    public class HasPermissionCommand implements Command {
        private final Info info;

        public HasPermissionCommand() {
            List<Parameter> parameters = List.of(
                    new Parameter(false, "rank_id", "STRING"),
                    new Parameter(false, "permission", "STRING"),
                    new Parameter(true, "has_permission", "INTEGER")
            );
            this.info = new Info("Checks if a rank has a specific permission, returning 1 for true and 0 for false.", parameters);
        }

        @Override
        public ArrayList<VarObject> execute(ArrayList<String> args, ScriptFileObject scriptFileObject) {
            ArrayList<VarObject> result = new ArrayList<>();
            Boolean permission = null;
            try {
                permission = Rank_api.has_permission_from_rank(scriptFileObject.get_value(args.get(1)).get(0).value, scriptFileObject.get_value(args.get(2)).get(0).value);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            if (permission) {
                result.add(new VarObject("", "INTEGER", String.valueOf(1)));
            } else {
                result.add(new VarObject("", "INTEGER", String.valueOf(0)));
            }
            return result;
        }

        @Override
        public Info getInfo() {
            return this.info;
        }
    }

    public class SetPlayerRankCommand implements Command {
        private final Info info;

        public SetPlayerRankCommand() {
            List<Parameter> parameters = List.of(
                    new Parameter(false, "player_id", "STRING"),
                    new Parameter(false, "rank_id", "STRING")
            );
            this.info = new Info("Sets the rank of a specific player.", parameters);
        }

        @Override
        public ArrayList<VarObject> execute(ArrayList<String> args, ScriptFileObject scriptFileObject) {
            Rank_api.set_player_rank_from(scriptFileObject.get_value(args.get(1)).get(0).value, scriptFileObject.get_value(args.get(2)).get(0).value);
            return new ArrayList<>();
        }

        @Override
        public Info getInfo() {
            return this.info;
        }
    }

    public class CreateRankCommand implements Command {
        private final Info info;

        public CreateRankCommand() {
            List<Parameter> parameters = List.of(
                    new Parameter(false, "rank_id", "STRING")
            );
            this.info = new Info("Creates a new rank with the specified ID.", parameters);
        }

        @Override
        public ArrayList<VarObject> execute(ArrayList<String> args, ScriptFileObject scriptFileObject) {
            Rank_api.create_rank(scriptFileObject.get_value(args.get(1)).get(0).value);
            return new ArrayList<>();
        }

        @Override
        public Info getInfo() {
            return this.info;
        }
    }

    public class AddRuleCommand implements Command {
        private final Info info;

        public AddRuleCommand() {
            List<Parameter> parameters = List.of(
                    new Parameter(false, "rank_id", "STRING"),
                    new Parameter(false, "rule", "STRING")
            );
            this.info = new Info("Adds a rule to a specific rank.", parameters);
        }

        @Override
        public ArrayList<VarObject> execute(ArrayList<String> args, ScriptFileObject scriptFileObject) {
            Rank_api.add_rule(scriptFileObject.get_value(args.get(1)).get(0).value, scriptFileObject.get_value(args.get(2)).get(0).value);
            return new ArrayList<>();
        }

        @Override
        public Info getInfo() {
            return this.info;
        }
    }


    public class SendPlayerToServerCommand implements Command {
        private final Info info;

        public SendPlayerToServerCommand() {
            List<Parameter> parameters = new ArrayList<>();
            parameters.add(new Parameter(false, "player_id", "STRING"));
            parameters.add(new Parameter(false, "server_name", "STRING"));
            this.info = new Info("Sends a player to a specified server.", parameters);
        }

        @Override
        public ArrayList<VarObject> execute(ArrayList<String> args, ScriptFileObject scriptFileObject) throws IOException {
            JSONObject json = new JSONObject();
            json.put("player_id", scriptFileObject.get_value(args.get(1)).get(0).value);
            json.put("server_name", scriptFileObject.get_value(args.get(2)).get(0).value);
            Start_cluster_client.client.send_event(json, "send_player_to_server");
            return new ArrayList<>();
        }

        @Override
        public Info getInfo() {
            return this.info;
        }
    }

    public class EnableScriptCommand implements Command {
        private final Info info;

        public EnableScriptCommand() {
            List<Parameter> parameters = new ArrayList<>();
            parameters.add(new Parameter(false, "script_name", "STRING"));
            this.info = new Info("Enables a script by name.", parameters);
        }

        @Override
        public ArrayList<VarObject> execute(ArrayList<String> args, ScriptFileObject scriptFileObject) {
            String script_name = scriptFileObject.get_value(args.get(1)).get(0).value;
            return new ArrayList<>();
        }

        @Override
        public Info getInfo() {
            return this.info;
        }
    }

    public class IntegerAddCommand implements Command {
        private final Info info;

        public IntegerAddCommand() {
            List<Parameter> parameters = new ArrayList<>();
            parameters.add(new Parameter(false, "operand1", "INTEGER"));
            parameters.add(new Parameter(false, "operand2", "INTEGER"));
            parameters.add(new Parameter(true, "result", "INTEGER"));
            this.info = new Info("Adds two integers and returns the result.", parameters);
        }

        @Override
        public ArrayList<VarObject> execute(ArrayList<String> args, ScriptFileObject scriptFileObject) {
            ArrayList<VarObject> result = new ArrayList<>();
            result.add(new VarObject("", "INTEGER", String.valueOf(Integer.parseInt(scriptFileObject.get_value(args.get(1)).get(0).value) + Integer.parseInt(scriptFileObject.get_value(args.get(2)).get(0).value))));
            return result;
        }

        @Override
        public Info getInfo() {
            return this.info;
        }
    }

    public class IntegerSubtractCommand implements Command {
        private final Info info;

        public IntegerSubtractCommand() {
            List<Parameter> parameters = new ArrayList<>();
            parameters.add(new Parameter(false, "operand1", "INTEGER"));
            parameters.add(new Parameter(false, "operand2", "INTEGER"));
            parameters.add(new Parameter(true, "result", "INTEGER"));
            this.info = new Info("Subtracts the second integer from the first and returns the result.", parameters);
        }

        @Override
        public ArrayList<VarObject> execute(ArrayList<String> args, ScriptFileObject scriptFileObject) {
            ArrayList<VarObject> result = new ArrayList<>();
            result.add(new VarObject("", "INTEGER", String.valueOf(Integer.parseInt(scriptFileObject.get_value(args.get(1)).get(0).value) - Integer.parseInt(scriptFileObject.get_value(args.get(2)).get(0).value))));
            return result;
        }

        @Override
        public Info getInfo() {
            return this.info;
        }
    }
    public class IntegerDivideCommand implements Command {
        private final Info info;

        public IntegerDivideCommand() {
            List<Parameter> parameters = new ArrayList<>();
            parameters.add(new Parameter(false, "dividend", "INTEGER"));
            parameters.add(new Parameter(false, "divisor", "INTEGER"));
            parameters.add(new Parameter(true, "result", "INTEGER"));
            this.info = new Info("Divides the first integer by the second integer.", parameters);
        }

        @Override
        public ArrayList<VarObject> execute(ArrayList<String> args, ScriptFileObject scriptFileObject) {
            ArrayList<VarObject> result = new ArrayList<>();

            try {
                int dividend = Integer.parseInt(scriptFileObject.get_value(args.get(1)).get(0).value);
                int divisor = Integer.parseInt(scriptFileObject.get_value(args.get(2)).get(0).value);

                // Handling division by zero
                if (divisor == 0) {
                    scriptFileObject.throw_error("ERROR: Division by zero!", scriptFileObject.current_line);
                } else {
                    result.add(new VarObject("", "INTEGER", String.valueOf(dividend / divisor)));
                }
            } catch (NumberFormatException e) {
                scriptFileObject.throw_error("ERROR: Invalid number format!", scriptFileObject.current_line);
            }

            return result;
        }

        @Override
        public Info getInfo() {
            return this.info;
        }
    }
    public class GetPlayerByIdCommand implements Command {
        private final Info info;

        public GetPlayerByIdCommand() {
            List<Parameter> parameters = new ArrayList<>();
            parameters.add(new Parameter(false, "playerId", "STRING"));
            parameters.add(new Parameter(true, "id", "STRING"));
            parameters.add(new Parameter(true, "online", "INTEGER"));
            parameters.add(new Parameter(true, "didStartup", "STRING"));
            parameters.add(new Parameter(true, "displayName", "STRING"));
            parameters.add(new Parameter(true, "playerName", "STRING"));
            parameters.add(new Parameter(true, "playerRank", "STRING"));
            this.info = new Info("Retrieves a player by ID and returns various player attributes.", parameters);
        }

        @Override
        public ArrayList<VarObject> execute(ArrayList<String> args, ScriptFileObject scriptFileObject) throws SQLException {
            ArrayList<VarObject> result = new ArrayList<>();
            PlayerObject dbPlayer = Functions.getPlayer("id", scriptFileObject.get_value(args.get(1)).get(0).value);
            result.add(new VarObject("", "STRING", String.valueOf(dbPlayer.id.toString())));
            result.add(new VarObject("", "INTEGER", String.valueOf(dbPlayer.online)));
            result.add(new VarObject("", "STRING", dbPlayer.didStartup));
            result.add(new VarObject("", "STRING", dbPlayer.display_name));
            result.add(new VarObject("", "STRING", dbPlayer.player_name));
            result.add(new VarObject("", "STRING", dbPlayer.player_rank));
            return result;
        }

        @Override
        public Info getInfo() {
            return this.info;
        }
    }



    public class GetQuestCommand implements Command {
        private final Info info;

        public GetQuestCommand() {
            List<Parameter> parameters = new ArrayList<>();
            parameters.add(new Parameter(false, "playerId", "STRING"));
            parameters.add(new Parameter(false, "questName", "STRING"));
            parameters.add(new Parameter(false, "progress", "STRING"));
            parameters.add(new Parameter(true, "id", "STRING"));
            parameters.add(new Parameter(true, "quest_name", "STRING"));
            parameters.add(new Parameter(true, "progress", "INTEGER"));
            this.info = new Info("Retrieves a quest for a player based on provided parameters.", parameters);
        }

        @Override
        public ArrayList<VarObject> execute(ArrayList<String> args, ScriptFileObject scriptFileObject) throws SQLException {
            ArrayList<VarObject> result = new ArrayList<>();
            ArrayList<Player_questObject> player_questObjects = Functions.getPlayerQuest(
                    scriptFileObject.get_value(args.get(1)).get(0).value,
                    scriptFileObject.get_value(args.get(2)).get(0).value,
                    scriptFileObject.get_value(args.get(3)).get(0).value
            );
            if (player_questObjects.size() > 0) {
                Player_questObject player_questObject = player_questObjects.get(0);
                result.add(new VarObject("", "STRING", String.valueOf(player_questObject.id)));
                result.add(new VarObject("", "STRING", String.valueOf(player_questObject.quest_name)));
                result.add(new VarObject("", "INTEGER", String.valueOf(player_questObject.progress)));
            } else {
                result.add(new VarObject("", "STRING", "null"));
                result.add(new VarObject("", "STRING", "null"));
                result.add(new VarObject("", "INTEGER", "0"));
            }
            return result;
        }

        @Override
        public Info getInfo() {
            return this.info;
        }
    }
    public class CreateQuestCommand implements Command {
        private final Info info;

        public CreateQuestCommand() {
            List<Parameter> parameters = new ArrayList<>();
            parameters.add(new Parameter(false, "id", "STRING"));
            parameters.add(new Parameter(false, "quest_name", "STRING"));
            parameters.add(new Parameter(false, "progress", "INTEGER"));
            this.info = new Info("Creates a new quest with the specified id, name, and progress.", parameters);
        }

        @Override
        public ArrayList<VarObject> execute(ArrayList<String> args, ScriptFileObject scriptFileObject) throws SQLException {
            Player_questObject player_questObject = new Player_questObject();
            player_questObject.id = scriptFileObject.get_value(args.get(1)).get(0).value;
            player_questObject.quest_name = scriptFileObject.get_value(args.get(2)).get(0).value;
            player_questObject.progress = Integer.parseInt(scriptFileObject.get_value(args.get(3)).get(0).value);
            Functions.createPlayer_quest(player_questObject);
            return new ArrayList<>();
        }

        @Override
        public Info getInfo() {
            return this.info;
        }
    }
    public class UpdateQuestCommand implements Command {
        private final Info info;

        public UpdateQuestCommand() {
            List<Parameter> parameters = new ArrayList<>();
            parameters.add(new Parameter(false, "arg1", "STRING"));
            parameters.add(new Parameter(false, "arg2", "STRING"));
            parameters.add(new Parameter(false, "arg3", "STRING"));
            parameters.add(new Parameter(false, "arg4", "STRING"));
            this.info = new Info("Update the quest information in the database.", parameters);
        }

        @Override
        public ArrayList<VarObject> execute(ArrayList<String> args, ScriptFileObject scriptFileObject) throws SQLException {
            Functions.update("players_quests", scriptFileObject.get_value(args.get(1)).get(0).value, scriptFileObject.get_value(args.get(2)).get(0).value, scriptFileObject.get_value(args.get(3)).get(0).value, scriptFileObject.get_value(args.get(4)).get(0).value);
            return new ArrayList<>();
        }

        @Override
        public Info getInfo() {
            return this.info;
        }
    }

    public class VarPoolCreateCommand implements Command {
        private final Info info;

        public VarPoolCreateCommand() {
            List<Parameter> parameters = new ArrayList<>();
            parameters.add(new Parameter(false, "varPoolName", "STRING"));
            this.info = new Info("Creates a new variable pool with the specified name.", parameters);
        }

        @Override
        public ArrayList<VarObject> execute(ArrayList<String> args, ScriptFileObject scriptFileObject) {
            if (Var.get_var_pool_by_name(scriptFileObject.get_value(args.get(1)).get(0).value) == null) {
                Var_pool var_pool = new Var_pool();
                var_pool.name = scriptFileObject.get_value(args.get(1)).get(0).value;
                Var.var_pools.add(var_pool);
            } else {
                scriptFileObject.throw_error("ERROR: Var_pool " + scriptFileObject.get_value(args.get(1)).get(0).value + " already defined!", scriptFileObject.current_line);
            }
            return new ArrayList<>();
        }

        @Override
        public Info getInfo() {
            return this.info;
        }
    }

    public class VarPoolGetCommand implements Command {
        private final Info info;

        public VarPoolGetCommand() {
            List<Parameter> parameters = new ArrayList<>();
            parameters.add(new Parameter(false, "varPoolName", "STRING"));
            parameters.add(new Parameter(false, "varName", "STRING"));
            parameters.add(new Parameter(true, "retrievedVar", "VarObject"));
            this.info = new Info("Retrieves a variable from the specified variable pool.", parameters);
        }

        @Override
        public ArrayList<VarObject> execute(ArrayList<String> args, ScriptFileObject scriptFileObject) {
            ArrayList<VarObject> result = new ArrayList<>();
            if (Var.get_var_pool_by_name(scriptFileObject.get_value(args.get(1)).get(0).value) == null) {
                scriptFileObject.throw_error("ERROR: Var_pool " + scriptFileObject.get_value(args.get(1)).get(0).value + " is not defined!", scriptFileObject.current_line);
            } else {
                Var_pool var_pool = Var.get_var_pool_by_name(scriptFileObject.get_value(args.get(1)).get(0).value);
                result.add(var_pool.get_var_by_name(scriptFileObject.get_value(args.get(2)).get(0).value));
            }
            return result;
        }

        @Override
        public Info getInfo() {
            return this.info;
        }
    }

    public class VarPoolPutCommand implements Command {
        private final Info info;

        public VarPoolPutCommand() {
            List<Parameter> parameters = new ArrayList<>();
            parameters.add(new Parameter(false, "varPoolName", "STRING"));
            parameters.add(new Parameter(false, "var", "VarObject"));
            this.info = new Info("Adds a variable to the specified variable pool.", parameters);
        }

        @Override
        public ArrayList<VarObject> execute(ArrayList<String> args, ScriptFileObject scriptFileObject) {
            if (Var.get_var_pool_by_name(scriptFileObject.get_value(args.get(1)).get(0).value) == null) {
                scriptFileObject.throw_error("ERROR: Var_pool " + scriptFileObject.get_value(args.get(1)).get(0).value + " is not defined!", scriptFileObject.current_line);
            } else {
                Var_pool var_pool = Var.get_var_pool_by_name(scriptFileObject.get_value(args.get(1)).get(0).value);
                var_pool.varObjects.add(scriptFileObject.get_value(args.get(2)).get(0));
            }
            return new ArrayList<>();
        }

        @Override
        public Info getInfo() {
            return this.info;
        }
    }

    public class VarPoolChangeCommand implements Command {
        private final Info info;

        public VarPoolChangeCommand() {
            List<Parameter> parameters = new ArrayList<>();
            parameters.add(new Parameter(false, "varPoolName", "STRING"));
            parameters.add(new Parameter(false, "varName", "STRING"));
            parameters.add(new Parameter(false, "newValue", "STRING"));
            this.info = new Info("Changes the value of a variable in the specified variable pool.", parameters);
        }

        @Override
        public ArrayList<VarObject> execute(ArrayList<String> args, ScriptFileObject scriptFileObject) {
            if (Var.get_var_pool_by_name(scriptFileObject.get_value(args.get(1)).get(0).value) == null) {
                scriptFileObject.throw_error("ERROR: Var_pool " + scriptFileObject.get_value(args.get(1)).get(0).value + " is not defined!", scriptFileObject.current_line);
            } else {
                Var_pool var_pool = Var.get_var_pool_by_name(scriptFileObject.get_value(args.get(1)).get(0).value);
                var_pool.change_value_of_var(scriptFileObject.get_value(args.get(2)).get(0).value, scriptFileObject.get_value(args.get(3)).get(0).value);
            }
            return new ArrayList<>();
        }

        @Override
        public Info getInfo() {
            return this.info;
        }
    }


    public class SetParentCommand implements Command {
        private final Info info;

        public SetParentCommand() {
            List<Parameter> parameters = new ArrayList<>();
            parameters.add(new Parameter(false, "parentRankId", "STRING"));
            parameters.add(new Parameter(false, "childRankId", "STRING"));
            this.info = new Info("Sets the parent rank for a specified rank.", parameters);
        }

        @Override
        public ArrayList<VarObject> execute(ArrayList<String> args, ScriptFileObject scriptFileObject) {
            Rank_api.set_parent(scriptFileObject.get_value(args.get(1)).get(0).value,
                    scriptFileObject.get_value(args.get(2)).get(0).value);
            return new ArrayList<>();
        }

        @Override
        public Info getInfo() {
            return this.info;
        }
    }

    public class SetRankColorCommand implements Command {
        private final Info info;

        public SetRankColorCommand() {
            List<Parameter> parameters = new ArrayList<>();
            parameters.add(new Parameter(false, "rankId", "STRING"));
            parameters.add(new Parameter(false, "color", "STRING"));
            this.info = new Info("Sets the display color for a specified rank.", parameters);
        }

        @Override
        public ArrayList<VarObject> execute(ArrayList<String> args, ScriptFileObject scriptFileObject) {
            Rank_api.set_rank_color(scriptFileObject.get_value(args.get(1)).get(0).value,
                    scriptFileObject.get_value(args.get(2)).get(0).value);
            return new ArrayList<>();
        }

        @Override
        public Info getInfo() {
            return this.info;
        }
    }



    private static final Map<String, Command> commands = new HashMap<>();

    {
        commands.put("consoleLog:", new ConsoleLogCommand());
        commands.put("debugLog:", new DebugLogCommand());
        commands.put("integer.*:", new IntegerMultiplyCommand());
        commands.put("string.join:", new StringJoinCommand());
        commands.put("system.send_player_to_server:", new SendPlayerToServerCommand());
        commands.put("system.enable_script:", new EnableScriptCommand());
        commands.put("rank.create_rank:", new CreateRankCommand());
        commands.put("rank.add_rule:", new AddRuleCommand());
        commands.put("rank.set_parent:", new SetParentCommand());
        commands.put("rank.set_rank_color:", new SetRankColorCommand());
        commands.put("rank.set_prefix:", new SetPrefixCommand());
        commands.put("rank.set_prefix_color:", new SetPrefixColorCommand());
        commands.put("rank.get:", new GetRankCommand());
        commands.put("rank.remove_rule:", new RemoveRuleCommand());
        commands.put("rank.has_permission:", new HasPermissionCommand());
        commands.put("rank.set_player_rank:", new SetPlayerRankCommand());
        commands.put("integer.+:", new IntegerAddCommand());
        commands.put("integer.-:", new IntegerSubtractCommand());
        commands.put("integer./:", new IntegerDivideCommand());
        commands.put("get_player_by_id:", new GetPlayerByIdCommand());
        commands.put("get_quest:", new GetQuestCommand());
        commands.put("create_quest:", new CreateQuestCommand());
        commands.put("update_quest:", new UpdateQuestCommand());
        commands.put("var_pool.create:", new VarPoolCreateCommand());
        commands.put("var_pool.get:", new VarPoolGetCommand());
        commands.put("var_pool.put:", new VarPoolPutCommand());
        commands.put("var_pool.change:", new VarPoolChangeCommand());
    }
    public ArrayList<VarObject> execute_functions(ArrayList<String> args,
                                                  Integer index, ScriptFileObject scriptFileObject) throws SQLException, IOException {
        Command command = commands.get(args.get(0));
        if (command != null) {
            return command.execute(args, scriptFileObject);
        }
        return null;
    }
}
