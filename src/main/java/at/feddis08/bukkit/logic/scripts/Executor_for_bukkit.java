package at.feddis08.bukkit.logic.scripts;

import at.feddis08.Boot;
import at.feddis08.bukkit.MMORPG;
import at.feddis08.bukkit.commands.Gamemode;
import at.feddis08.bukkit.commands.Rank;
import at.feddis08.bukkit.commands.Warp;
import at.feddis08.tools.io.database.Functions;
import at.feddis08.tools.io.database.objects.PlayerObject;
import at.feddis08.tools.io.database.objects.Player_questObject;
import at.feddis08.tools.io.database.objects.RankObject;
import at.feddis08.bukkit.logic.game.Var;
import at.feddis08.bukkit.logic.game.mob_spawner.Spawner;
import at.feddis08.bukkit.logic.game.ore_mine.Main;
import at.feddis08.bukkit.logic.game.ore_mine.Mine;
import at.feddis08.bukkit.logic.scripts.ArrayObject;
import at.feddis08.bukkit.logic.scripts.VarObject;
import at.feddis08.bukkit.logic.scripts.Var_pool;
import at.feddis08.bukkit.minecraft.tools.Methods;
import at.feddis08.bukkit.minecraft.tools.classes.Book;
import at.feddis08.tools.io.text_files.files.file_objects.ScriptFileObject;
import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class Executor_for_bukkit {
    public interface Command {
        ArrayList<VarObject> execute(ArrayList<String> args, ScriptFileObject scriptFileObject) throws SQLException, IOException, ExecutionException, InterruptedException;
    }

    // SetBlockCommand
    public static class SetBlockCommand implements Command {
        @Override
        public ArrayList<VarObject> execute(ArrayList<String> args, ScriptFileObject scriptFileObject) {
            String world_name = scriptFileObject.get_value(args.get(5)).get(0).value;
            int x = Integer.parseInt(scriptFileObject.get_value(args.get(1)).get(0).value);
            int y = Integer.parseInt(scriptFileObject.get_value(args.get(2)).get(0).value);
            int z = Integer.parseInt(scriptFileObject.get_value(args.get(3)).get(0).value);
            String material_name = scriptFileObject.get_value(args.get(4)).get(0).value;
            Bukkit.getScheduler().runTask(MMORPG.plugin, () -> MMORPG.Server
                    .getWorld(world_name)
                    .getBlockAt(x, y, z)
                    .setType(Material.getMaterial(material_name)));
            return new ArrayList<>();
        }
    }
    public static class BroadcastMessageCommand implements Command {
        @Override
        public ArrayList<VarObject> execute(ArrayList<String> args, ScriptFileObject scriptFileObject) {
            MMORPG.Server.broadcast("System", scriptFileObject.get_value(args.get(1)).get(0).value);
            return new ArrayList<>();
        }
    }
    public static class OpenInventoryOnMinecraftPlayerCommand implements Command {
        @Override
        public ArrayList<VarObject> execute(ArrayList<String> args, ScriptFileObject scriptFileObject) {
            Methods.open_inv_on_minecraft_player(scriptFileObject.get_value(args.get(1)).get(0).value, scriptFileObject.get_value(args.get(2)).get(0).value);
            return new ArrayList<>();
        }
    }

    // SendMessageToPlayerCommand
    public static class SendMessageToPlayerCommand implements Command {
        @Override
        public ArrayList<VarObject> execute(ArrayList<String> args, ScriptFileObject scriptFileObject) {
            Methods.send_minecraft_message_by_id(scriptFileObject.get_value(args.get(1)).get(0).value, scriptFileObject.get_value(args.get(2)).get(0).value);
            return new ArrayList<>();
        }
    }

    // OpenBookCommand
    public static class OpenBookCommand implements Command {
        @Override
        public ArrayList<VarObject> execute(ArrayList<String> args, ScriptFileObject scriptFileObject) {
            MMORPG.Server.getPlayer(UUID.fromString(scriptFileObject.get_value(args.get(1)).get(0).value))
                    .closeInventory();
            MMORPG.Server.getPlayer(UUID.fromString(scriptFileObject.get_value(args.get(1)).get(0).value))
                    .openBook(Var.get_book_by_display_name(scriptFileObject.get_value(args.get(2)).get(0).value).book);
            return new ArrayList<>();
        }
    }

    // CreateBookCommand
    public static class CreateBookCommand implements Command {
        @Override
        public ArrayList<VarObject> execute(ArrayList<String> args, ScriptFileObject scriptFileObject) {
            Var.books.add(new Book(scriptFileObject.get_value(args.get(1)).get(0).value, scriptFileObject.get_value(args.get(2)).get(0).value, scriptFileObject.get_value(args.get(2)).get(0).value));
            return new ArrayList<>();
        }
    }

    // AddPageToBookCommand
    public static class AddPageToBookCommand implements Command {
        @Override
        public ArrayList<VarObject> execute(ArrayList<String> args, ScriptFileObject scriptFileObject) {
            BookMeta itemMeta = (BookMeta) Var.get_book_by_display_name(scriptFileObject.get_value(args.get(1)).get(0).value).book.getItemMeta();
            itemMeta.addPage(scriptFileObject.get_value(args.get(2)).get(0).value);
            Var.get_book_by_display_name(scriptFileObject.get_value(args.get(1)).get(0).value).book.setItemMeta(itemMeta);
            return new ArrayList<>();
        }
    }
    public static class CreateSpawnerCommand implements Command {
        @Override
        public ArrayList<VarObject> execute(ArrayList<String> args, ScriptFileObject scriptFileObject) {
            Spawner spawner = new Spawner();
            spawner.name = scriptFileObject.get_value(args.get(1)).get(0).value;
            spawner.mob_type = scriptFileObject.get_value(args.get(2)).get(0).value;
            spawner.world_name = scriptFileObject.get_value(args.get(3)).get(0).value;
            spawner.max_mobs = Integer.parseInt(scriptFileObject.get_value(args.get(4)).get(0).value);
            spawner.cool_down_ticks = Integer.parseInt(scriptFileObject.get_value(args.get(5)).get(0).value);
            at.feddis08.bukkit.logic.game.mob_spawner.Main.spawners.add(spawner);
            return new ArrayList<>();
        }
    }

    // AddLocationToSpawnerCommand
    public static class AddLocationToSpawnerCommand implements Command {
        @Override
        public ArrayList<VarObject> execute(ArrayList<String> args, ScriptFileObject scriptFileObject) {
            Spawner spawner = at.feddis08.bukkit.logic.game.mob_spawner.Main.get_spawner_by_name(scriptFileObject.get_value(args.get(1)).get(0).value);
            at.feddis08.bukkit.minecraft.tools.classes.Location location = new at.feddis08.bukkit.minecraft.tools.classes.Location();
            location.x = Integer.parseInt(scriptFileObject.get_value(args.get(2)).get(0).value);
            location.y = Integer.parseInt(scriptFileObject.get_value(args.get(3)).get(0).value);
            location.z = Integer.parseInt(scriptFileObject.get_value(args.get(4)).get(0).value);
            spawner.spawn_points.add(location);
            return new ArrayList<>();
        }
    }

    // GetSpawnerByNameCommand
    public static class GetSpawnerByNameCommand implements Command {
        @Override
        public ArrayList<VarObject> execute(ArrayList<String> args, ScriptFileObject scriptFileObject) {
            ArrayList<VarObject> result = new ArrayList<>();
            Spawner spawner = at.feddis08.bukkit.logic.game.mob_spawner.Main.get_spawner_by_name(scriptFileObject.get_value(args.get(1)).get(0).value);
            if (spawner != null) {
                result.add(new VarObject("", "STRING", spawner.name));
                result.add(new VarObject("", "STRING", spawner.mob_type));
                result.add(new VarObject("", "STRING", spawner.world_name));
                result.add(new VarObject("", "INTEGER", String.valueOf(spawner.max_mobs)));
                result.add(new VarObject("", "INTEGER", String.valueOf(spawner.cool_down_ticks)));
            } else {
                result.add(new VarObject("", "STRING", "null"));
                result.add(new VarObject("", "STRING", "null"));
                result.add(new VarObject("", "STRING", "null"));
                result.add(new VarObject("", "INTEGER", "0"));
                result.add(new VarObject("", "INTEGER", "0"));
            }
            return result;
        }
    }
    public static class CreateMineCommand implements Command {
        @Override
        public ArrayList<VarObject> execute(ArrayList<String> args, ScriptFileObject scriptFileObject) {
            Mine mine = new Mine();
            mine.name = scriptFileObject.get_value(args.get(1)).get(0).value;
            mine.material_name = scriptFileObject.get_value(args.get(2)).get(0).value;
            mine.world_name = scriptFileObject.get_value(args.get(3)).get(0).value;
            mine.x = Integer.parseInt(scriptFileObject.get_value(args.get(4)).get(0).value);
            mine.y = Integer.parseInt(scriptFileObject.get_value(args.get(5)).get(0).value);
            mine.z = Integer.parseInt(scriptFileObject.get_value(args.get(6)).get(0).value);
            mine.cool_down_ticks = Integer.parseInt(scriptFileObject.get_value(args.get(7)).get(0).value);
            Main.mines.add(mine);
            return new ArrayList<>();
        }
    }

    // GetMineByCoordsCommand
    public static class GetMineByCoordsCommand implements Command {
        @Override
        public ArrayList<VarObject> execute(ArrayList<String> args, ScriptFileObject scriptFileObject) {
            ArrayList<VarObject> result = new ArrayList<>();
            Mine mine = Main.get_mine_by_cords(scriptFileObject.get_value(args.get(1)).get(0).value,
                    Integer.parseInt(scriptFileObject.get_value(args.get(2)).get(0).value),
                    Integer.parseInt(scriptFileObject.get_value(args.get(3)).get(0).value),
                    Integer.parseInt(scriptFileObject.get_value(args.get(4)).get(0).value));
            if (mine != null) {
                result.add(new VarObject("", "STRING", mine.name));
                result.add(new VarObject("", "STRING", mine.material_name));
                result.add(new VarObject("", "STRING", mine.world_name));
                result.add(new VarObject("", "INTEGER", String.valueOf(mine.x)));
                result.add(new VarObject("", "INTEGER", String.valueOf(mine.y)));
                result.add(new VarObject("", "INTEGER", String.valueOf(mine.z)));
                result.add(new VarObject("", "INTEGER", String.valueOf(mine.cool_down_ticks)));
            } else {
                result.add(new VarObject("", "STRING", "null"));
                result.add(new VarObject("", "STRING", "null"));
                result.add(new VarObject("", "STRING", "null"));
                result.add(new VarObject("", "INTEGER", "0"));
                result.add(new VarObject("", "INTEGER", "0"));
                result.add(new VarObject("", "INTEGER", "0"));
                result.add(new VarObject("", "INTEGER", "0"));
            }
            return result;
        }
    }
    // BreakMineCommand
    public static class BreakMineCommand implements Command {
        @Override
        public ArrayList<VarObject> execute(ArrayList<String> args, ScriptFileObject scriptFileObject) throws IOException, ExecutionException, InterruptedException {
            Mine mine = Main.get_mine_by_name(scriptFileObject.get_value(args.get(1)).get(0).value);
            mine.break_block(scriptFileObject.get_value(args.get(2)).get(0).value);
            return new ArrayList<>();
        }
    }

    // GetMineByNameCommand
    public static class GetMineByNameCommand implements Command {
        @Override
        public ArrayList<VarObject> execute(ArrayList<String> args, ScriptFileObject scriptFileObject) {
            ArrayList<VarObject> result = new ArrayList<>();
            Mine mine = Main.get_mine_by_name(scriptFileObject.get_value(args.get(1)).get(0).value);
            if (mine != null) {
                result.add(new VarObject("", "STRING", mine.name));
                result.add(new VarObject("", "STRING", mine.material_name));
                result.add(new VarObject("", "STRING", mine.world_name));
                result.add(new VarObject("", "INTEGER", String.valueOf(mine.x)));
                result.add(new VarObject("", "INTEGER", String.valueOf(mine.y)));
                result.add(new VarObject("", "INTEGER", String.valueOf(mine.z)));
                result.add(new VarObject("", "INTEGER", String.valueOf(mine.cool_down_ticks)));
            } else {
                result.add(new VarObject("", "STRING", "null"));
                result.add(new VarObject("", "STRING", "null"));
                result.add(new VarObject("", "STRING", "null"));
                result.add(new VarObject("", "INTEGER", "0"));
                result.add(new VarObject("", "INTEGER", "0"));
                result.add(new VarObject("", "INTEGER", "0"));
                result.add(new VarObject("", "INTEGER", "0"));
            }
            return result;
        }
    }
    // ChangeGamemodeOfPlayerCommand
    public static class ChangeGamemodeOfPlayerCommand implements Command {
        @Override
        public ArrayList<VarObject> execute(ArrayList<String> args, ScriptFileObject scriptFileObject) throws SQLException {
            Gamemode.changeBukkitPlayerGamemode(scriptFileObject.get_value(args.get(1)).get(0).value, scriptFileObject.get_value(args.get(2)).get(0).value);
            return new ArrayList<>();
        }
    }

    // WarpPlayerCommand
    public static class WarpPlayerCommand implements Command {
        @Override
        public ArrayList<VarObject> execute(ArrayList<String> args, ScriptFileObject scriptFileObject) throws SQLException {
            Warp.warp_player(scriptFileObject.get_value(args.get(1)).get(0).value, scriptFileObject.get_value(args.get(2)).get(0).value);
            return new ArrayList<>();
        }
    }

    // SpawnEntityCommand
    public static class SpawnEntityCommand implements Command {
        @Override
        public ArrayList<VarObject> execute(ArrayList<String> args, ScriptFileObject scriptFileObject) {
            Entity entity = MMORPG.Server.getWorld(scriptFileObject.get_value(args.get(1)).get(0).value).spawnEntity(
                    new Location(MMORPG.Server.getWorld(scriptFileObject.get_value(args.get(1)).get(0).value),
                            Double.valueOf(scriptFileObject.get_value(args.get(2)).get(0).value),
                            Double.valueOf(scriptFileObject.get_value(args.get(3)).get(0).value),
                            Double.valueOf(scriptFileObject.get_value(args.get(4)).get(0).value)),
                    EntityType.fromName(scriptFileObject.get_value(args.get(5)).get(0).value));
            ArrayList<VarObject> result = new ArrayList<>();
            result.add(new VarObject("", "STRING", entity.getUniqueId().toString()));
            return result;
        }
    }

    // ChangeEntitySpeedCommand - Assuming this command changes the speed of an entity.
    // The logic for this command is not complete in your example.
    public static class ChangeEntitySpeedCommand implements Command {
        @Override
        public ArrayList<VarObject> execute(ArrayList<String> args, ScriptFileObject scriptFileObject) {
            // Implement the logic for changing entity speed here
            return new ArrayList<>();
        }
    }

    // GivePlayerItemStackCommand
    public static class GivePlayerItemStackCommand implements Command {
        @Override
        public ArrayList<VarObject> execute(ArrayList<String> args, ScriptFileObject scriptFileObject) {
            MMORPG.Server.getPlayer(UUID.fromString(scriptFileObject.get_value(args.get(1)).get(0).value)).getInventory().addItem(
                    new ItemStack(Material.valueOf(scriptFileObject.get_value(args.get(2)).get(0).value),
                            Integer.parseInt(scriptFileObject.get_value(args.get(3)).get(0).value)));
            return new ArrayList<>();
        }
    }
    // Command Registry
    public static final Map<String, Command> commands = new HashMap<>();

    static {
        commands.put("minecraft.set_block:", new SetBlockCommand());
        commands.put("minecraft.broadcast_message:", new BroadcastMessageCommand());
        commands.put("open_inv_on_minecraft_player:", new OpenBookCommand());
        commands.put("minecraft.send_message_to_player:", new SendMessageToPlayerCommand());
        commands.put("minecraft.book.open:", new OpenBookCommand());
        commands.put("minecraft.book.create:", new CreateBookCommand());
        commands.put("minecraft.book.add_page:", new AddPageToBookCommand());
        commands.put("spawner.create:", new CreateSpawnerCommand());
        commands.put("spawner.add_location:", new AddLocationToSpawnerCommand());
        commands.put("spawner.get_by_name:", new GetSpawnerByNameCommand());
        commands.put("create_mine:", new CreateMineCommand());
        commands.put("get_mine_by_cords:", new GetMineByCoordsCommand());
        commands.put("break_mine:", new BreakMineCommand());
        commands.put("get_mine_by_name:", new GetMineByNameCommand());
        commands.put("minecraft.change_gamemode_of_player:", new ChangeGamemodeOfPlayerCommand());
        commands.put("warp_player:", new WarpPlayerCommand());
        commands.put("minecraft.spawn_entity:", new SpawnEntityCommand());
        commands.put("minecraft.change_entity_speed:", new ChangeEntitySpeedCommand());
        commands.put("minecraft.give_player_itemstack:", new GivePlayerItemStackCommand());

        // Add other command registrations here
    }

    public static ArrayList<VarObject> execute_functions(ArrayList<String> args, Integer index, ScriptFileObject scriptFileObject) throws SQLException, IOException, ExecutionException, InterruptedException {
        Command command = commands.get(args.get(0));
        if (command != null) {
            return command.execute(args, scriptFileObject);
        }
        return null;
    }


}
