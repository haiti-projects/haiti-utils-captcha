package dev.struchkov.haiti.util.captcha.text.producer;

import java.security.SecureRandom;
import java.util.Random;

/**
 * Produces text of a given length from a given array of characters.
 */
public class DefaultTextProducer implements TextProducer {

    private static final Random RAND = new SecureRandom();
    private static final int DEFAULT_LENGTH = 5;
    private static final char[] DEFAULT_CHARS = new char[]{
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'k', 'm', 'n', 'p', 'r', 'w', 'x', 'y',
            '2', '3', '4', '5', '6', '7', '8'
    };

    private final int length;
    private final char[] srcChars;

    public DefaultTextProducer() {
        this(DEFAULT_LENGTH, DEFAULT_CHARS);
    }

    public DefaultTextProducer(int length, char[] srcChars) {
        this.length = length;
        this.srcChars = copyOf(srcChars, srcChars.length);
    }

    private static char[] copyOf(char[] original, int newLength) {
        char[] copy = new char[newLength];
        System.arraycopy(
                original, 0, copy, 0,
                Math.min(original.length, newLength)
        );
        return copy;
    }

    @Override
    public String getText() {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(srcChars[RAND.nextInt(srcChars.length)]);
        }
        return sb.toString();
    }

}
