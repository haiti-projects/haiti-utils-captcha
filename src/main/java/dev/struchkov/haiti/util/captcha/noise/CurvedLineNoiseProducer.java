package dev.struchkov.haiti.util.captcha.noise;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.security.SecureRandom;
import java.util.Random;

import static java.awt.geom.PathIterator.SEG_LINETO;
import static java.awt.geom.PathIterator.SEG_MOVETO;

/**
 * Adds a randomly curved line to the image.
 */
public class CurvedLineNoiseProducer implements NoiseProducer {

    private static final Random RAND = new SecureRandom();

    private final Color color;
    private final float width;

    public CurvedLineNoiseProducer() {
        this(Color.BLACK, 3.0f);
    }

    public CurvedLineNoiseProducer(Color color, float width) {
        this.color = color;
        this.width = width;
    }

    @Override
    public void makeNoise(BufferedImage image) {
        final int imgWidth = image.getWidth();
        final int imgHeight = image.getHeight();

        // the curve from where the points are taken
        final CubicCurve2D cc = new CubicCurve2D.Float(
                imgWidth * .1f, imgHeight * RAND.nextFloat(),
                imgWidth * .1f, imgHeight * RAND.nextFloat(),
                imgWidth * .25f, imgHeight * RAND.nextFloat(),
                imgWidth * .9f, imgHeight * RAND.nextFloat()
        );

        // creates an iterator to define the boundary of the flattened curve
        final PathIterator pi = cc.getPathIterator(null, 2);
        final Point2D[] tmp = new Point2D[200];
        int i = 0;

        // while pi is iterating the curve, adds points to tmp array
        while (!pi.isDone()) {
            float[] coords = new float[6];
            int currentSegment = pi.currentSegment(coords);
            if (currentSegment == SEG_MOVETO || currentSegment == SEG_LINETO) {
                tmp[i] = new Point2D.Float(coords[0], coords[1]);
            }
            i++;
            pi.next();
        }

        // the points where the line changes the stroke and direction
        final Point2D[] pts = new Point2D[i];
        // copies points from tmp to pts
        System.arraycopy(tmp, 0, pts, 0, i);

        final Graphics2D graph = (Graphics2D) image.getGraphics();
        graph.setRenderingHints(new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON));

        graph.setColor(color);

        // for the maximum 3 point change the stroke and direction
        for (i = 0; i < pts.length - 1; i++) {
            if (i < 3) {
                graph.setStroke(new BasicStroke(this.width));
            }
            graph.drawLine(
                    (int) pts[i].getX(),
                    (int) pts[i].getY(),
                    (int) pts[i + 1].getX(),
                    (int) pts[i + 1].getY()
            );
        }
        graph.dispose();
    }
}
