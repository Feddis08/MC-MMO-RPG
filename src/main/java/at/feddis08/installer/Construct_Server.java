package at.feddis08.installer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Construct_Server {
    private static String path = Start.absolute_path + "/MMORPG/";
    public static void start() throws IOException {
        File pathAsFile = new File(path);

        if (!Files.exists(Paths.get(path))) {
            pathAsFile.mkdir();
        }
        at.feddis08.mmorpg.io.url_files.File.get_from_url("https://api.papermc.io/v2/projects/paper/versions/1.19/builds/81/downloads/paper-1.19-81.jar", "paper_1.19.jar", path);
    }
}
