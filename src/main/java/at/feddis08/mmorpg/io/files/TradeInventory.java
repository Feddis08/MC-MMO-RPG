package at.feddis08.mmorpg.io.files;

import at.feddis08.mmorpg.MMORPG;
import at.feddis08.mmorpg.io.files.config_patterns.CheckConfigFile;
import at.feddis08.mmorpg.io.files.config_patterns.CheckDiscordFile;
import at.feddis08.mmorpg.io.files.file_objects.InventoryParser;
import at.feddis08.mmorpg.io.files.file_objects.Inventory_ConfigFileObject;
import at.feddis08.mmorpg.logic.game.Var;
import at.feddis08.mmorpg.logic.game.trade.invObjects.Inventory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TradeInventory {
    public static String path = Main.path + "Inventories/";
    public static void start() throws IOException {
        MMORPG.debugLog("Check inventory files...");
        File pathAsFile = new File(path);

        if (!Files.exists(Paths.get(path))) {
            pathAsFile.mkdir();
        }

        Integer index = 0;
        while (index < Var.config.inventories.size()){
            String inventory_filename = Var.config.inventories.get(index);
            InventoryParser inventoryParser = new InventoryParser();
            inventoryParser.parse_config_file(ReadFile.getFile(path + inventory_filename));
            inventoryParser.inventory.construct();
            Var.inventories.add(inventoryParser.inventory);
            index = index + 1;
        }
    }
}
