package ru.shaplov.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.shaplov.models.RedirectUrl;
import ru.shaplov.service.RedirectService;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;

/**
 * @author shaplov
 * @since 29.08.2019
 */
@Controller
public class RedirectionController {

    private static final Logger LOG = LogManager.getLogger(RedirectionController.class);

    private final RedirectService redirectService;

    @Autowired
    public RedirectionController(RedirectService redirectService) {
        this.redirectService = redirectService;
    }

    @GetMapping("/help")
    public String helpPage() {
        return "help.html";
    }

    @GetMapping("/{shortUrl}")
    @ResponseBody
    public ResponseEntity<String> redirecting(@PathVariable String shortUrl,
                                              Principal principal) throws URISyntaxException {
        try {
            RedirectUrl redirectUrl = redirectService.getRedirectAndIncrement(shortUrl, principal.getName());
            URI uri = new URI(redirectUrl.getUrl());
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setLocation(uri);
            HttpStatus httpStatus = HttpStatus.valueOf(redirectUrl.getRedirectType());
            return new ResponseEntity<>(httpHeaders, httpStatus);
        } catch (Exception e) {
            LOG.error("Redirection failed for shot url: " + shortUrl + " accountId: "
                    + principal.getName() + ". " + e.getMessage(), e);
            throw e;
        }
    }
}
