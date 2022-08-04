package at.feddis08.mmorpg.io.files.config_patterns;

import at.feddis08.mmorpg.discord.DISCORD;
import at.feddis08.mmorpg.io.files.Main;
import at.feddis08.mmorpg.io.files.ReadFile;
import at.feddis08.mmorpg.io.files.file_objects.Discord_ConfigFileObject;
import at.feddis08.mmorpg.io.files.file_objects.Inventory_ConfigFileObject;
import at.feddis08.mmorpg.logic.game.Var;

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
