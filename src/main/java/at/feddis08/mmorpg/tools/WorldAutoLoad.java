package at.feddis08.mmorpg.tools;

import at.feddis08.mmorpg.database.Functions;
import at.feddis08.mmorpg.database.objects.WorldObject;

import java.sql.SQLException;
import java.util.ArrayList;

public class WorldAutoLoad {
    public static void checkAutoloadWorlds() throws SQLException {
        ArrayList<WorldObject> worlds = Functions.getWorlds();
        Integer index = 0;
        while (index < worlds.size()){
            if (worlds.get(index).autoload.equals("1")){
                StartLoadWorld.loadWorld(worlds.get(index).name);
            }
            index = index + 1;
        }
    }
}
