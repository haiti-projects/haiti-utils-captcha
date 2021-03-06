package dev.struchkov.haiti.util.captcha.background;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

/**
 * Generates a transparent background.
 */
public class TransparentBackgroundProducer implements BackgroundProducer {

    @Override
    public BufferedImage addBackground(BufferedImage image) {
        return getBackground(image.getWidth(), image.getHeight());
    }

    @Override
    public BufferedImage getBackground(int width, int height) {
        final BufferedImage bg = new BufferedImage(width, height, Transparency.TRANSLUCENT);
        final Graphics2D g = bg.createGraphics();

        g.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.0f));
        g.fillRect(0, 0, width, height);

        return bg;
    }

}
