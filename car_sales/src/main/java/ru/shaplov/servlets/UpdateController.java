package ru.shaplov.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.shaplov.logic.ILogicDB;
import ru.shaplov.logic.LogicDB;
import ru.shaplov.models.Item;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author shaplov
 * @since 18.07.2019
 */
@WebServlet("/update")
public class UpdateController extends HttpServlet {

    private final Logger logger = LogManager.getLogger(UpdateController.class);

    private final ILogicDB logic = LogicDB.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/json");
        try {
            int itemId = Integer.parseInt(req.getParameter("itemId"));
            boolean sold = Boolean.parseBoolean(req.getParameter("sold"));
            Item item = new Item(itemId);
            Item storedItem = (Item) logic.get(item);
            int userId = storedItem.getUser().getId();
            HttpSession session = req.getSession();
            Integer curUserId = (Integer) session.getAttribute("userId");
            if (curUserId == null || curUserId != userId) {
                logger.error("Item doesn't belong to user on update");
                throw new ServletException("Unauthorized access");
            }
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode node = mapper.createObjectNode();
            PrintWriter out = resp.getWriter();
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
            String json = mapper.writeValueAsString(node);
            out.append(json);
            out.flush();
        } catch (Exception e) {
            logger.error("Error on updating item");
            resp.sendError(400, String.format("Error on updating item%n%s", e.getMessage()));
        }
    }
}
