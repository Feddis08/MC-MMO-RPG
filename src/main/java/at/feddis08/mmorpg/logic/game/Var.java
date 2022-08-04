package at.feddis08.mmorpg.logic.game;

import at.feddis08.mmorpg.io.files.file_objects.Inventory_ConfigFileObject;
import at.feddis08.mmorpg.io.files.file_objects.TradeTable_Config_FileObject;
import at.feddis08.mmorpg.logic.game.trade.invObjects.Inventory;
import at.feddis08.mmorpg.logic.game.trade.invObjects.PlayerInvObject;
import at.feddis08.mmorpg.logic.game.trade.invObjects.Slot;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Objects;

public class Var {
    public static Inventory_ConfigFileObject config = new Inventory_ConfigFileObject();
    public static ArrayList<Inventory> inventories = new ArrayList<>();
    public static ArrayList<TradeTable_Config_FileObject> tradeTables = new ArrayList<>();
    public static ArrayList<PlayerInvObject> playerInvObjects = new ArrayList<>();

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
    public static TradeTable_Config_FileObject get_TradeTable_Config_FileObject_by_inventory_display_name(String display_name){
        Integer index = 0;
        TradeTable_Config_FileObject result = null;
        while (index < tradeTables.size()){
            if (Objects.equals(tradeTables.get(index).inventory_display_name, display_name))
                result = tradeTables.get(index);
            index = index + 1;
        }
        return result;
    }
    public static Slot get_inventory_slot_by_index(Inventory inventory2, Integer index2){
        Integer index = 0;
        Slot result = null;
        while (index < inventory2.slots.size()){
            if (Objects.equals(inventory2.slots.get(index).index, index2))
                result = inventory2.slots.get(index);
            index = index + 1;
        }
        return result;
    }
    public static PlayerInvObject get_Player_inv_object_by_player_id(String player_id){
        Integer index = 0;
        PlayerInvObject result = null;
        while (index < playerInvObjects.size()){
            if (Objects.equals(playerInvObjects.get(index).player_id, player_id))
                result = playerInvObjects.get(index);
            index = index + 1;
        }
        return result;
    }

}
