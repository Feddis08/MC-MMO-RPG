package at.feddis08.mmorpg.io.files;

import at.feddis08.mmorpg.MMORPG;
import at.feddis08.mmorpg.io.files.file_objects.InventoryParser;
import at.feddis08.mmorpg.io.files.file_objects.TradeTable_Config_FileObject;
import at.feddis08.mmorpg.logic.game.Var;

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
        index = 0;
        while (index < Var.config.trade_tables.size()){
            String trade_table_filename = Var.config.trade_tables.get(index);
            TradeTable_Config_FileObject tradeTable_Config_File = new TradeTable_Config_FileObject();
            tradeTable_Config_File.parse_config_file(ReadFile.getFile(path + trade_table_filename));
            Var.tradeTables.add(tradeTable_Config_File);
            index = index + 1;
        }
    }
}
