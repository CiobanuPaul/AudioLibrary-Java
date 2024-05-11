package org.example;

import com.sun.jdi.InternalException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AuditCommand extends Command {
    public AuditCommand() {
        super(Authorization.AUTHENTICATED);
    }


    @Override
    public void execute(String[] args, Account account, Connection connection) throws NotEnoughPermissionException, InvalidArgumentsException, IllegalArgumentException, InternalException {
        if(!account.getAuthorization().hasMorePermissionThan(authorizationRequired))
            throw new NotEnoughPermissionException();
        int pageNumber;
        if(args.length == 1)
            pageNumber = 1;
        else if(args.length == 2){
            try{
                pageNumber = Integer.parseInt(args[1]);
            }
            catch(NumberFormatException e){
                throw new IllegalArgumentException("Invalid usage of command audit");
            }
        }
        else {
            throw new IllegalArgumentException("Invalid usage of command audit!");
        }
        String name = args[0];
        int id_account;
        try(PreparedStatement preparedStatement = connection.prepareStatement("select * from Account where username=?")){
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(!resultSet.next())
                throw new InvalidArgumentsException("User " + name + " does not exist");
            id_account = resultSet.getInt("id_account");
        }
        catch(SQLException e){
            throw new InternalException("There was a problem querying the database");
        }

        List<Listable> auditEntries = new ArrayList<>();
        try(PreparedStatement preparedStatement = connection.prepareStatement("select * from Audit where id_account=?")){
            preparedStatement.setInt(1, id_account);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                String command = resultSet.getString("command");
                Date date = resultSet.getDate("date");
                boolean success = resultSet.getBoolean("success");
                auditEntries.add(new AuditEntry(id_account, command, date, success));
            }
        }
        catch(SQLException e){
            throw new InternalException("There was a problem querying the database");
        }

        String prompt = "audit " + args[0];
        Paginator paginator = new Paginator(auditEntries, 2);
        paginator.listItems(prompt, pageNumber);
    }
}
