package ru.shaplov.controllers;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.shaplov.exceptions.BadRequestException;
import ru.shaplov.exceptions.UnauthorizedException;
import ru.shaplov.logic.ILogicDB;
import ru.shaplov.models.*;

import javax.servlet.ServletContext;
import javax.servlet.http.*;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Class for CRUD operations on Item.class.
 *
 * @author shaplov
 * @since 18.07.2019
 */
@Controller
public class ItemsController {

    private final Logger logger = LogManager.getLogger(ItemsController.class);

    private final ILogicDB logic;

    private final Map<String, BiConsumer<String, Item>> paramMap = new HashMap<>();
    private final Map<String, Function<Map<String, String>, List<IEntity>>> listMap = new HashMap<>();

    @Autowired
    public ItemsController(ILogicDB logic) {
        this.logic = logic;
        paramMap.put("name", (v, item) -> item.setTitle(v));
        paramMap.put("brand", (v, item) -> item.setBrand(new Brand(Integer.parseInt(v))));
        paramMap.put("model", (v, item) -> item.setModel(new Model(Integer.parseInt(v))));
        paramMap.put("body-type-options", (v, item) -> item.setBody(new BodyType(Integer.parseInt(v))));
        paramMap.put("engine-type-options", (v, item) -> item.setEngine(new EngineType(Integer.parseInt(v))));
        paramMap.put("drive-type-options", (v, item) -> item.setDrive(new DriveType(Integer.parseInt(v))));
        paramMap.put("transmission-type-options", (v, item) -> item.setTrans(new TransType(Integer.parseInt(v))));
        listMap.put("ALL", params -> logic.getList("Item"));
        listMap.put("TODAY", params -> logic.getItemsForDate(LocalDate.now()));
        listMap.put("WITH_IMG", params -> logic.getItemsWithImg());
        listMap.put("BRAND", params -> {
            int brand = Integer.parseInt(params.get("filteredBrand"));
            return logic.getItemsForBrand(brand);
        });
    }

    @GetMapping(value = "/items", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String itemsList(@RequestParam Map<String, String> params) {
        try {
            List<IEntity> itemList = listMap.get(params.get("filter")).apply(params);
            JsonFactory factory = new JsonFactory();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            JsonGenerator generator = factory.createGenerator(outputStream);
            generator.writeStartArray();
            for (IEntity entity : itemList) {
                Item item = (Item) entity;
                generator.writeStartObject();
                generator.writeNumberField("itemId", item.getId());
                generator.writeStringField("title", item.getTitle());
                generator.writeStringField("picture", item.getPicture());
                generator.writeBooleanField("sold", item.isSold());
                generator.writeFieldName("user");
                generator.writeStartObject();
                generator.writeNumberField("userId", item.getUser().getId());
                generator.writeStringField("tel", item.getUser().getTel());
                generator.writeEndObject();
                generator.writeStringField("brand", item.getBrand().getTitle());
                generator.writeStringField("model", item.getModel().getTitle());
                generator.writeStringField("body", item.getBody().getTitle());
                generator.writeStringField("engine", item.getEngine().getTitle());
                generator.writeStringField("drive", item.getDrive().getTitle());
                generator.writeStringField("trans", item.getTrans().getTitle());
                generator.writeEndObject();
            }
            generator.writeEndArray();
            generator.close();
            outputStream.close();
            return outputStream.toString(StandardCharsets.UTF_8);
        } catch (Exception e) {
            logger.error("Error on getting list of items.");
            throw new BadRequestException("Error on delete item");
        }
    }

    @PostMapping(value = "/items", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String deleteItem(HttpSession session, @RequestParam("itemId") String itemIdStr) {
        try {
            int itemId = Integer.parseInt(itemIdStr);
            CarUser user = ((Item) logic.get(new Item(itemId))).getUser();
            int userId = user.getId();
            Integer currentUserId = (Integer) (session.getAttribute("userId"));
            if (currentUserId != userId) {
                logger.error("User id not equals to item owner");
                throw new IllegalStateException("User id not equals to item owner");
            }
            Item item = new Item(itemId);
            logic.delete(item);
            logger.info("Item id " + item.getId() + " deleted");
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode node = mapper.createObjectNode();
            node.put("success", true);
            return mapper.writeValueAsString(node);
        } catch (Exception e) {
            logger.error("Error on delete item");
            throw new BadRequestException("Error on delete item");
        }
    }

    @PostMapping(value = "/add", consumes = "multipart/form-data;charset=UTF-8")
    public String addItem(HttpSession session, @RequestParam Map<String, String> allParameters,
                          @RequestParam Part file) {
        ServletContext context = session.getServletContext();
        Path path = (Path) context.getAttribute("IMAGES_PATH");
        Integer userId = (Integer) session.getAttribute("userId");
        final Item item = new Item();
        if (userId == null) {
            throw new UnauthorizedException("You need to login first");
        } else {
            CarUser user = new CarUser();
            user.setId(userId);
            item.setUser(user);
            item.setCreated(Calendar.getInstance());
            item.setSold(false);
        }
        try {
            for (Map.Entry<String, String> entry : allParameters.entrySet()) {
                paramMap.get(entry.getKey()).accept(entry.getValue(), item);
            }
            if (file.getSize() > 0) {
                if (!file.getContentType().equals("image/jpeg")) {
                    logger.error("Wrong content type, must be image/jpeg but was " + file.getContentType());
                    throw new IllegalStateException("wrong image type");
                }
                String fileName = file.getSubmittedFileName();
                String generatedName = fileName.substring(0, fileName.lastIndexOf("."))
                        + new Random().nextInt()
                        + fileName.substring(fileName.lastIndexOf("."));
                Path generatedPath = Paths.get(path.toString(), generatedName);
                file.write(generatedPath.toString());
                String realPath = context.getRealPath("");
                String picture = Paths.get(realPath).relativize(generatedPath).toString();
                item.setPicture(picture);
            }
            logic.save(item);
            return "redirect:/index.html";
        } catch (Exception e) {
            logger.error("Error parsing multipart/form-data");
            throw new BadRequestException("Error parsing multipart/form-data");
        }
    }

    @PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String updateItem(HttpSession session, @RequestParam("itemId") String itemIdStr,
                             @RequestParam("sold") String soldStr) {
        try {
            int itemId = Integer.parseInt(itemIdStr);
            boolean sold = Boolean.parseBoolean(soldStr);
            Item item = new Item(itemId);
            Item storedItem = (Item) logic.get(item);
            int userId = storedItem.getUser().getId();
            Integer curUserId = (Integer) session.getAttribute("userId");
            if (curUserId == null || curUserId != userId) {
                logger.error("Item doesn't belong to user on update");
                throw new IllegalStateException("Unauthorized access");
            }
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode node = mapper.createObjectNode();
            if (sold == storedItem.isSold()) {
                node.put("success", false);
                logger.info("Item doesn't changed");
            } else {
                storedItem.setSold(sold);
                logic.update(storedItem);
                node.put("success", true);
                node.put("sold", storedItem.isSold());
                logger.info("Item status sold changed on " + sold);
            }
            return mapper.writeValueAsString(node);
        } catch (Exception e) {
            logger.error("Error on updating item");
            throw new BadRequestException(String.format("Error on updating item%n%s", e.getMessage()));
        }
    }
}
