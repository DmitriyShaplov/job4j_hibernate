package ru.shaplov.filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author shaplov
 * @since 18.07.2019
 */
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession();
        if (session.getAttribute("username") == null || session.getAttribute("userId") == null) {
            if (req.getRequestURI().contains("additem")
            || req.getRequestURI().contains("update")
            || (req.getRequestURI().contains("items") && req.getMethod().equalsIgnoreCase("POST"))) {
                resp.sendRedirect(String.format("%s/index.html", req.getContextPath()));
            } else {
                chain.doFilter(request, response);
            }
        } else {
            if (req.getRequestURI().contains("login") || req.getRequestURI().contains("registry")) {
                resp.sendRedirect(String.format("%s/index.html", req.getContextPath()));
            } else {
                chain.doFilter(request, response);
            }
        }
    }
}
