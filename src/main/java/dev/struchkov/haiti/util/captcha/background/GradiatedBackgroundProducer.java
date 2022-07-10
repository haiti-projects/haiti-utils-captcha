package dev.struchkov.haiti.util.captcha.background;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static java.awt.Color.DARK_GRAY;
import static java.awt.Color.WHITE;
import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;

/**
 * Creates a gradiated background with the given <i>from</i> and <i>to</i>
 * Color values. If none are specified they default to light gray and white
 * respectively.
 */
public class GradiatedBackgroundProducer implements BackgroundProducer {

    private Color fromColor;
    private Color toColor;

    public GradiatedBackgroundProducer() {
        this(DARK_GRAY, WHITE);
    }

    public GradiatedBackgroundProducer(Color from, Color to) {
        fromColor = from;
        toColor = to;
    }

    public void setFromColor(Color fromColor) {
        this.fromColor = fromColor;
    }

    public void setToColor(Color toColor) {
        this.toColor = toColor;
    }

    @Override
    public BufferedImage getBackground(int width, int height) {
        // create an opaque image
        final BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        final Graphics2D g = img.createGraphics();
        final RenderingHints hints = new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);

        g.setRenderingHints(hints);

        // create the gradient color
        final GradientPaint ytow = new GradientPaint(0, 0, fromColor, width, height, toColor);

        g.setPaint(ytow);
        // draw gradient color
        g.fill(new Rectangle2D.Double(0, 0, width, height));

        // draw the transparent image over the background
        g.drawImage(img, 0, 0, null);
        g.dispose();

        return img;
    }

    @Override
    public BufferedImage addBackground(BufferedImage image) {
        return getBackground(image.getWidth(), image.getHeight());
    }

}
