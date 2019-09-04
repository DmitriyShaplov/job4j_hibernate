package ru.shaplov.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.shaplov.models.RedirectUrl;
import ru.shaplov.repository.AccountRepository;
import ru.shaplov.repository.RedirectUrlRepository;
import ru.shaplov.service.AccountDetailsService;
import ru.shaplov.service.RedirectService;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = RedirectionController.class,
        includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                classes = {
                        AccountDetailsService.class
                }))
@AutoConfigureMockMvc
public class RedirectionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RedirectService redirectService;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private RedirectUrlRepository redirectUrlRepository;

    @Test
    @WithMockUser(username = "username")
    public void whenGetRedirection() throws Exception {
        RedirectUrl redirectUrl = new RedirectUrl();
        redirectUrl.setShortURL("test");
        redirectUrl.setUrl("https://www.google.com");
        given(
                redirectService.getRedirectAndIncrement("test", "username")
        ).willReturn(redirectUrl);
        mockMvc.perform(
                get("/test")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("https://www.google.com"));
    }

    @Test
    public void whenGetHelpPage() throws Exception {
        mockMvc.perform(
                get("/help")
        ).andExpect(
                view().name("help.html")
        );
    }
}