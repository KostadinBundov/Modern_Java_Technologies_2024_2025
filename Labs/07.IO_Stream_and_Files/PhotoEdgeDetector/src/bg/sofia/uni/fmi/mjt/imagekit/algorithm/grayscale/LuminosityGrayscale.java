package bg.sofia.uni.fmi.mjt.imagekit.algorithm.grayscale;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class LuminosityGrayscale implements GrayscaleAlgorithm {
    private static final double RED_MULTIPLICATOR = 0.21;
    private static final double GREEN_MULTIPLICATOR = 0.72;
    private static final double BLUE_MULTIPLICATOR = 0.07;

    public LuminosityGrayscale() { }

    @Override
    public BufferedImage process(BufferedImage image) {
        if (image == null) {
            throw new IllegalArgumentException("Image should not be null!");
        }

        BufferedImage grayImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                int rgb = image.getRGB(i, j);

                Color grayColor = evaluatePixelColor(rgb);
                grayImage.setRGB(i, j, grayColor.getRGB());
            }
        }

        return grayImage;
    }

    private Color evaluatePixelColor(int pixel) {
        Color color = new Color(pixel);
        int red = color.getRed();
        int blue = color.getBlue();
        int green = color.getGreen();

        int gray = (int)(RED_MULTIPLICATOR * red + GREEN_MULTIPLICATOR * green + BLUE_MULTIPLICATOR * blue);

        return new Color(gray, gray, gray);
    }
}