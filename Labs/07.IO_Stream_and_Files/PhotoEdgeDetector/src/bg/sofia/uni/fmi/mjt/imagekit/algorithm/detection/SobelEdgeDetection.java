package bg.sofia.uni.fmi.mjt.imagekit.algorithm.detection;

import bg.sofia.uni.fmi.mjt.imagekit.algorithm.ImageAlgorithm;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class SobelEdgeDetection implements EdgeDetectionAlgorithm {
    public static final int WHITE_PIXEL_VALUE = 255;
    public static final int BLACK_PIXEL_VALUE = 0;
    private final ImageAlgorithm grayscaleAlgorithm;

    private static final int[][] GX = {
        {1, 0, -1},
        {2, 0, -2},
        {1, 0, -1}
    };

    private static final int[][] GY = {
        {1, 2, 1},
        {0, 0, 0},
        {-1, -2, -1}
    };

    public SobelEdgeDetection(ImageAlgorithm grayscaleAlgorithm) {
        this.grayscaleAlgorithm = grayscaleAlgorithm;
    }

    @Override
    public BufferedImage process(BufferedImage image) {
        BufferedImage grayImage = grayscaleAlgorithm.process(image);
        BufferedImage edgeImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);

        for (int i = 1; i < grayImage.getWidth() - 1; i++) {
            for (int j = 1; j < grayImage.getHeight() - 1; j++) {
                int xValuesSum = 0;
                int yValuesSum = 0;

                for (int k = -1; k <= 1; k++) {
                    for (int l = -1; l <= 1; l++) {
                        int rgb = grayImage.getRGB(i + k, j + l);
                        Color color = new Color(rgb);

                        xValuesSum += GX[k + 1][l + 1] * color.getRed();
                        yValuesSum += GY[k + 1][l + 1] * color.getRed();
                    }
                }

                double gradientMagnitude = Math.sqrt(xValuesSum * xValuesSum + yValuesSum * yValuesSum);

                Color pixelColor = evaluatePixelColor(gradientMagnitude);

                edgeImage.setRGB(i, j, pixelColor.getRGB());
            }
        }

        return edgeImage;
    }

    private Color evaluatePixelColor(double magnitude) {
        int edgeValue = (int) magnitude;

        edgeValue = Math.min(edgeValue, WHITE_PIXEL_VALUE);
        edgeValue = Math.max(edgeValue, BLACK_PIXEL_VALUE);

        return new Color(edgeValue, edgeValue, edgeValue);
    }
}