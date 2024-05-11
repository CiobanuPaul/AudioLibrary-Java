package org.example;

public enum Authorization {
    ANONYMOUS(0),
    AUTHENTICATED(1),
    ADMINISTRATOR(2);

    private final int permission;
    Authorization(int permission) {
        this.permission = permission;
    }



    public int getPermission() {
        return permission;
    }

    public boolean hasMorePermissionThan(Authorization other) {
        return permission >= other.permission;
    }

}
