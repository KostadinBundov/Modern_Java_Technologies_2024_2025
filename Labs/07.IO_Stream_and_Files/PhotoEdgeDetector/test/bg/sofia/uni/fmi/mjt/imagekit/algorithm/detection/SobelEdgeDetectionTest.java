package bg.sofia.uni.fmi.mjt.imagekit.algorithm.detection;

import bg.sofia.uni.fmi.mjt.imagekit.algorithm.ImageAlgorithm;
import bg.sofia.uni.fmi.mjt.imagekit.algorithm.grayscale.LuminosityGrayscale;
import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

class SobelEdgeDetectionTest {
    private BufferedImage image;

    private void setUp() {
        image = new BufferedImage(3, 3, BufferedImage.TYPE_INT_RGB);

        image.setRGB(0, 0, new Color(255, 255, 255).getRGB());
        image.setRGB(1, 0, new Color(255, 255, 255).getRGB());
        image.setRGB(2, 0, new Color(255, 255, 255).getRGB());
        image.setRGB(0, 1, new Color(255, 255, 255).getRGB());
        image.setRGB(1, 1, new Color(0, 0, 0).getRGB());
        image.setRGB(2, 1, new Color(255, 255, 255).getRGB());
        image.setRGB(0, 2, new Color(255, 255, 255).getRGB());
        image.setRGB(1, 2, new Color(255, 255, 255).getRGB());
        image.setRGB(2, 2, new Color(255, 255, 255).getRGB());
    }

    @Test
    void testGradientCalculation() {
        setUp();

        ImageAlgorithm grayscale = new LuminosityGrayscale();
        SobelEdgeDetection edgeDetection = new SobelEdgeDetection(grayscale);

        BufferedImage resultImage = edgeDetection.process(image);

        Color centralPixel = new Color(resultImage.getRGB(1, 1));
        assertEquals(new Color(0, 0, 0).getRGB(), resultImage.getRGB(1, 1), "Central pixel should be an edge");
    }
}