package ru.shaplov.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

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
@WebServlet("/session-state")
public class SessionStateController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/json");
        HttpSession session = req.getSession();
        String username = (String) session.getAttribute("username");
        Integer userId = (Integer) session.getAttribute("userId");
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("username", username);
        node.put("userId", userId);
        String json = mapper.writeValueAsString(node);
        PrintWriter out = resp.getWriter();
        out.append(json);
        out.flush();
    }
}
