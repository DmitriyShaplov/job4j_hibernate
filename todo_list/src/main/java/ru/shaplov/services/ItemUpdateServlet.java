package ru.shaplov.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.shaplov.logic.DbLogic;
import ru.shaplov.logic.ILogic;
import ru.shaplov.models.Item;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

/**
 * Servlet for update item.
 *
 * @author shaplov
 * @since 05.07.2019
 */
@WebServlet("/item-update")
public class ItemUpdateServlet extends HttpServlet {

    private final ILogic logic = DbLogic.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("text/json");
        BufferedReader in = req.getReader();
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = in.readLine()) != null) {
            sb.append(line);
        }
        ObjectMapper mapper = new ObjectMapper();
        Item item = mapper.readValue(sb.toString(), Item.class);
        item.setCreated(Calendar.getInstance());
        Item nitem = logic.update(item);
        PrintWriter out = resp.getWriter();
        String json = mapper.writeValueAsString(nitem);
        out.append(json);
        out.flush();
    }
}
