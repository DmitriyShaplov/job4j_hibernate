package ru.shaplov.listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author shaplov
 * @since 18.07.2019
 */
@WebListener
public class FileLocationContextListener implements ServletContextListener {

    private Logger logger = LogManager.getLogger(FileLocationContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext ctx = sce.getServletContext();
        String rootPath = ctx.getRealPath("");
        String relativePath = ctx.getInitParameter("images.dir");
        Path path = Paths.get(rootPath, relativePath);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
                logger.info("Images directory created.");
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
        ctx.setAttribute("IMAGES_PATH", path);
    }
}
