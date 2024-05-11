package org.example;

import com.sun.jdi.InternalException;

import java.sql.Connection;

public class LogoutCommand extends Command {

    public LogoutCommand() {
        super(Authorization.AUTHENTICATED);
    }

    @Override
    public void execute(String[] args, Account account, Connection connection) throws NotEnoughPermissionException, InvalidArgumentsException, IllegalArgumentException, InternalException {
        if(!account.getAuthorization().hasMorePermissionThan(authorizationRequired)){
            throw new NotEnoughPermissionException();
        }
        if(args.length > 0){
            throw new IllegalArgumentException("Invalid usage of command logout!");
        }
        account.resetInfo();
        System.out.println("Successfully logged out.");
    }
}
