package dev.struchkov.haiti.util.captcha.text.renderer;

import java.awt.image.BufferedImage;

/**
 * Render the answer for the CAPTCHA onto the image.
 */
@FunctionalInterface
public interface WordRenderer {

    /**
     * Render a word to a BufferedImage.
     *
     * @param word  The sequence of characters to be rendered.
     * @param image The image onto which the word will be rendered.
     */
    void render(String word, BufferedImage image);

}
