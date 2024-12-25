package bg.sofia.uni.fmi.mjt.imagekit.filesystem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LocalFileSystemImageManagerTest {
    private LocalFileSystemImageManager fileSystemManager;

    @BeforeEach
    void setUp() {
        fileSystemManager = new LocalFileSystemImageManager();
    }

    @Test
    void testLoadImageSuccessfully() throws IOException {
        File file = new File("image.png");

        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        ImageIO.write(image, "png", file);

        try {
            BufferedImage loadedImage = fileSystemManager.loadImage(file);

            assertNotNull(loadedImage, "Should load correct file.");
        } finally {
            file.delete();
        }
    }

    @Test
    void testLoadImageThrowsForNullArgument() {
        assertThrows(IllegalArgumentException.class, () -> fileSystemManager.loadImage(null), "Null file argument should throws exception.");
    }

    @Test
    void testLoadImageThrowsForUnsupportedFormat() {
        assertThrows(IOException.class, () -> fileSystemManager.loadImage( new File("image.unsupported")),
            "Should not be able to load file with unsupported format!");
    }

    @Test
    void testLoadImageThrowsForNonExistingFile() {
        File file = new File("file.png");
        assertThrows(IOException.class, () -> fileSystemManager.loadImage(file),
            "Loading a non-existing file should throw exception.");
    }

    @Test
    void testLoadImagesFromDirectorySuccessfully() throws IOException {
        File temporaryDirectory = Files.createTempDirectory("directory").toFile();
        File image1 = new File(temporaryDirectory, "image1.png");
        File image2 = new File(temporaryDirectory, "image2.jpeg");

        BufferedImage testImage = new BufferedImage(1, 11, BufferedImage.TYPE_INT_RGB);
        ImageIO.write(testImage, "png", image1);
        ImageIO.write(testImage, "jpeg", image2);


        try {
            var images = fileSystemManager.loadImagesFromDirectory(temporaryDirectory);
            assertEquals(2, images.size(), "Two images should be loaded");
        } finally {
            image1.delete();
            image2.delete();
            temporaryDirectory.delete();
        }
    }

    @Test
    void testLoadImagesFromDirectoryThrowsForNullArgument() {
        assertThrows(IllegalArgumentException.class, () -> fileSystemManager.loadImagesFromDirectory(null), "Null directory argument should throws exception.");
    }

    @Test
    void testLoadImagesFromNonExistentDirectory() {
        assertThrows(IOException.class, () -> fileSystemManager.loadImagesFromDirectory(new File("directory")),
            "Loading images from non-existing directory should throws exception.");
    }

    @Test
    void testLoadImagesFromFileInsteadOfDirectory() throws IOException {
        File tempFile = Files.createTempFile("file", ".png").toFile();

        try {
            assertThrows(IOException.class, () -> fileSystemManager.loadImagesFromDirectory(tempFile),
                "Loading images from file that is not a directory should throws exception.");
        } finally {
            tempFile.delete();
        }
    }

    @Test
    void testLoadImagesWithUnsupportedFormats() throws IOException {
        File tempDir = Files.createTempDirectory("directory").toFile();
        File file = new File(tempDir, "file.unsupported");
        file.createNewFile();

        try {
            assertThrows(IOException.class, () -> fileSystemManager.loadImagesFromDirectory(tempDir),
                "Should not be able to load files with unsupported format.");
        } finally {
            file.delete();
            tempDir.delete();
        }
    }

    @Test
    void testLoadImagesFromEmptyDirectory() throws IOException {
        File tempDir = Files.createTempDirectory("empty-dir").toFile();

        try {
            assertThrows(IOException.class, () -> fileSystemManager.loadImagesFromDirectory(tempDir),
                "Exception should be thrown when there are no images to be loaded.");
        } finally {
            tempDir.delete();
        }
    }


    @Test
    void testSaveImageThrowsForNullImageArgument() {
        assertThrows(IllegalArgumentException.class, () -> fileSystemManager.saveImage(null, new File("image.png")),
            "Should not be able to save null image.");
    }

    @Test
    void testSaveImageThrowsForNullFileArgument() {
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        assertThrows(IllegalArgumentException.class, () -> fileSystemManager.saveImage(image,null),
            "Should not be able to save image in null file.");
    }

    @Test
    void testSaveImageThrowsForExistingFile() throws Exception {
        File temporaryFile = File.createTempFile("image", ".jpeg");
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);

        assertThrows(IOException.class, () -> fileSystemManager.saveImage(image, temporaryFile),
            "Should not be able to save already existing file.");
    }

    @Test
    void testSaveImageThrowsIfParentDirectoryDoesNotExist() {
        File nonExistentDirectory = new File("folder");
        File imageFile = new File(nonExistentDirectory, "image.png");

        BufferedImage testImage = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);

        assertThrows(IOException.class, () -> fileSystemManager.saveImage(testImage, imageFile),
            "Should not be able to save image if directory does not exist.");
    }

    @Test
    void testSaveImageSuccessfully() throws IOException {
        File file = new File("Folder");
        file.mkdirs();

        File imageFile = new File(file,"image.png");
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);

        try {
            fileSystemManager.saveImage(image, imageFile);
            assertTrue(imageFile.exists(), "After saving image the file should exists.");
        } finally {
            imageFile.delete();
            file.delete();
        }
    }
}