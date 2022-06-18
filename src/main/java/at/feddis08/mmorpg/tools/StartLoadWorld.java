package at.feddis08.mmorpg.tools;

import at.feddis08.mmorpg.MMORPG;
import at.feddis08.mmorpg.commands.LoadWorld;
import at.feddis08.mmorpg.database.Functions;
import at.feddis08.mmorpg.database.objects.WorldObject;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;

import java.sql.SQLException;

public class StartLoadWorld implements Runnable{
    public static String world_id;
    public static void loadWorld(String world_id){
        Thread t = new Thread("loadWorld");
        StartLoadWorld.world_id = world_id;
        StartLoadWorld.startLoadWorld(world_id);
        t.start();
    }
    @Override
    public void run() {
        startLoadWorld(world_id);
        return;
    }
    public static void startLoadWorld(String world_id){
        WorldCreator wc = new WorldCreator(world_id);
        wc.environment(World.Environment.NORMAL);
        wc.type(WorldType.NORMAL);
        MMORPG.consoleLog("Loading world: " + world_id);
        wc.createWorld();
        MMORPG.consoleLog("World load complete");
        WorldObject dbWorld = null;
        try {
            dbWorld = Functions.getWorld("name", world_id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (dbWorld.id == null) {
            dbWorld = new WorldObject();
            dbWorld.loaded = "true";
            dbWorld.id = world_id;
            dbWorld.name = world_id;
            try {
                Functions.createWorld(dbWorld);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            dbWorld.players_on = dbWorld.players_on + 1;
            dbWorld.loaded = "true";
            try {
                Functions.update("worlds", "players_on", dbWorld.players_on.toString(), dbWorld.id, "id");
                Functions.update("worlds", "loaded", dbWorld.loaded, dbWorld.id, "id");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
