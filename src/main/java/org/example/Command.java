package org.example;

import com.sun.jdi.InternalException;

import java.sql.Connection;

/**
 * Command which can be executed with several arguments received.
 */
public abstract class Command implements Executable{
    protected Authorization authorizationRequired;

    protected Command(Authorization authorizationRequired) {
        this.authorizationRequired = authorizationRequired;
    }

    /**
     *
     * @return The authorization required to execute the command.
     */
    public Authorization getAuthorizationRequired() {
        return authorizationRequired;
    }
    public abstract void execute(String[] args, Account account, Connection connection)
            throws NotEnoughPermissionException, InvalidArgumentsException, IllegalArgumentException, InternalException;


}
