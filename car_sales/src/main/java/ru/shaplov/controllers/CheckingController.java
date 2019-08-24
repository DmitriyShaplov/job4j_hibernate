package ru.shaplov.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.shaplov.exceptions.BadRequestException;
import ru.shaplov.logic.ILogicStatus;

import javax.persistence.NoResultException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Controller for checking items statuses
 *
 * @author shaplov
 * @since 03.08.2019
 */
@Controller
public class CheckingController {

    private final Logger logger = LogManager.getLogger(CheckingController.class);

    private final Map<String, Function<Map<String, String>, Integer>> countMap = new HashMap<>();
    private final Map<String, Function<Map<String, String>, Integer>> lastIdMap = new HashMap<>();

    private final ILogicStatus logic;

    @Autowired
    public CheckingController(ILogicStatus logic) {
        this.logic = logic;
        countMap.put("ALL", params -> logic.getItemCount());
        countMap.put("TODAY", params -> logic.getItemCountForDate(LocalDate.now()));
        countMap.put("WITH_IMG", params -> logic.getItemCountWithImg());
        countMap.put("BRAND", params -> logic.getItemCountForBrand(Integer.parseInt(params.get("filteredBrand"))));
        lastIdMap.put("ALL", params -> logic.getLastItemId());
        lastIdMap.put("TODAY", params -> logic.getLastItemIdForDate(LocalDate.now()));
        lastIdMap.put("WITH_IMG", params -> logic.getLastItemIdWithImg());
        lastIdMap.put("BRAND", params -> logic.getLastItemIdForBrand(Integer.parseInt(params.get("filteredBrand"))));
    }

    @GetMapping(value = "/check-items", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ObjectNode checkItems(@RequestParam Map<String, String> params) {
        try {
            int itemsSize = Integer.parseInt(params.get("size"));
            int lastItemId = -1;
            if (itemsSize > 0) {
                lastItemId = Integer.parseInt(params.get("lastId"));
            }
            int size = countMap.get(params.get("filter")).apply(params);
            int itemId = -1;
            if (size > 0) {
                try {
                    itemId = lastIdMap.get(params.get("filter")).apply(params);
                } catch (NoResultException e) {
                    logger.info("Item was added at the runtime while checking.");
                }
            }
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode node = mapper.createObjectNode();
            if (itemsSize > 0 && (itemsSize != size || lastItemId != itemId)) {
                node.put("needUpdate", true);
                logger.info("updating items list");
            } else {
                node.put("needUpdate", false);
            }
            return node;
        } catch (Exception e) {
            String error = String.format("Exception in check-items%n%s", e.getMessage());
            logger.error(error);
            throw new BadRequestException(error);
        }
    }

    @GetMapping(value = "/check-status", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ObjectNode checkStatus(@RequestParam("itemId") String itemIdStr, @RequestParam("sold") String soldStr) {
        try {
            int itemId = Integer.parseInt(itemIdStr);
            boolean sold = Boolean.parseBoolean(soldStr);
            boolean storedSold = logic.isSold(itemId);
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode node = mapper.createObjectNode();
            if (sold != storedSold) {
                node.put("change", true);
                node.put("sold", storedSold);
                logger.info("updating sold status, item id " + itemId);
            } else {
                node.put("change", false);
            }
            return node;
        } catch (Exception e) {
            String error = String.format("Exception in status controller%n%s", e.getMessage());
            logger.error(error);
            throw new BadRequestException(error);
        }
    }
}
