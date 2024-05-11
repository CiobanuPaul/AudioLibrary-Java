package org.example;

import com.sun.jdi.InternalException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.security.NoSuchAlgorithmException;

public class LoginCommand extends Command {

    public LoginCommand() {
        super(Authorization.ANONYMOUS);
    }

    @Override
    public void execute(String[] args, Account account, Connection connection) throws NotEnoughPermissionException, InvalidArgumentsException, IllegalArgumentException, InternalException {
        if(account.getAuthorization() != authorizationRequired) {
            throw new NotEnoughPermissionException();
        }
        if(args.length != 2) {
            throw new IllegalArgumentException("Invalid usage of command login!");
        }
        String hashedPassword;
        try {
            hashedPassword = PasswordHasher.hashPassword(args[1]);
        } catch (NoSuchAlgorithmException e) {
            throw new InternalException("There was an error while processing the credentials!");
        }
        try(PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Account WHERE username = ? and password = ?")) {
            preparedStatement.setString(1, args[0]);
            preparedStatement.setString(2, hashedPassword);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                int id_account = resultSet.getInt("id_account");
                String username = resultSet.getString("username");
                boolean admin = resultSet.getBoolean("administrator");
                Authorization authorization = admin ? Authorization.ADMINISTRATOR : Authorization.AUTHENTICATED;
                account.setAuthenticatedInfo(id_account, username, authorization);
                System.out.printf("You are now authenticated as %s!\n", username);
            }
            else{
                throw new InvalidArgumentsException("Username or password is invalid. Please try again!");
            }
        }
        catch(SQLException e) {
            throw new InternalException("There was an error while logging in!");
        }
    }
}
