package at.feddis08.tools.io.text_files.files.config_patterns;

import at.feddis08.tools.io.text_files.files.Main;
import at.feddis08.tools.io.text_files.files.ReadFile;
import at.feddis08.tools.io.text_files.files.file_objects.ScriptsFileObject;
import at.feddis08.bukkit.logic.scripts.Var;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CheckScriptsFile {

    public static void check() throws IOException {
        File path2AsFile = new File(Var.path);

        if (!Files.exists(Paths.get(Var.path))) {
            path2AsFile.mkdir();
        }
        if (!Files.exists(Paths.get(Main.path + "scripts.txt"))) {
            File myObj = new File(Main.path + "scripts.txt");
            myObj.createNewFile();
            FileWriter myWriter = new FileWriter(Main.path + "scripts.txt");
            myWriter.write("# This is the scripts file of the MMORPG!");
            myWriter.write(System.lineSeparator());
            myWriter.write("# ---Initialization of scripts---");
            myWriter.write(System.lineSeparator());
            myWriter.write("# add: <file_name>");
            myWriter.write(System.lineSeparator());
            myWriter.write("#add: basic_scripts.txt");
            myWriter.write(System.lineSeparator());
            myWriter.close();
        }
        Var.config = new ScriptsFileObject();
        Var.config.parse_config_file(ReadFile.getFile(Main.path + "scripts.txt"));
    }

}
