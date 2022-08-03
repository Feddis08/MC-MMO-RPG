package at.feddis08.mmorpg.io.files.config_patterns;

import at.feddis08.mmorpg.MMORPG;
import at.feddis08.mmorpg.io.files.Main;
import at.feddis08.mmorpg.io.files.ReadFile;
import at.feddis08.mmorpg.io.files.file_objects.ConfigFileObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CheckConfigFile {
    public static void check() throws IOException {
        if (!Files.exists(Paths.get(Main.path + "config.txt"))) {
            File myObj = new File(Main.path + "config.txt");
            myObj.createNewFile();
            FileWriter myWriter = new FileWriter(Main.path + "config.txt");
            myWriter.write("# This is the config file of the MMORPG!");
            myWriter.write(System.lineSeparator());
            myWriter.write("# ---General settings---");
            myWriter.write(System.lineSeparator());
            myWriter.write("console_prefix: MMO-RPG");
            myWriter.write(System.lineSeparator());
            myWriter.write("enable_debug_log: true");
            myWriter.write(System.lineSeparator());
            myWriter.write("# ---database settings---");
            myWriter.write(System.lineSeparator());
            myWriter.write("database_ip: localhost");
            myWriter.write(System.lineSeparator());
            myWriter.write("database_port: 3306");
            myWriter.write(System.lineSeparator());
            myWriter.write("database_database_name: MMORPG");
            myWriter.write(System.lineSeparator());
            myWriter.write("database_username: MMORPG");
            myWriter.write(System.lineSeparator());
            myWriter.write("database_password: database_password");
            myWriter.write(System.lineSeparator());
            myWriter.write("# ---Discord_bot settings---");
            myWriter.write(System.lineSeparator());
            myWriter.write("enable_discord_bot: false");
            myWriter.write(System.lineSeparator());
            myWriter.write("discord_bot_token: null");
            myWriter.write(System.lineSeparator());
            myWriter.close();
        }
        MMORPG.config = new ConfigFileObject();
        MMORPG.config.parse_config_file(ReadFile.getFile(Main.path + "config.txt"));
    }
}
