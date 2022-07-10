package dev.struchkov.haiti.util.captcha;

import dev.struchkov.haiti.util.captcha.background.BackgroundProducer;
import dev.struchkov.haiti.util.captcha.background.TransparentBackgroundProducer;
import dev.struchkov.haiti.util.captcha.noise.CurvedLineNoiseProducer;
import dev.struchkov.haiti.util.captcha.noise.NoiseProducer;
import dev.struchkov.haiti.util.captcha.text.producer.DefaultTextProducer;
import dev.struchkov.haiti.util.captcha.text.producer.TextProducer;
import dev.struchkov.haiti.util.captcha.text.renderer.DefaultWordRenderer;
import dev.struchkov.haiti.util.captcha.text.renderer.WordRenderer;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

/**
 * A builder for generating a CAPTCHA image/answer pair.
 *
 * <p>
 * Example for generating a new CAPTCHA:
 * </p>
 * <pre>Captcha captcha = new Captcha.Builder(200, 50)
 * 	.addText()
 * 	.addBackground()
 * 	.build();</pre>
 * <p>Note that the <code>build()</code> must always be called last. Other methods are optional,
 * and can sometimes be repeated. For example:</p>
 * <pre>Captcha captcha = new Captcha.Builder(200, 50)
 * 	.addText()
 * 	.addNoise()
 * 	.addNoise()
 * 	.addNoise()
 * 	.addBackground()
 * 	.build();</pre>
 * <p>Adding multiple backgrounds has no affect; the last background added will simply be the
 * one that is eventually rendered.</p>
 * <p>To validate that <code>answerStr</code> is a correct answer to the CAPTCHA:</p>
 *
 * <code>captcha.isCorrect(answerStr);</code>
 */
public final class Captcha {

    private final String answer;
    private final BufferedImage img;
    private final LocalDateTime timeStamp;

    private Captcha(Builder builder) {
        img = builder.img;
        answer = builder.answer;
        timeStamp = builder.timeStamp;
    }

    public static Builder builder(int width, int height) {
        return new Builder(width, height);
    }

    public boolean isCorrect(String answer) {
        return this.answer.equals(answer);
    }

    public String getAnswer() {
        return answer;
    }

    /**
     * @return A png captcha image.
     */
    public BufferedImage getImage() {
        return img;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    @Override
    public String toString() {
        return "[Answer: " +
                answer +
                "][Timestamp: " +
                timeStamp +
                "][Image: " +
                img +
                "]";
    }

    public static class Builder {

        private String answer = "";
        private BufferedImage img;
        private BufferedImage backGround;
        private LocalDateTime timeStamp;
        private boolean addBorder = false;

        public Builder(int width, int height) {
            img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        }

        /**
         * Add a background using the default {@link BackgroundProducer} (a {@link TransparentBackgroundProducer}).
         */
        public Builder addBackground() {
            return addBackground(new TransparentBackgroundProducer());
        }

        /**
         * Add a background using the given {@link BackgroundProducer}.
         */
        public Builder addBackground(BackgroundProducer bgProd) {
            backGround = bgProd.getBackground(img.getWidth(), img.getHeight());
            return this;
        }

        /**
         * Generate the answer to the CAPTCHA using the {@link DefaultTextProducer}.
         */
        public Builder addText() {
            return addText(new DefaultTextProducer());
        }

        /**
         * Generate the answer to the CAPTCHA using the given
         * {@link TextProducer}.
         */
        public Builder addText(TextProducer txtProd) {
            return addText(txtProd, new DefaultWordRenderer());
        }

        /**
         * Generate the answer to the CAPTCHA using the default
         * {@link TextProducer}, and render it to the image using the given
         * {@link WordRenderer}.
         */
        public Builder addText(WordRenderer wRenderer) {
            return addText(new DefaultTextProducer(), wRenderer);
        }

        /**
         * Generate the answer to the CAPTCHA using the given
         * {@link TextProducer}, and render it to the image using the given
         * {@link WordRenderer}.
         */
        public Builder addText(TextProducer txtProd, WordRenderer wRenderer) {
            answer += txtProd.getText();
            wRenderer.render(answer, img);
            return this;
        }

        /**
         * Add noise using the default {@link NoiseProducer} (a {@link CurvedLineNoiseProducer}).
         */
        public Builder addNoise() {
            return this.addNoise(new CurvedLineNoiseProducer());
        }

        /**
         * Add noise using the given NoiseProducer.
         */
        public Builder addNoise(NoiseProducer nProd) {
            nProd.makeNoise(img);
            return this;
        }

        /**
         * Draw a single-pixel wide black border around the image.
         */
        public Builder addBorder() {
            addBorder = true;
            return this;
        }

        /**
         * Build the CAPTCHA. This method should always be called, and should always
         * be called last.
         *
         * @return The constructed CAPTCHA.
         */
        public Captcha build() {
            if (backGround == null) {
                backGround = new TransparentBackgroundProducer().getBackground(img.getWidth(), img.getHeight());
            }

            // Paint the main image over the background
            final Graphics2D g = backGround.createGraphics();
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            g.drawImage(img, null, null);

            if (addBorder) {
                int width = img.getWidth();
                int height = img.getHeight();

                g.setColor(Color.BLACK);
                g.drawLine(0, 0, 0, width);
                g.drawLine(0, 0, width, 0);
                g.drawLine(0, height - 1, width, height - 1);
                g.drawLine(width - 1, height - 1, width - 1, 0);
            }

            img = backGround;

            timeStamp = LocalDateTime.now();

            return new Captcha(this);
        }

    }
}
