package org.example;

import com.sun.jdi.InternalException;

import java.sql.Connection;

public abstract class Command implements Executable{
    protected Authorization authorizationRequired;

    protected Command(Authorization authorizationRequired) {
        this.authorizationRequired = authorizationRequired;
    }
    public Authorization getAuthorizationRequired() {
        return authorizationRequired;
    }
    public abstract void execute(String[] args, Account account, Connection connection)
            throws NotEnoughPermissionException, InvalidArgumentsException, IllegalArgumentException, InternalException;

//    public boolean hasMorePermissionThan(Command cmd) {
//        return authorization.getPermission() > cmd.getAuthorization().getPermission();
//    }

}
