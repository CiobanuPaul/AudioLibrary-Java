package org.example;

import com.sun.jdi.InternalException;

import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class RegisterCommand extends Command {

    public RegisterCommand() {
        super(Authorization.ANONYMOUS);
    }

    @Override
    public void execute(String[] args, Account account, Connection connection) throws NotEnoughPermissionException, InvalidArgumentsException, IllegalArgumentException, InternalException {
        if(account.getAuthorization() != authorizationRequired) {
            throw new NotEnoughPermissionException();
        }
        if(args.length != 2) {
            throw new IllegalArgumentException("Invalid usage of command register!");
        }

        boolean isAdmin = false;
        try (Statement statement = connection.createStatement()) {
            ResultSet response = statement.executeQuery("select * from Account");
            if (!response.next()) {
                isAdmin = true;
            }
        }
        catch (SQLException e) {
            throw new InternalException("There was an error while querying the database!");
        }

        try(PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Account WHERE username = ?")) {
            preparedStatement.setString(1, args[0]);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                throw new InvalidArgumentsException("User with given username already exists! Please try again!");
            }
        }
        catch (SQLException e) {
            throw new InternalException("There was an error while querying the database!");
        }

        String hashedPassword;
        try {
            hashedPassword = PasswordHasher.hashPassword(args[1]);
        } catch (NoSuchAlgorithmException e) {
            throw new InternalException("There was an error while processing the credentials!");
        }

        try(PreparedStatement preparedStatement = connection.prepareStatement("insert into Account(username, password, administrator) values(?, ?, ?)")){
            preparedStatement.setString(1, args[0]);
            preparedStatement.setString(2, hashedPassword);
            preparedStatement.setBoolean(3, isAdmin);
            preparedStatement.execute();
        }
        catch(SQLException e) {
            throw new InternalException("There was an error while registering the credentials!");
        }

        System.out.printf("Registered account with username %s\n", args[0]);
    }
}
