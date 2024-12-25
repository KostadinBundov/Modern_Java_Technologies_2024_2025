package bg.sofia.uni.fmi.mjt.imagekit.algorithm.grayscale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.awt.Color;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LuminosityGrayscaleTest {

    private LuminosityGrayscale grayscaleAlgorithm;
    private BufferedImage image;

    @BeforeEach
    void setUp() {
        grayscaleAlgorithm = new LuminosityGrayscale();
    }

    @Test
    void testSinglePixelConversionToGray() {
        int redPixel = new Color(255, 0, 0).getRGB();
        image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, redPixel);
        BufferedImage resultImage = grayscaleAlgorithm.process(image);

        assertEquals(new Color(53, 53, 53).getRGB(), resultImage.getRGB(0, 0), "Conversion of single pixel should return correct value");
    }
}