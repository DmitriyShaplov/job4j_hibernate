package ru.shaplov.util;

import org.springframework.core.io.Resource;

import javax.servlet.http.Part;
import java.io.IOException;
import java.util.Optional;

/**
 * @author shaplov
 * @since 21.08.2019
 */
public interface ImageStorageService {
    Optional<String> storeImage(Part file) throws IOException;
    Resource loadImageAsResource(String fileName) throws IOException;
}
