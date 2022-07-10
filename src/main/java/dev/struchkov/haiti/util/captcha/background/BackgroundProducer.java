package dev.struchkov.haiti.util.captcha.background;

import java.awt.image.BufferedImage;

/**
 * Used to add a captcha background.
 *
 * @author upagge 10.07.2022
 */
public interface BackgroundProducer {

    /**
     * Add the background to the given image.
     *
     * @param image The image onto which the background will be rendered.
     * @return The image with the background rendered.
     */
    BufferedImage addBackground(BufferedImage image);

    /**
     * Returns a gradient background.
     */
    BufferedImage getBackground(int width, int height);

}
