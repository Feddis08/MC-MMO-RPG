package at.feddis08.mmorpg.logic.game;

import at.feddis08.mmorpg.io.files.file_objects.Discord_ConfigFileObject;
import at.feddis08.mmorpg.io.files.file_objects.Inventory_ConfigFileObject;
import at.feddis08.mmorpg.logic.game.trade.invObjects.Inventory;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Objects;

public class Var {
    public static Inventory_ConfigFileObject config = new Inventory_ConfigFileObject();
    public static ArrayList<Inventory> inventories = new ArrayList<>();

    public static Inventory get_inventory_by_display_name(String display_name){
        Integer index = 0;
        Inventory result = null;
        while (index < inventories.size()){
            if (Objects.equals(inventories.get(index).display_name, display_name))
                result = inventories.get(index);
            index = index + 1;
        }
        return result;
    }

}
