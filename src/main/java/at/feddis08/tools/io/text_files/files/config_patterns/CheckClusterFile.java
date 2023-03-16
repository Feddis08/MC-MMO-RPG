package at.feddis08.tools.io.text_files.files.config_patterns;

import at.feddis08.Boot;
import at.feddis08.bukkit.logic.scripts.Var;
import at.feddis08.tools.io.text_files.files.Main;
import at.feddis08.tools.io.text_files.files.ReadFile;
import at.feddis08.tools.io.text_files.files.file_objects.ClusterFileObject;
import at.feddis08.tools.io.text_files.files.file_objects.ScriptsFileObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CheckClusterFile {

    public static void check() throws IOException {
        File path2AsFile = new File(Var.path);

        if (!Files.exists(Paths.get(Var.path))) {
            path2AsFile.mkdir();
        }
        if (!Files.exists(Paths.get(Main.path + "cluster.txt"))) {
            File myObj = new File(Main.path + "cluster.txt");
            myObj.createNewFile();
            FileWriter myWriter = new FileWriter(Main.path + "cluster.txt");
            myWriter.write("# Cluster config");
            myWriter.write(System.lineSeparator());
            myWriter.write("# ---General settings---");
            myWriter.write(System.lineSeparator());
            myWriter.write("# cluster_name: ");
            myWriter.write(System.lineSeparator());
            myWriter.write("# ---For cluster-master only---");
            myWriter.write(System.lineSeparator());
            myWriter.write("# ---nodes---");
            myWriter.write(System.lineSeparator());
            myWriter.write("# add: <token>");
            myWriter.write(System.lineSeparator());
            myWriter.write("#add: 1234567890");
            myWriter.write(System.lineSeparator());
            myWriter.close();
        }
        Boot.cluster_config = new ClusterFileObject();
        Boot.cluster_config.parse_config_file(ReadFile.getFile(Main.path + "cluster.txt"));
    }

}
