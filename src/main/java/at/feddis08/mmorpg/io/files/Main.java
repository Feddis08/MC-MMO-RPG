package at.feddis08.mmorpg.io.files;

import at.feddis08.mmorpg.io.files.config_patterns.CheckConfigFile;
import at.feddis08.mmorpg.io.files.config_patterns.CheckDiscordFile;

import java.io.File;
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
        CheckConfigFile.check();
        CheckDiscordFile.check();
    }
}
