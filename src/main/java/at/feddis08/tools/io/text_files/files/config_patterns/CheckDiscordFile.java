package at.feddis08.tools.io.text_files.files.config_patterns;

import at.feddis08.tools.discord.DISCORD;
import at.feddis08.tools.io.text_files.files.Main;
import at.feddis08.tools.io.text_files.files.ReadFile;
import at.feddis08.tools.io.text_files.files.file_objects.Discord_ConfigFileObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CheckDiscordFile {

    public static void check() throws IOException {
        if (!Files.exists(Paths.get(Main.path + "discord_config.txt"))) {
            File myObj = new File(Main.path + "discord_config.txt");
            myObj.createNewFile();
            FileWriter myWriter = new FileWriter(Main.path + "discord_config.txt");
            myWriter.write("# This is the discord_bot config file of the MMORPG!");
            myWriter.write(System.lineSeparator());
            myWriter.write("# ---General settings---");
            myWriter.write(System.lineSeparator());
            myWriter.write("# enter the discord channel id");
            myWriter.write(System.lineSeparator());
            myWriter.write("server_log: ");
            myWriter.write(System.lineSeparator());
            myWriter.write("# the chats where the bridge between minecraft and discord is.");
            myWriter.write(System.lineSeparator());
            myWriter.write("read_only_chat: ");
            myWriter.write(System.lineSeparator());
            myWriter.write("chat:  ");
            myWriter.write(System.lineSeparator());
            myWriter.close();
        }
        DISCORD.config = new Discord_ConfigFileObject();
        DISCORD.config.parse_config_file(ReadFile.getFile(Main.path + "discord_config.txt"));
    }

}
