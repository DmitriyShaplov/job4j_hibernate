package ru.shaplov.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.shaplov.exceptions.BadRequestException;
import ru.shaplov.exceptions.UnauthorizedException;
import ru.shaplov.logic.ILogicItem;
import ru.shaplov.models.*;
import ru.shaplov.principal.CarUserPrincipal;

import javax.servlet.http.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    private static final Logger LOG = LogManager.getLogger(ItemsController.class);

    private final ILogicItem logic;

    private final Map<String, BiConsumer<String, Item>> paramMap = new HashMap<>();
    private final Map<String, Function<Map<String, String>, List<Item>>> listMap = new HashMap<>();

    @Autowired
    public ItemsController(ILogicItem logic) {
        this.logic = logic;
        paramMap.put("name", (v, item) -> item.setTitle(v));
        paramMap.put("brand", (v, item) -> item.setBrand(new Brand(Integer.parseInt(v))));
        paramMap.put("model", (v, item) -> item.setModel(new Model(Integer.parseInt(v))));
        paramMap.put("body-type-options", (v, item) -> item.setBody(new BodyType(Integer.parseInt(v))));
        paramMap.put("engine-type-options", (v, item) -> item.setEngine(new EngineType(Integer.parseInt(v))));
        paramMap.put("drive-type-options", (v, item) -> item.setDrive(new DriveType(Integer.parseInt(v))));
        paramMap.put("transmission-type-options", (v, item) -> item.setTrans(new TransType(Integer.parseInt(v))));
        listMap.put("ALL", params -> logic.getItems());
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
            List<Item> itemList = listMap.get(params.get("filter")).apply(params);
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(itemList);
        } catch (Exception e) {
            LOG.error("Error on getting list of items.");
            throw new BadRequestException("Error on getting items");
        }
    }

    @PostMapping(value = "/items", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String deleteItem(@RequestParam("itemId") String itemIdStr, Authentication authentication) {
        try {
            int itemId = Integer.parseInt(itemIdStr);
            CarUser user = logic.get(new Item(itemId)).getUser();
            int userId = user.getId();
            if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
                throw new UnauthorizedException("You need to login first");
            }
            int currentUserId = ((CarUserPrincipal) authentication.getPrincipal()).getId();
            if ((!authentication.getAuthorities().isEmpty()
                    && !"ROLE_ADMIN".equals(authentication.getAuthorities().iterator().next().getAuthority()))
                    && currentUserId != userId) {
                LOG.error("User id not equals to item owner");
                throw new IllegalStateException("User id not equals to item owner");
            }
            Item item = new Item(itemId);
            logic.delete(item);
            LOG.info("Item id " + item.getId() + " deleted");
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode node = mapper.createObjectNode();
            node.put("success", true);
            return mapper.writeValueAsString(node);
        } catch (Exception e) {
            LOG.error("Error on delete item");
            throw new BadRequestException("Error on delete item");
        }
    }

    @PostMapping(value = "/add", consumes = "multipart/form-data;charset=UTF-8")
    public String addItem(@RequestParam Map<String, String> allParameters,
                          @RequestParam Part file, Authentication authentication) {
        final Item item = new Item();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            throw new UnauthorizedException("You need to login first");
        } else {
            int userId = ((CarUserPrincipal) authentication.getPrincipal()).getId();
            CarUser user = new CarUser();
            user.setId(userId);
            item.setUser(user);
            item.setCreated(LocalDateTime.now());
            item.setSold(false);
        }
        try {
            for (Map.Entry<String, String> entry : allParameters.entrySet()) {
                paramMap.get(entry.getKey()).accept(entry.getValue(), item);
            }
            logic.save(item, file);
            return "redirect:/index.html";
        } catch (Exception e) {
            LOG.error("Error parsing multipart/form-data");
            throw new BadRequestException("Error parsing multipart/form-data");
        }
    }

    @GetMapping("/images/{image}")
    @ResponseBody
    public ResponseEntity<Resource> getPicture(@PathVariable String image) {
        try {
            PictureLob img = logic.getImg(new PictureLob(Integer.parseInt(image)));
            Resource resource = new ByteArrayResource(img.getImg());
            String contentType = img.getMimeType();
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);
        } catch (Exception e) {
            throw new BadRequestException("Image not found " + image);
        }
    }

    @PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String updateItem(Authentication authentication, @RequestParam("itemId") String itemIdStr,
                             @RequestParam("sold") String soldStr) {
        try {
            int itemId = Integer.parseInt(itemIdStr);
            boolean sold = Boolean.parseBoolean(soldStr);
            Item item = new Item(itemId);
            Item storedItem = logic.get(item);
            int userId = storedItem.getUser().getId();
            if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
                LOG.error("Unauthorized user");
                throw new IllegalStateException("You must authorize first.");
            }
            int curUserId = ((CarUserPrincipal) authentication.getPrincipal()).getId();
            if ((!authentication.getAuthorities().isEmpty()
                    && !"ROLE_ADMIN".equals(authentication.getAuthorities().iterator().next().getAuthority()))
                    && curUserId != userId) {
                LOG.error("User id not equals to item owner");
                throw new IllegalStateException("User id not equals to item owner");
            }
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode node = mapper.createObjectNode();
            if (sold == storedItem.isSold()) {
                node.put("success", false);
                LOG.info("Item doesn't changed");
            } else {
                storedItem.setSold(sold);
                logic.update(storedItem);
                node.put("success", true);
                node.put("sold", storedItem.isSold());
                LOG.info("Item status sold changed on " + sold);
            }
            return mapper.writeValueAsString(node);
        } catch (Exception e) {
            LOG.error("Error on updating item");
            throw new BadRequestException(String.format("Error on updating item%n%s", e.getMessage()));
        }
    }
}
