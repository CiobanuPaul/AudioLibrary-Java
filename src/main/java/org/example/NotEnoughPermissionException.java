package org.example;

public class NotEnoughPermissionException extends Exception {
    public NotEnoughPermissionException(String message) {
        super(message);
    }
    public NotEnoughPermissionException(String message, Throwable cause) {
        super(message, cause);
    }
    public NotEnoughPermissionException(Throwable cause) {
        super(cause);
    }
    public NotEnoughPermissionException(){
        super("You don't have enough permissions to do this");
    }
}
