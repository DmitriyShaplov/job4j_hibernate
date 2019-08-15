package ru.shaplov.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.shaplov.principal.CarUserPrincipal;
import ru.shaplov.logic.ILogicUser;
import ru.shaplov.models.CarUser;
import ru.shaplov.validator.CarUserValidator;

import javax.servlet.http.HttpSession;
import java.util.Calendar;
import java.util.stream.Collectors;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

/**
 * @author shaplov
 * @since 03.08.2019
 */
@Controller
public class AuthController {

    private static final Logger LOG = LogManager.getLogger(AuthController.class);

    private final AuthenticationManager authManager;

    private final CarUserValidator userValidator;

    private final ILogicUser logic;

    @Autowired
    public AuthController(AuthenticationManager authManager, CarUserValidator userValidator, ILogicUser logic) {
        this.authManager = authManager;
        this.userValidator = userValidator;
        this.logic = logic;
    }

    @PostMapping(value = "/registry", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ObjectNode registryUser(@ModelAttribute CarUser user, BindingResult bindingResult, HttpSession session) {
        userValidator.validate(user, bindingResult);
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        boolean success = false;
        if (bindingResult.hasErrors()) {
            String errors = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(". "));
            LOG.info(errors);
            if (bindingResult.getFieldError("login") != null
                    && "Duplicate.user".equals(bindingResult.getFieldError("login").getCode())) {
                node.put("repeatedLogin", "error");
            }
        } else {
            try {
                user.setCreated(Calendar.getInstance());
                user = logic.save(user);
                success = true;
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
        }
        if (success) {
            UsernamePasswordAuthenticationToken authReq
                    = new UsernamePasswordAuthenticationToken(user.getLogin(), user.getPassword());
            Authentication auth = authManager.authenticate(authReq);
            if (auth.isAuthenticated()) {
                SecurityContext securityContext = SecurityContextHolder.getContext();
                securityContext.setAuthentication(auth);
                session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, securityContext);
                node.put("username", user.getLogin());
                node.put("userId", user.getId());
                LOG.info("User successfully registered and logged in");
            }
            SecurityContext sc = SecurityContextHolder.getContext();
            sc.setAuthentication(auth);
        }
        return node;
    }

    @GetMapping(value = "/session-state", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ObjectNode checkSessionState(Authentication authentication) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        if (!(authentication == null) && !(authentication instanceof AnonymousAuthenticationToken)) {
            String username = ((CarUserPrincipal) authentication.getPrincipal()).getUsername();
            int userId = ((CarUserPrincipal) authentication.getPrincipal()).getId();
            node.put("username", username);
            node.put("userId", userId);
        }
        return node;
    }
}
