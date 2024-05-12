package org.example;

/**
 * Exception thrown when certain arguments respect parsing restrictions, but are not valid or existing.
 * <p>Example: invalid credentials on login.</p>
 */
public class InvalidArgumentsException extends Exception{
    public InvalidArgumentsException(String message) {
        super(message);
    }
}
