package at.feddis08.tools.io.text_files.files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class ReadFile {
    public static ArrayList<String> getFile(String path) throws IOException {
        File file = new File(path);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String st;
        ArrayList<String> st1 = new ArrayList<>();
        while ((st = br.readLine()) != null) Objects.requireNonNull(st1).add(st);
        return st1;
    }
}
