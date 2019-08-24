package ru.shaplov.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.shaplov.principal.CarUserPrincipal;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
public class AuthControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void whenTryAdditemThenAccessDenied() throws Exception {
        mvc.perform(
                get("/additem.html")
        ).andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/index.html"));

    }

    @Test
    public void whenBadCredentials() throws Exception {
        mvc.perform(post("/login").param("username", "testTESTtest1"))
                .andDo(print())
                .andExpect(unauthenticated());
    }

    @Test
    @Sql(value = {"/create-user-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void whenCorrectLogin() throws Exception {
        mvc.perform(formLogin().userParameter("login").user("testlogin").password("password"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(content().string(containsString("{\"username\":\"testlogin\",\"userId\":")));
    }

    @Test
    @WithMockUser
    public void whenGetAdditemAuthorized() throws Exception {
        mvc.perform(get("/additem.html"))
                .andExpect(status().isOk());
    }

    @Test
    public void whenRegistryUser() throws Exception {
        mvc.perform(
                post("/registry").param("login", "test")
                        .param("password", "password")
                        .param("tel", "test")
        )
                .andDo(print())
                .andExpect(content().string(containsString("{\"username\":\"test\",\"userId\":")));
    }

    @Test
    public void whenCheckSessionState() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("username", "user");
        node.put("userId", 1);
        mvc.perform(
                get("/session-state")
                        .with(user(new CarUserPrincipal(1, "user", "password", new ArrayList<>())))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(
                        content().string(mapper.writeValueAsString(node))
                );
    }
}