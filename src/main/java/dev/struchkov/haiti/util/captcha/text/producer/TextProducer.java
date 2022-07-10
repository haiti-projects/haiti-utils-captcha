package dev.struchkov.haiti.util.captcha.text.producer;

/**
 * Generate an answer for the CAPTCHA.
 */
@FunctionalInterface
public interface TextProducer {

    /**
     * Generate a series of characters to be used as the answer for the CAPTCHA.
     */
    String getText();

}
