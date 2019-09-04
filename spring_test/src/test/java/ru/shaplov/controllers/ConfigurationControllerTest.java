package ru.shaplov.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.shaplov.models.Account;
import ru.shaplov.models.RedirectUrl;
import ru.shaplov.repository.AccountRepository;
import ru.shaplov.repository.RedirectUrlRepository;
import ru.shaplov.service.AccountDetailsService;
import ru.shaplov.service.AccountService;
import ru.shaplov.service.RedirectService;
import ru.shaplov.validator.AccountValidator;
import ru.shaplov.validator.RedirectUrlValidator;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = ConfigurationController.class,
        includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                classes = {
                AccountDetailsService.class, AccountValidator.class, RedirectUrlValidator.class
                }))
@AutoConfigureMockMvc
public class ConfigurationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @MockBean
    private RedirectService redirectService;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private RedirectUrlRepository redirectUrlRepository;

    @Test
    public void whenOpenAccountThenJsonResponse() throws Exception {
        Account account = new Account();
        account.setAccountId("account-test");
        Account result = new Account();
        result.setAccountId("account-test");
        result.setDecodedPass("password-test");
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("accountId", "account-test");
        ObjectNode resultNode = mapper.createObjectNode();
        resultNode.put("success", true);
        resultNode.put("description", "Your account is opened");
        resultNode.put("password", result.getDecodedPass());
        given(
                accountService.openAccount(account)
        ).willReturn(result);
        mockMvc.perform(
                post("/account").content(mapper.writeValueAsString(node))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(resultNode)));
    }

    @Test
    @WithMockUser(username = "username")
    public void whenRegistryUrlThenJsonResponse() throws Exception {
        RedirectUrl redirectUrl = new RedirectUrl();
        redirectUrl.setUrl("https://www.google.com");
        RedirectUrl result = new RedirectUrl();
        result.setUrl(redirectUrl.getUrl());
        result.setShortURL("short");
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("url", result.getUrl());
        ObjectNode resultNode = mapper.createObjectNode();
        resultNode.put("shortUrl", "http://localhost/" + result.getShortURL());
        given(
                redirectService.registryUrl(redirectUrl, "username")
        ).willReturn(result);
        mockMvc.perform(
                post("/register").content(mapper.writeValueAsString(node))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(resultNode)));
    }

    @Test
    public void whenGetStatisticForUser() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        RedirectUrl url = new RedirectUrl();
        url.setUrl("https://www.google.com");
        url.setCount(10);
        List<RedirectUrl> resultList = List.of(url);
        Map<String, Long> map = resultList.stream()
                .collect(Collectors.toMap(RedirectUrl::getUrl, RedirectUrl::getCount));
        String accountId = "username";
        given(
                redirectService.findUrlsForAccountId(accountId)
        ).willReturn(resultList);
        mockMvc.perform(
                get("/statistic/username")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(map)));
    }
}