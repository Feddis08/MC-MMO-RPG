package at.feddis08.tools.io.text_files.files;

import at.feddis08.tools.io.text_files.files.config_patterns.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static String path = "./MMORPG/";
    public static void start() throws IOException {
        System.out.println("Check general config files...");
        File pathAsFile = new File(path);

        if (!Files.exists(Paths.get(path))) {
            pathAsFile.mkdir();
        }
        CheckConfigFile.check();
        CheckDiscordFile.check();
        CheckInventory_ConfigFile.check();
        TradeInventory.start();
        CheckScriptsFile.check();
        CheckClusterFile.check();
    }
}
