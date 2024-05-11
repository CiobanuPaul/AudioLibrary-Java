package org.example;

import com.sun.jdi.InternalException;

import javax.lang.model.element.ExecutableElement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CreatePlaylistCommand extends Command {
    public CreatePlaylistCommand() {
        super(Authorization.AUTHENTICATED);
    }

    @Override
    public void execute(String[] args, Account account, Connection connection) throws NotEnoughPermissionException, InvalidArgumentsException, IllegalArgumentException, InternalException {
        if(!account.getAuthorization().hasMorePermissionThan(authorizationRequired))
            throw new NotEnoughPermissionException();
        if(args.length != 1)
            throw new IllegalArgumentException("Invalid usage of command create playlist");

        try(PreparedStatement preparedStatement = connection.prepareStatement("select * from Playlist where id_account = ? and name = ?")){
            preparedStatement.setInt(1, account.getId_account());
            preparedStatement.setString(2, args[0]);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                throw new InvalidArgumentsException("You already hava a playlist named " + args[0]);
            }
        }
        catch(SQLException e) {
            throw new InternalException("There was a problem querying the database");
        }

        try(PreparedStatement preparedStatement = connection.prepareStatement("insert into Playlist(id_account, name) values(?, ?)")){
            preparedStatement.setInt(1, account.getId_account());
            preparedStatement.setString(2, args[0]);
            preparedStatement.executeUpdate();
        }
        catch (SQLException e){
            throw new InternalException("There was a problem querying the database");
        }
        System.out.println("Playlist " + args[0] + " was created successfully!");
    }
}
