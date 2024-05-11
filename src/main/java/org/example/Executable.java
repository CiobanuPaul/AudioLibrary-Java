package org.example;

import com.sun.jdi.InternalException;

import java.sql.Connection;

public interface Executable {
    void execute(String[] args, Account account, Connection connection)
            throws NotEnoughPermissionException, InvalidArgumentsException, IllegalArgumentException, InternalException;
}
