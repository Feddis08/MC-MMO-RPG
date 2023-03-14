package at.feddis08.bukkit.logic.scripts;

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
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;
public class Executor_for_bukkit {
    public static ArrayList<VarObject> execute_functions(ArrayList<String> args, Integer index) throws SQLException {
        ArrayList<VarObject> result = new ArrayList<>();
        if (Objects.equals(args.get(0), "open_inv_on_minecraft_player:")){
            Methods.open_inv_on_minecraft_player(ScriptFileObject.get_value(args.get(1)).get(0).value, ScriptFileObject.get_value(args.get(2)).get(0).value);
        }
        if (Objects.equals(args.get(0), "minecraft.send_message_to_player:")){
            Methods.send_minecraft_message_by_id(ScriptFileObject.get_value(args.get(1)).get(0).value, ScriptFileObject.get_value(args.get(2)).get(0).value);
        }
        if (Objects.equals(args.get(0), "minecraft.book.open:")){
            MMORPG.Server.getPlayer(UUID.fromString(ScriptFileObject.get_value(args.get(1)).get(0).value)).closeInventory();
            MMORPG.Server.getPlayer(UUID.fromString(ScriptFileObject.get_value(args.get(1)).get(0).value)).openBook(Var.get_book_by_display_name(ScriptFileObject.get_value(args.get(2)).get(0).value).book);
        }
        if (Objects.equals(args.get(0), "minecraft.book.create:")){
            Var.books.add(new Book(ScriptFileObject.get_value(args.get(1)).get(0).value, ScriptFileObject.get_value(args.get(2)).get(0).value, ScriptFileObject.get_value(args.get(2)).get(0).value));
        }
        if (Objects.equals(args.get(0), "minecraft.book.add_page:")){
            BookMeta itemMeta = (BookMeta) Var.get_book_by_display_name(ScriptFileObject.get_value(args.get(1)).get(0).value).book.getItemMeta();
            itemMeta.addPage(ScriptFileObject.get_value(args.get(2)).get(0).value);
            Var.get_book_by_display_name(ScriptFileObject.get_value(args.get(1)).get(0).value).book.setItemMeta(itemMeta);
        }
        if (Objects.equals(args.get(0), "spawner.create:")){
            Spawner spawner = new Spawner();
            spawner.name = ScriptFileObject.get_value(args.get(1)).get(0).value;
            spawner.mob_type = ScriptFileObject.get_value(args.get(2)).get(0).value;
            spawner.world_name = ScriptFileObject.get_value(args.get(3)).get(0).value;
            spawner.max_mobs = Integer.parseInt(ScriptFileObject.get_value(args.get(4)).get(0).value);
            spawner.cool_down_ticks = Integer.parseInt(ScriptFileObject.get_value(args.get(5)).get(0).value);
            at.feddis08.bukkit.logic.game.mob_spawner.Main.spawners.add(spawner);
        }
        if (Objects.equals(args.get(0), "spawner.add_location:")){
            Spawner spawner = at.feddis08.bukkit.logic.game.mob_spawner.Main.get_spawner_by_name(ScriptFileObject.get_value(args.get(1)).get(0).value);
            at.feddis08.bukkit.minecraft.tools.classes.Location location = new at.feddis08.bukkit.minecraft.tools.classes.Location();
            location.x = Integer.parseInt(ScriptFileObject.get_value(args.get(2)).get(0).value);
            location.y = Integer.parseInt(ScriptFileObject.get_value(args.get(3)).get(0).value);
            location.z = Integer.parseInt(ScriptFileObject.get_value(args.get(4)).get(0).value);
            spawner.spawn_points.add(location);
        }
        if (Objects.equals(args.get(0), "spawner.get_by_name:")){
            Spawner spawner = at.feddis08.bukkit.logic.game.mob_spawner.Main.get_spawner_by_name (ScriptFileObject.get_value(args.get(1)).get(0).value);
            if (!(spawner == null)){
                result.add(new VarObject("", "STRING", String.valueOf(spawner.name)));
                result.add(new VarObject("", "STRING", String.valueOf(spawner.mob_type)));
                result.add(new VarObject("", "STRING", String.valueOf(spawner.world_name)));
                result.add(new VarObject("", "INTEGER", String.valueOf(spawner.max_mobs)));
                result.add(new VarObject("", "INTEGER", String.valueOf(spawner.cool_down_ticks)));
            }else{
                result.add(new VarObject("", "STRING", String.valueOf("null")));
                result.add(new VarObject("", "STRING", String.valueOf("null")));
                result.add(new VarObject("", "STRING", String.valueOf("null")));
                result.add(new VarObject("", "INTEGER", String.valueOf(0)));
                result.add(new VarObject("", "INTEGER", String.valueOf(0)));
            }
        }
        if (Objects.equals(args.get(0), "create_mine:")){
            Mine mine = new Mine();
            mine.name = ScriptFileObject.get_value(args.get(1)).get(0).value;
            mine.material_name = ScriptFileObject.get_value(args.get(2)).get(0).value;
            mine.world_name = ScriptFileObject.get_value(args.get(3)).get(0).value;
            mine.x = Integer.parseInt(ScriptFileObject.get_value(args.get(4)).get(0).value);
            mine.y = Integer.parseInt(ScriptFileObject.get_value(args.get(5)).get(0).value);
            mine.z = Integer.parseInt(ScriptFileObject.get_value(args.get(6)).get(0).value);
            mine.cool_down_ticks = Integer.parseInt(ScriptFileObject.get_value(args.get(7)).get(0).value);
            Main.mines.add(mine);
        }
        if (Objects.equals(args.get(0), "get_mine_by_cords:")){
            Mine mine = Main.get_mine_by_cords (ScriptFileObject.get_value(args.get(1)).get(0).value, Integer.parseInt(ScriptFileObject.get_value(args.get(2)).get(0).value), Integer.parseInt(ScriptFileObject.get_value(args.get(3)).get(0).value), Integer.parseInt(ScriptFileObject.get_value(args.get(4)).get(0).value));
            if (!(mine == null)){
                result.add(new VarObject("", "STRING", String.valueOf(mine.name)));
                result.add(new VarObject("", "STRING", String.valueOf(mine.material_name)));
                result.add(new VarObject("", "STRING", String.valueOf(mine.world_name)));
                result.add(new VarObject("", "INTEGER", String.valueOf(mine.x)));
                result.add(new VarObject("", "INTEGER", String.valueOf(mine.y)));
                result.add(new VarObject("", "INTEGER", String.valueOf(mine.z)));
                result.add(new VarObject("", "INTEGER", String.valueOf(mine.cool_down_ticks)));
            }else{
                result.add(new VarObject("", "STRING", String.valueOf("null")));
                result.add(new VarObject("", "STRING", String.valueOf("null")));
                result.add(new VarObject("", "STRING", String.valueOf("null")));
                result.add(new VarObject("", "INTEGER", String.valueOf(0)));
                result.add(new VarObject("", "INTEGER", String.valueOf(0)));
                result.add(new VarObject("", "INTEGER", String.valueOf(0)));
                result.add(new VarObject("", "INTEGER", String.valueOf(0)));
            }
        }
        if (Objects.equals(args.get(0), "break_mine:")){
            Mine mine = Main.get_mine_by_name (ScriptFileObject.get_value(args.get(1)).get(0).value);
            mine.break_block(ScriptFileObject.get_value(args.get(2)).get(0).value);
        }
        if (Objects.equals(args.get(0), "get_mine_by_name:")){
            Mine mine = Main.get_mine_by_name (ScriptFileObject.get_value(args.get(1)).get(0).value);
            if (!(mine == null)){
                result.add(new VarObject("", "STRING", String.valueOf(mine.name)));
                result.add(new VarObject("", "STRING", String.valueOf(mine.material_name)));
                result.add(new VarObject("", "STRING", String.valueOf(mine.world_name)));
                result.add(new VarObject("", "INTEGER", String.valueOf(mine.x)));
                result.add(new VarObject("", "INTEGER", String.valueOf(mine.y)));
                result.add(new VarObject("", "INTEGER", String.valueOf(mine.z)));
                result.add(new VarObject("", "INTEGER", String.valueOf(mine.cool_down_ticks)));
            }else{
                result.add(new VarObject("", "STRING", String.valueOf("null")));
                result.add(new VarObject("", "STRING", String.valueOf("null")));
                result.add(new VarObject("", "STRING", String.valueOf("null")));
                result.add(new VarObject("", "INTEGER", String.valueOf(0)));
                result.add(new VarObject("", "INTEGER", String.valueOf(0)));
                result.add(new VarObject("", "INTEGER", String.valueOf(0)));
                result.add(new VarObject("", "INTEGER", String.valueOf(0)));
            }
        }
        if (Objects.equals(args.get(0), "minecraft.change_gamemode_of_player:")){
            Gamemode.changeBukkitPlayerGamemode(ScriptFileObject.get_value(args.get(1)).get(0).value, ScriptFileObject.get_value(args.get(2)).get(0).value);
        }
        if (Objects.equals(args.get(0), "warp_player:")){
            Boolean warped = Warp.warp_player(ScriptFileObject.get_value(args.get(1)).get(0).value, ScriptFileObject.get_value(args.get(2)).get(0).value);
        }
        if (Objects.equals(args.get(0), "minecraft.spawn_entity:")){
            Entity entity = MMORPG.Server.getWorld(ScriptFileObject.get_value(args.get(1)).get(0).value).spawnEntity(new Location(MMORPG.Server.getWorld(ScriptFileObject.get_value(args.get(1)).get(0).value), Double.valueOf(ScriptFileObject.get_value(args.get(2)).get(0).value), Double.valueOf(ScriptFileObject.get_value(args.get(3)).get(0).value), Double.valueOf(ScriptFileObject.get_value(args.get(4)).get(0).value)), EntityType.fromName(ScriptFileObject.get_value(args.get(5)).get(0).value));
            result.add(new VarObject("", "STRING", entity.getUniqueId().toString()));
        }
        if (Objects.equals(args.get(0), "minecraft.change_entity_speed:")){
            //MMORPG.Server.getEntity(UUID.fromString(get_value(args.get(1)).get(0).value)).set
        }
        if (Objects.equals(args.get(0), "minecraft.give_player_itemstack:")){
            MMORPG.Server.getPlayer(UUID.fromString(ScriptFileObject.get_value(args.get(1)).get(0).value)).getInventory().addItem(new ItemStack(Material.valueOf(ScriptFileObject.get_value(args.get(2)).get(0).value), Integer.parseInt(ScriptFileObject.get_value(args.get(3)).get(0).value)));
        }
        return result;
    }
    
}
