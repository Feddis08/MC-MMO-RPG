package at.feddis08.mmorpg.io.files;

import at.feddis08.mmorpg.MMORPG;
import at.feddis08.mmorpg.io.files.objects.ConfigFileObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static String path = "./MMORPG/";
    public static void start() throws IOException {
        System.out.println("Check files ...");
        File pathAsFile = new File(path);

        if (!Files.exists(Paths.get(path))) {
            pathAsFile.mkdir();
        }
        if (!Files.exists(Paths.get(path + "config.txt"))) {
            File myObj = new File(path + "config.txt");
            myObj.createNewFile();
            FileWriter myWriter = new FileWriter(path + "config.txt");
            myWriter.write("# This is the config file of the MMORPG!");
            myWriter.write(System.lineSeparator());
            myWriter.write("# ---General settings---");
            myWriter.write(System.lineSeparator());
            myWriter.write("console_prefix: MMO-RPG: ");
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
        MMORPG.config.parse_config_file(ReadFile.getFile(path + "config.txt"));
    }
}
