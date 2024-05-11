package org.example;

import com.sun.jdi.InternalException;

import java.sql.*;

public class PromoteCommand extends Command {
    public PromoteCommand() {
        super(Authorization.ADMINISTRATOR);
    }

    @Override
    public void execute(String[] args, Account account, Connection connection) throws NotEnoughPermissionException, InvalidArgumentsException, IllegalArgumentException, InternalException {
        if(!account.getAuthorization().hasMorePermissionThan(authorizationRequired)) {
            throw new NotEnoughPermissionException();
        }
        if(args.length != 1)
            throw new IllegalArgumentException("Invalid usage of command promote!");

        String username = args[0];
        try(PreparedStatement preparedStatement = connection.prepareStatement("select * from Account where username=?")){
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(!resultSet.next()){
                throw new InvalidArgumentsException("Specified user does not exist!");
            }
        }
        catch(SQLException e){
            throw new InternalException("There was a problem while querying the database");
        }

        try(PreparedStatement preparedStatement = connection.prepareStatement("update Account set administrator=true where username=?")){
            preparedStatement.setString(1, username);
            preparedStatement.execute();
        }
        catch(SQLException e){
            throw new InternalException("There was a problem while querying the database");
        }
        System.out.printf("%s is now an administrator!\n", username);
    }
}
