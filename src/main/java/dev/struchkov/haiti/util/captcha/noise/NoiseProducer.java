package dev.struchkov.haiti.util.captcha.noise;

import java.awt.image.BufferedImage;

@FunctionalInterface
public interface NoiseProducer {

    void makeNoise(BufferedImage image);

}
