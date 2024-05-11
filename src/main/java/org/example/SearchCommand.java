package org.example;

import com.sun.jdi.InternalException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SearchCommand extends Command {
    public SearchCommand() {
        super(Authorization.AUTHENTICATED);
    }

    @Override
    public void execute(String[] args, Account account, Connection connection) throws NotEnoughPermissionException, InvalidArgumentsException, IllegalArgumentException, InternalException {
        if(!account.getAuthorization().hasMorePermissionThan(authorizationRequired))
            throw new NotEnoughPermissionException();
        int pageNumber;
        if(args.length == 2){
            pageNumber = 1;
        }
        else if(args.length == 3){
            try {
                pageNumber = Integer.parseInt(args[2]);
            }
            catch(NumberFormatException e){
                throw new IllegalArgumentException("Invalid usage of search command");
            }
        }
        else{
            throw new IllegalArgumentException("Invalid usage of search command");
        }

        String criteria = args[0];
        if(!criteria.equals("author") && !criteria.equals("name")){
            throw new IllegalArgumentException("Invalid usage of search command");
        }

        String toSearch = args[1];
        toSearch = "%" + toSearch + "%";
        List<Listable> melodies = new ArrayList<>();
        String sql = "select * from Melody where " + criteria + " like ?";
        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, toSearch);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                String name = rs.getString("name");
                String author = rs.getString("author");
                int year = rs.getInt("year");
                int id_melody = rs.getInt("id_melody");
                Melody melody = new Melody(name, author, year, id_melody);
                melodies.add(melody);

            }
        }
        catch(SQLException e){
            throw new InternalException("There was a problem while querying the database.");
        }

        String prompt = "search " + criteria + " " + args[1];
        Paginator paginator = new Paginator(melodies, 2);
        paginator.listItems(prompt, pageNumber);
    }
}
