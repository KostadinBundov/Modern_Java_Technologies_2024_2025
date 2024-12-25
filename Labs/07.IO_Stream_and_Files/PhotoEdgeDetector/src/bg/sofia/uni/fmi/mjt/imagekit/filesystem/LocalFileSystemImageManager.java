package bg.sofia.uni.fmi.mjt.imagekit.filesystem;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class LocalFileSystemImageManager implements FileSystemImageManager {
    public LocalFileSystemImageManager() { }

    @Override
    public BufferedImage loadImage(File imageFile) throws IOException {
        if (imageFile == null) {
            throw new IllegalArgumentException("The file cannot be null!");
        }

        Path path = imageFile.toPath();

        if (path == null || !Files.isRegularFile(path)) {
            throw new IOException("The File is not a regular file!");
        }

        if (!checkIsFileFormatIsSupported(imageFile)) {
            throw new IOException("The file format is not supported!");
        }

        BufferedImage image = ImageIO.read(imageFile);

        if (image == null) {
            throw new IOException("Error reading the image!");
        }

        return image;
    }

    @Override
    public List<BufferedImage> loadImagesFromDirectory(File imagesDirectory) throws IOException {
        if (imagesDirectory == null) {
            throw new IllegalArgumentException("The directory cannot be null!");
        }
        Path path = imagesDirectory.toPath();

        if (path == null || !Files.exists(path)) {
            throw new IOException("The directory does not exist!");
        }
        if (!Files.isDirectory(path)) {
            throw new IOException("The path is not a directory!");
        }

        List<BufferedImage> images = new ArrayList<>();
        File[] files = imagesDirectory.listFiles();

        if (files == null || files.length == 0) {
            throw new IOException("No files in directory!");
        }

        for (File file : files) {
            BufferedImage image = loadImage(file);
            images.add(image);
        }
        return images;
    }

    @Override
    public void saveImage(BufferedImage image, File imageFile) throws IOException {
        if (image == null) {
            throw new IllegalArgumentException("The image cannot be null!");
        }

        if (imageFile == null) {
            throw new IllegalArgumentException("The file cannot be null!");
        }

        File parentDirectory = imageFile.getParentFile();
        if (parentDirectory == null || !parentDirectory.exists()) {
            throw new IOException("The parent directory does not exist!");
        }

        if (imageFile.exists()) {
            throw new IOException("The file already exists!");
        }

        if (!checkIsFileFormatIsSupported(imageFile)) {
            throw new IOException("Unsupported file format or extension!");
        }
        
        String fileExtension = getExtension(imageFile.toPath());
        boolean result = ImageIO.write(image, fileExtension, imageFile);

        if (!result) {
            throw new IOException("The file is not save!");
        }
    }

    private boolean checkIsFileFormatIsSupported(File file) {
        String fileName = file.getName().toLowerCase();

        for (var format : SupportedFileFormats.values()) {
            if (fileName.endsWith("." + format.toString().toLowerCase())) {
                return true;
            }
        }

        return false;
    }

    private String getExtension(Path path) {

        String imageName = path.getFileName().toString();
        int index = imageName.lastIndexOf('.');

        if (index == -1) {
            return "";
        }

        return imageName.substring(index + 1).toLowerCase();
    }
}