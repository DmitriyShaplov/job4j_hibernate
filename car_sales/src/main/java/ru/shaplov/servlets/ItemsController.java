package ru.shaplov.servlets;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.shaplov.logic.ILogic;
import ru.shaplov.logic.LogicDB;
import ru.shaplov.models.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * @author shaplov
 * @since 18.07.2019
 */
@WebServlet("/items")
public class ItemsController extends HttpServlet {

    private final Logger logger = LogManager.getLogger(ItemsController.class);

    private final ILogic logic = LogicDB.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/json");
        resp.setCharacterEncoding("UTF-8");
        List<IEntity> itemList = logic.getList("Item");
        JsonFactory factory = new JsonFactory();
        PrintWriter out = resp.getWriter();
        JsonGenerator generator = factory.createGenerator(out);
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
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/json");
        try {
            int itemId = Integer.parseInt(req.getParameter("itemId"));
            CarUser user = ((Item) logic.get(new Item(itemId))).getUser();
            int userId = user.getId();
            HttpSession session = req.getSession();
            Integer currentUserId = (Integer) (session.getAttribute("userId"));
            if (currentUserId != userId) {
                logger.error("User id not equals to item owner");
                throw new ServletException("User id not equals to item owner");
            }
            Item item = new Item(itemId);
            logic.delete(item);
            logger.info("Item id " + item.getId() + " deleted");
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode node = mapper.createObjectNode();
            node.put("success", true);
            String json = mapper.writeValueAsString(node);
            PrintWriter out = resp.getWriter();
            out.append(json);
            out.flush();
        } catch (Exception e) {
            logger.error("Error on delete item");
            resp.sendError(400, "Error on delete item");
        }
    }
}
