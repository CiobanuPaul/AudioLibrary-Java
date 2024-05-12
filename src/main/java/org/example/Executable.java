package org.example;

import com.sun.jdi.InternalException;

import java.sql.Connection;

/**
 * Implements {@code execute(String[] args, Account account, Connection connection)} function.
 */
public interface Executable {

    /**
     * The execute function receives arguments and uses them to execute certain actions using an account and a connection.
     * @param args
     * @param account
     * @param connection
     * @throws NotEnoughPermissionException
     * @throws InvalidArgumentsException
     * @throws IllegalArgumentException
     * @throws InternalException
     */
    void execute(String[] args, Account account, Connection connection)
            throws NotEnoughPermissionException, InvalidArgumentsException, IllegalArgumentException, InternalException;
}
