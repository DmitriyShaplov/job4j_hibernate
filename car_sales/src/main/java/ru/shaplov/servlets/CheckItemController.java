package ru.shaplov.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.shaplov.logic.ILogicStatus;
import ru.shaplov.logic.LogicStatus;

import javax.persistence.NoResultException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Class for checking items amount and last item id.
 * @author shaplov
 * @since 18.07.2019
 */
@WebServlet("/check-items")
public class CheckItemController extends HttpServlet {

    private final Logger logger = LogManager.getLogger(CheckItemController.class);

    private final ILogicStatus logic = LogicStatus.getInstance();

    private final Map<String, Function<HttpServletRequest, Integer>> countMap = new HashMap<>();
    private final Map<String, Function<HttpServletRequest, Integer>> lastIdMap = new HashMap<>();

    @Override
    public void init() throws ServletException {
        countMap.put("ALL", req -> logic.getItemCount());
        countMap.put("TODAY", req -> logic.getItemCountForDate(LocalDate.now()));
        countMap.put("WITH_IMG", req -> logic.getItemCountWithImg());
        countMap.put("BRAND", req -> logic.getItemCountForBrand(Integer.parseInt(req.getParameter("filteredBrand"))));
        lastIdMap.put("ALL", req -> logic.getLastItemId());
        lastIdMap.put("TODAY", req -> logic.getLastItemIdForDate(LocalDate.now()));
        lastIdMap.put("WITH_IMG", req -> logic.getLastItemIdWithImg());
        lastIdMap.put("BRAND", req -> logic.getLastItemIdForBrand(Integer.parseInt(req.getParameter("filteredBrand"))));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("text/json");
        try {
            int itemsSize = Integer.parseInt(req.getParameter("size"));
            int lastItemId = -1;
            if (itemsSize > 0) {
                lastItemId = Integer.parseInt(req.getParameter("lastId"));
            }
            int size = countMap.get(req.getParameter("filter")).apply(req);
            int itemId = -1;
            if (size > 0) {
                try {
                    itemId = lastIdMap.get(req.getParameter("filter")).apply(req);
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
            String json = mapper.writeValueAsString(node);
            PrintWriter out = resp.getWriter();
            out.append(json);
            out.flush();
        } catch (Exception e) {
            String error = String.format("Exception in check-items%n%s", e.getMessage());
            logger.error(error);
            resp.sendError(400, error);
        }
    }
}
