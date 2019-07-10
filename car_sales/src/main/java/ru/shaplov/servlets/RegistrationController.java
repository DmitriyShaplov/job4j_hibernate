package ru.shaplov.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;
import ru.shaplov.logic.ILogic;
import ru.shaplov.logic.LogicDB;
import ru.shaplov.models.CarUser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

/**
 * @author shaplov
 * @since 18.07.2019
 */
@WebServlet("/registry")
public class RegistrationController extends HttpServlet {

    private final Logger logger = LogManager.getLogger(RegistrationController.class);

    private final ILogic logic = LogicDB.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("text/json");
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        String tel = req.getParameter("tel");
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        StringBuilder errorMsg = new StringBuilder();
        if (login == null || login.equals("")) {
            node.put("login", "empty");
            errorMsg.append("Login can't be null or empty");
            errorMsg.append(System.lineSeparator());
        }
        if (password == null || password.equals("")) {
            node.put("password", "empty");
            errorMsg.append("Password can't be null or empty");
            errorMsg.append(System.lineSeparator());
        }
        if (tel == null || tel.equals("")) {
            node.put("tel", "empty");
            errorMsg.append("tel can't be null or empty");
            errorMsg.append(System.lineSeparator());
        }
        String err = errorMsg.toString();
        if (!err.isEmpty()) {
            logger.info(err);
        } else {
            try {
                CarUser user = new CarUser();
                user.setLogin(login);
                user.setPassword(password);
                user.setTel(tel);
                user.setCreated(Calendar.getInstance());
                logic.save(user);
            } catch (ConstraintViolationException e) {
                logger.info(e.getMessage(), e);
                node.put("repeatedLogin", "error");
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        CarUser user = logic.authUser(login, password);
        if (user != null) {
            HttpSession session = req.getSession();
            session.setAttribute("username", user.getLogin());
            session.setAttribute("userId", user.getId());
            node.put("username", user.getLogin());
            node.put("userId", user.getId());
            logger.info("User successfully registered");
        }
        String json = mapper.writeValueAsString(node);
        PrintWriter out = resp.getWriter();
        out.append(json);
        out.flush();
    }
}
