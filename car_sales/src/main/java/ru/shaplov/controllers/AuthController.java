package ru.shaplov.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.shaplov.logic.ILogicUser;
import ru.shaplov.models.CarUser;

import javax.servlet.http.HttpSession;
import java.util.Calendar;

/**
 * @author shaplov
 * @since 03.08.2019
 */
@Controller
public class AuthController {

    private final Logger logger = LogManager.getLogger(AuthController.class);

    private final ILogicUser logic;

    @Autowired
    public AuthController(ILogicUser logic) {
        this.logic = logic;
    }

    @PostMapping(value = "/registry", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ObjectNode registryUser(HttpSession session, @RequestParam String login,
                               @RequestParam String password,
                               @RequestParam String tel) {
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
        boolean success = false;
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
                success = true;
            } catch (ConstraintViolationException e) {
                logger.info(e.getMessage(), e);
                node.put("repeatedLogin", "error");
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        if (success) {
            CarUser user = logic.authUser(login, password);
            if (user != null) {
                session.setAttribute("username", user.getLogin());
                session.setAttribute("userId", user.getId());
                node.put("username", user.getLogin());
                node.put("userId", user.getId());
                logger.info("User successfully registered");
            }
        }
        return node;
    }

    @PostMapping(value = "/login", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ObjectNode loginUser(HttpSession session, @RequestParam String login, @RequestParam String password) {
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
        return node;
    }

    @GetMapping(value = "/session-state", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ObjectNode checkSessionState(HttpSession session) {
        String username = (String) session.getAttribute("username");
        Integer userId = (Integer) session.getAttribute("userId");
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("username", username);
        node.put("userId", userId);
        return node;
    }

    @PostMapping(value = "logout")
    @ResponseBody
    public void logoutUser(HttpSession session) {
        if (session != null) {
            session.invalidate();
            logger.info("Session invalidated");
        }
    }
}
