package at.feddis08.installer;

import java.io.IOException;
import java.util.Scanner;

public class Start {
    public static String absolute_path = "";
    public static String absolute_path_java = "";
    public static void main(String[] args) throws IOException {
        String[] commands;
        Scanner input = new Scanner(System.in);
        System.out.println("");
        System.out.println("The MMO-RPG installer.");
        System.out.println("");
        System.out.println("");
        System.out.println("Set the absolute path of the MMO-RPG: ");
        String answer = input.nextLine();
        commands = answer.split(" ");
        absolute_path = commands[0];
        System.out.println(absolute_path);
        System.out.println("");


        System.out.println("Set the absolute path of your java: ");
        answer = input.nextLine();
        commands = answer.split(" ");
        absolute_path_java = commands[0];
        System.out.println(absolute_path_java);

        System.out.println("");
        System.out.println("Done.");
        System.out.println("Start construction...");

        Construct_Server.start();
    }
}
