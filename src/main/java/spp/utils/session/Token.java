package spp.utils.session;

import java.security.SecureRandom;

public class Token {

    private static final SecureRandom RNG = new SecureRandom();

    private Token() {

    }

    public static String generate() {
        byte[] bytes = new byte[32];
        RNG.nextBytes(bytes);
        StringBuilder stringBuilder = new StringBuilder(64);
        for (byte b : bytes) {
            stringBuilder.append(String.format("%02x", b));
        }
        return stringBuilder.toString();
    }
}
