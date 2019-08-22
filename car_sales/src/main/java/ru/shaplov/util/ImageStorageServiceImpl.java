package ru.shaplov.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import ru.shaplov.exceptions.BadRequestException;

import javax.servlet.http.Part;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Random;

/**
 * @author shaplov
 * @since 21.08.2019
 */
@Service
public class ImageStorageServiceImpl implements ImageStorageService {

    private static final Logger LOG = LogManager.getLogger(ImageStorageServiceImpl.class);

    private final Path imageStorageLocation;

    public ImageStorageServiceImpl(@Value("${custom.images.dir}") String path) {
        this.imageStorageLocation = Paths.get(path).toAbsolutePath().normalize();
    }

    public Optional<String> storeImage(Part file) throws IOException {
        if (!Files.exists(imageStorageLocation)) {
            Files.createDirectories(imageStorageLocation);
        }
        String result = null;
        if (file.getSize() > 0) {
            if (!file.getContentType().contains("image")) {
                LOG.error("Wrong content type, must be image but was " + file.getContentType());
                throw new IllegalStateException("wrong image type");
            }
            String fileName = file.getSubmittedFileName();
            String generatedName = fileName.substring(0, fileName.lastIndexOf("."))
                    + new Random().nextInt()
                    + fileName.substring(fileName.lastIndexOf("."));
            Path generatedPath = imageStorageLocation.resolve(generatedName);
            file.write(generatedPath.toString());
            result = generatedName;
        }
        return Optional.ofNullable(result);
    }

    @Override
    public Resource loadImageAsResource(String fileName) throws IOException {
        Resource resource = new UrlResource(imageStorageLocation.resolve(fileName).toUri());
        if (!resource.exists()) {
            throw new BadRequestException("Image not found " + fileName);
        }
        return resource;
    }
}
