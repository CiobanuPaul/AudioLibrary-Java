package org.example;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    public static void showHelp(){
        System.out.println("Welcome to AudioLibrary!");
        System.out.println();
        System.out.println("First you need to login/register using the command:");
        System.out.println("login <username> <password>");
        System.out.println("or");
        System.out.println("register <username> <password>");
        System.out.println();
        System.out.println("After authenticating, you can use the following commands:");
        System.out.println("logout");
        System.out.println("list playlists");
        System.out.println("add byName \"<playlistName>\" <melodyId>[ <melodyId2> <melodyId3> ...]");
        System.out.println("add byId <playlistId> <melodyId>[ <melodyId2> <melodyId3> ...]");
        System.out.println("search name \"<textToSearch>\"");
        System.out.println("search author \"<textToSearch>\"");
        System.out.println("export playlist <playlistName> <format>   (format can be csv or json or txt)");
        System.out.println("export playlist <playlistId> <format>   (format can be csv or json or txt)");
        System.out.println();
        System.out.println("If you have administrator rights than you can use the following commands:");
        System.out.println("promote <username>");
        System.out.println("create song \"<name>\" \"<author>\" <year>");
        System.out.println("audit <username>");
        System.out.println();
        System.out.println("Enjoy the app!");
    }


    public static void main(String[] args) {
        showHelp();
        Account account = new Account();
        Dotenv dotenv = Dotenv.configure().load();
        try (Connection connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/AudioLibrary", dotenv.get("user"), dotenv.get("pass"))) {
            CommandProcessor commandProcessor = CommandProcessor.getInstance();
            while (true) {
                Scanner scanner = new Scanner(System.in);
                String input = scanner.nextLine();
                boolean success = commandProcessor.processAndExecute(input, account, connection);
                Auditor.audit(input, account, connection, success);
            }
        }
        catch (SQLException e) {
            System.out.println("There was a problem connecting to the database");
        }
    }
}
