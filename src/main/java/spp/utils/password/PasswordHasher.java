package spp.utils.password;


import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;


public class PasswordHasher {

    public String hashPassword(String password) {
        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
        char[] passwordChars = password.toCharArray();
        try {
            return argon2.hash(2, 65536, 1, passwordChars);
        } finally {
            argon2.wipeArray(passwordChars);
        }

    }

    public boolean verifyPassword(String hash, String password) {
        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
        return argon2.verify(hash, password.toCharArray());

    }

}
