package org.example;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Used for hashing a {@code String} using SHA-256 algorithm.
 */
public class PasswordHasher {
    /**
     * Returns a hash for a {@code String}, usually a password, using SHA-256 algorithm.
     * @param password
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(password.getBytes());
        byte[] hashedBytes = md.digest();
        String hashedPassword = Base64.getEncoder().encodeToString(hashedBytes);
        return hashedPassword;
    }
}
