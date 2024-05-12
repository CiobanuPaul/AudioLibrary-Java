package org.example;

/**
 * Information about different levels of authorization.
 */
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

    /**
     *Returns {@code true} if {@code this} has the {@code permission} attribute greater or equal to {@code other.permission}
     * @param other
     * @return
     */
    public boolean hasMorePermissionThan(Authorization other) {
        return permission >= other.permission;
    }

}
