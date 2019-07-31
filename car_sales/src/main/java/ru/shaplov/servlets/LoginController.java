package ru.shaplov.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.shaplov.logic.ILogicUser;
import ru.shaplov.logic.LogicUser;
import ru.shaplov.models.CarUser;

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
@WebServlet("/login")
public class LoginController extends HttpServlet {

    private final Logger logger = LogManager.getLogger(LoginController.class);

    private final ILogicUser logic = LogicUser.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("text/json");
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        String errorMsg = null;
        if (login == null || password == null || login.equals("") || password.equals("")) {
            node.put("error", "empty");
            errorMsg = "Login and password can't be empty";
            logger.info("Login or password is empty");
        }
        if (errorMsg == null) {
            CarUser user = logic.authUser(login, password);
            if (user != null) {
                HttpSession session = req.getSession();
                session.setAttribute("username", user.getLogin());
                session.setAttribute("userId", user.getId());
                node.put("username", user.getLogin());
                node.put("userId", user.getId());
                logger.info("User logged in. Id: " + user.getId() + " . Login: " + user.getLogin());
            } else {
                node.put("error", "not exists");
                logger.info("Such user doesn't exists");
            }
        }
        String json = mapper.writeValueAsString(node);
        PrintWriter out = resp.getWriter();
        out.append(json);
        out.flush();
    }
}
