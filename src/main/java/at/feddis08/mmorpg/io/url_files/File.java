package at.feddis08.mmorpg.io.url_files;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class File {
    public static void get_from_url(String url, String file_name, String path) throws IOException {
        System.out.println("Downloading file: " + url + " " + path + file_name);
        InputStream in = new URL(url).openStream();
        Files.copy(in, Paths.get(path + file_name), StandardCopyOption.REPLACE_EXISTING);
    }
}
