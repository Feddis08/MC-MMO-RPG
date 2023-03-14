package at.feddis08.tools.io.text_files.files.config_patterns;

import at.feddis08.tools.io.text_files.files.Main;
import at.feddis08.tools.io.text_files.files.ReadFile;
import at.feddis08.tools.io.text_files.files.file_objects.Inventory_ConfigFileObject;
import at.feddis08.bukkit.logic.game.Var;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CheckInventory_ConfigFile {
    public static void check() throws IOException {
        if (!Files.exists(Paths.get(Main.path + "inventory_config.txt"))) {
            File myObj = new File(Main.path + "inventory_config.txt");
            myObj.createNewFile();
            FileWriter myWriter = new FileWriter(Main.path + "inventory_config.txt");
            myWriter.write("# This is the inventory config file of the MMORPG!");
            myWriter.write(System.lineSeparator());
            myWriter.write("# add_inv a inventory: add_inv: <file_name>");
            myWriter.write(System.lineSeparator());
            myWriter.write("# add_trade a inventory: add_trade: <file_name>");
            myWriter.write(System.lineSeparator());
            myWriter.write("# add_inv: wheat_trade_table.txt");
            myWriter.write(System.lineSeparator());
            myWriter.close();
        }
        Var.config = new Inventory_ConfigFileObject();
        Var.config.parse_config_file(ReadFile.getFile(Main.path + "inventory_config.txt"));
    }

}
