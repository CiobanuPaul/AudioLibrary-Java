package org.example;

import com.sun.jdi.InternalException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListPlaylistsCommand extends Command {
    public ListPlaylistsCommand() {
        super(Authorization.AUTHENTICATED);
    }

    @Override
    public void execute(String[] args, Account account, Connection connection) throws NotEnoughPermissionException, InvalidArgumentsException, IllegalArgumentException, InternalException {
        if(!account.getAuthorization().hasMorePermissionThan(authorizationRequired))
            throw new NotEnoughPermissionException();
        int pageNumber;
        if(args.length == 0)
            pageNumber = 1;
        else if(args.length == 1){
            try{
                pageNumber = Integer.parseInt(args[0]);
            }
            catch(NumberFormatException e){
                throw new IllegalArgumentException("Invalid usage of command `list playlists`");
            }
        }
        else{
            System.err.println(args.length);
            System.err.println(Arrays.toString(args));
            throw new IllegalArgumentException("Invalid usage of command `list playlists`");
        }

        List<Listable> playlists = new ArrayList<>();
        try(PreparedStatement preparedStatement = connection.prepareStatement("select * from Playlist where id_account=?")){
            preparedStatement.setInt(1, account.getId_account());
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                String name = resultSet.getString("name");
                int id_playlist = resultSet.getInt("id_playlist");
                int id_account = resultSet.getInt("id_account");
                playlists.add(new Playlist(name, id_playlist, id_account));
            }
        }
        catch(SQLException e){
            throw new InternalException("There was a problem while querying the database");
        }

        String prompt = "list playlists";
        Paginator paginator = new Paginator(playlists, 2);
        paginator.listItems(prompt, pageNumber);
    }

}
