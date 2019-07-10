package ru.shaplov.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.shaplov.logic.ILogic;
import ru.shaplov.logic.LogicDB;
import ru.shaplov.models.Item;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author shaplov
 * @since 18.07.2019
 */
@WebServlet("/check-status")
public class StatusController extends HttpServlet {

    private final Logger logger = LogManager.getLogger(StatusController.class);

    private final ILogic logic = LogicDB.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/json");
        try {
            int itemId = Integer.parseInt(req.getParameter("itemId"));
            boolean sold = Boolean.parseBoolean(req.getParameter("sold"));
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
            String json = mapper.writeValueAsString(node);
            PrintWriter out = resp.getWriter();
            out.append(json);
            out.flush();
        } catch (Exception e) {
            String error = String.format("Exception in status controller%n%s", e.getMessage());
            logger.error(error);
            resp.sendError(400, error);
        }
    }
}
