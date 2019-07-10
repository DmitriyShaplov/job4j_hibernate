package ru.shaplov.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.shaplov.logic.ILogic;
import ru.shaplov.logic.LogicDB;
import ru.shaplov.models.IEntity;
import ru.shaplov.models.Item;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Class for checking items amount and last item id.
 * @author shaplov
 * @since 18.07.2019
 */
@WebServlet("/check-items")
public class CheckItemController extends HttpServlet {

    private final Logger logger = LogManager.getLogger(CheckItemController.class);

    private final ILogic logic = LogicDB.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("text/json");
        try {
            int itemsSize = Integer.parseInt(req.getParameter("size"));
            int lastItemId = Integer.parseInt(req.getParameter("lastId"));
            int itemId = logic.getLastItemId();
            int size = logic.getItemCount();
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode node = mapper.createObjectNode();
            if (itemsSize != size || lastItemId != itemId) {
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
