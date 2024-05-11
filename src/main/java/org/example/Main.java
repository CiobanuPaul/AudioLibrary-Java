package org.example;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
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
