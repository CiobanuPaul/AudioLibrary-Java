package org.example;

import com.sun.jdi.InternalException;

import java.security.InvalidParameterException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddToPlaylistCommand extends Command {
    public AddToPlaylistCommand() {
        super(Authorization.AUTHENTICATED);
    }
    private void addById(String[] args, Account account, Connection connection, int id_playlist) throws InvalidArgumentsException {
        List<Integer> melodiesIds = new ArrayList<>();
        for(int i = 2; i < args.length; i++) {
            try {
                melodiesIds.add(Integer.parseInt(args[i]));
            }
            catch(NumberFormatException e){
                throw new IllegalArgumentException("Invalid usage of command add");
            }
        }

        Playlist playlist = null;
        try(PreparedStatement preparedStatement = connection.prepareStatement("select * from Playlist where id_account=? and id_playlist=?")){
            preparedStatement.setInt(1, account.getId_account());
            preparedStatement.setInt(2, id_playlist);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(!resultSet.next()){
                throw new InvalidArgumentsException("The desired playlist does not exist.");
            }
            else{
                String name = resultSet.getString("name");
                playlist = new Playlist(name, id_playlist, account.getId_account());
            }
        }
        catch(SQLException e){
            throw new InternalException("There was a problem while querying the database.");
        }

        StringBuilder errorPrompt = new StringBuilder();
        Map<Integer, Melody> melodyMap = new HashMap<>();
        for(int id_melody:melodiesIds){
            try(PreparedStatement preparedStatement = connection.prepareStatement("select * from Melody where id_melody=?")){
                preparedStatement.setInt(1, id_melody);
                ResultSet resultSet = preparedStatement.executeQuery();
                if(!resultSet.next())
                    errorPrompt.append("Song with identifier " + id_melody + " does not exist.\n");
                else{
                    String name = resultSet.getString("name");
                    String author = resultSet.getString("author");
                    int year = resultSet.getInt("year");
                    Melody melody = new Melody(name, author, year, id_melody);
                    melodyMap.put(id_melody, melody);
                    try(PreparedStatement preparedStatement1 = connection.prepareStatement("select * from Playlist_Melody where id_melody=? and id_playlist=?")){
                        preparedStatement1.setInt(1, id_melody);
                        preparedStatement1.setInt(2, id_playlist);
                        ResultSet resultSet1 = preparedStatement1.executeQuery();
                        if(resultSet1.next())
                            errorPrompt.append("The song " + name + " by " + author + " is already part of " + playlist.getName() + ".\n");
                    }
                    catch (SQLException e){
                        throw  new InternalException("There was a problem while querying the database.");
                    }
                }
            }
            catch (SQLException e){
                throw  new InternalException("There was a problem while querying the database.");
            }
        }
        if(!errorPrompt.isEmpty()){
            throw new InvalidArgumentsException(errorPrompt.toString());
        }

        for(int id_melody:melodiesIds){
            Melody melody = melodyMap.get(id_melody);
            try(PreparedStatement preparedStatement = connection.prepareStatement("insert into Playlist_Melody(id_playlist, id_melody) values(?, ?)")){
                preparedStatement.setInt(1, id_playlist);
                preparedStatement.setInt(2, id_melody);
                preparedStatement.executeQuery();
                System.out.println("Added " + melody.getName() + " by " + melody.getAuthor() + " to " + playlist.getName());
            }
            catch (SQLException e) {
                throw new InternalException("There was a problem while querying the database.");
            }
        }
    }


    @Override
    public void execute(String[] args, Account account, Connection connection) throws NotEnoughPermissionException, InvalidArgumentsException, IllegalArgumentException, InternalException {
        if(!account.getAuthorization().hasMorePermissionThan(authorizationRequired))
            throw new NotEnoughPermissionException();
        if(args.length < 3)
            throw new IllegalArgumentException("Invalid usage of command add");
        String byMethod = args[0];
        if(byMethod.equals("byId")) {
            int id_playlist;
            try{
                id_playlist = Integer.parseInt(args[1]);
            }
            catch(NumberFormatException e){
                throw new IllegalArgumentException("Invalid usage of command add");
            }
            addById(args, account, connection, id_playlist);
        }
        else if(byMethod.equals("byName")) {
            String name = args[1];
            int id_playlist;
            try(PreparedStatement preparedStatement = connection.prepareStatement("select id_playlist from Playlist where name=? and id_account=?")){
                preparedStatement.setString(1, name);
                preparedStatement.setInt(2, account.getId_account());
                ResultSet resultSet = preparedStatement.executeQuery();
                if(resultSet.next()) {
                    id_playlist = resultSet.getInt("id_playlist");
                }
                else{
                    throw new InvalidArgumentsException("The desired playlist does not exist.");
                }
            }
            catch (SQLException e){
                throw new InternalException("There was a problem while querying the database.");
            }
            addById(args, account, connection, id_playlist);
        }
        else {
            throw new IllegalArgumentException("Invalid usage of command add");
        }


    }
}
