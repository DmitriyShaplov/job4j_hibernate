package ru.shaplov.servlets;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.shaplov.logic.ILogicDB;
import ru.shaplov.logic.LogicDB;
import ru.shaplov.models.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.BiConsumer;

/**
 * @author shaplov
 * @since 18.07.2019
 */
@WebServlet("/additem")
public class AddItemController extends HttpServlet {

    private final Logger logger = LogManager.getLogger(AddItemController.class);

    private final ILogicDB logic = LogicDB.getInstance();

    private final Map<String, BiConsumer<String, Item>> paramMap = new HashMap<>();

    private ServletFileUpload uploader = null;

    @Override
    public void init() throws ServletException {
        DiskFileItemFactory fileFactory = new DiskFileItemFactory();
        Path path = (Path) getServletContext().getAttribute("IMAGES_PATH");
        fileFactory.setRepository(path.toFile());
        this.uploader = new ServletFileUpload(fileFactory);
        this.uploader.setHeaderEncoding("UTF-8");
        paramMap.put("name", (v, item) -> item.setTitle(v));
        paramMap.put("brand", (v, item) -> item.setBrand(new Brand(Integer.parseInt(v))));
        paramMap.put("model", (v, item) -> item.setModel(new Model(Integer.parseInt(v))));
        paramMap.put("body-type-options", (v, item) -> item.setBody(new BodyType(Integer.parseInt(v))));
        paramMap.put("engine-type-options", (v, item) -> item.setEngine(new EngineType(Integer.parseInt(v))));
        paramMap.put("drive-type-options", (v, item) -> item.setDrive(new DriveType(Integer.parseInt(v))));
        paramMap.put("transmission-type-options", (v, item) -> item.setTrans(new TransType(Integer.parseInt(v))));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setCharacterEncoding("utf-8");
        if (!ServletFileUpload.isMultipartContent(req)) {
            throw new ServletException("Content type is not multipart/form-data");
        }
        Path path = (Path) getServletContext().getAttribute("IMAGES_PATH");
        HttpSession session = req.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        final Item item = new Item();
        if (userId == null) {
            resp.sendError(401, "You need to login first");
            return;
        } else {
            CarUser user = new CarUser();
            user.setId(userId);
            item.setUser(user);
            item.setCreated(Calendar.getInstance());
            item.setSold(false);
        }
        try {
            List<FileItem> formItems = uploader.parseRequest(req);
            if (formItems != null && formItems.size() > 0) {
                for (FileItem fileItem : formItems) {
                    if (fileItem.isFormField()) {
                        String name = fileItem.getFieldName();
                        String value = fileItem.getString("UTF-8");
                        paramMap.get(name).accept(value, item);
                    } else {
                        String fileName = fileItem.getName();
                        String contentType = fileItem.getContentType();
                        if (fileItem.getSize() > 0) {
                            if (!contentType.equals("image/jpeg")) {
                                logger.error("Wrong content type, must be image/jpeg but was " + contentType);
                                resp.sendError(400, "wrong image type");
                                return;
                            }
                            String generatedName = fileName.substring(0, fileName.lastIndexOf("."))
                                    + new Random().nextInt()
                                    + fileName.substring(fileName.lastIndexOf("."));
                            Path generatedPath = Paths.get(path.toString(), generatedName);
                            fileItem.write(generatedPath.toFile());
                            String realPath = getServletContext().getRealPath("");
                            String picture = Paths.get(realPath).relativize(generatedPath).toString();
                            item.setPicture(picture);
                        }
                    }
                }
                logic.save(item);
                resp.sendRedirect(String.format("%s/index.html", req.getContextPath()));
            }
        } catch (Exception e) {
            logger.error("Error parsing multipart/form-data");
            resp.sendError(400, "Error parsing multipart/form-data");
        }
    }
}
