package org.example;

import com.sun.jdi.InternalException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CreateSongCommand extends Command {
    public CreateSongCommand() {
        super(Authorization.ADMINISTRATOR);
    }


    @Override
    public void execute(String[] args, Account account, Connection connection) throws NotEnoughPermissionException, InvalidArgumentsException, IllegalArgumentException, InternalException {
        if(!account.getAuthorization().hasMorePermissionThan(authorizationRequired))
            throw new NotEnoughPermissionException();
        if(args.length != 3){
            throw new IllegalArgumentException("Invalid usage of command create song");
        }

        String name = args[0];
        String author = args[1];
        int year;
        try{
            year = Integer.parseInt(args[2]);
        }
        catch(NumberFormatException e){
            throw new IllegalArgumentException("Invalid usage of command create song");
        }

        try(PreparedStatement preparedStatement = connection.prepareStatement("select * from Melody where name=? and author=?")){
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, author);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                throw new InvalidArgumentsException("This song is already part of the library!");
            }
        } catch (SQLException e) {
            throw new InternalException("There was a problem while querying the database");
        }

        try(PreparedStatement preparedStatement = connection.prepareStatement("insert into Melody(name, author, year) values(?, ?, ?)")){
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, author);
            preparedStatement.setInt(3, year);
            preparedStatement.executeUpdate();
        }
        catch(SQLException e){
            throw new InternalException("There was a problem while querying the database");
        }
        System.out.printf("Added %s by %s to the library\n", name, author);
    }
}
