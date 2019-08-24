package ru.shaplov.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockPart;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.shaplov.logic.ILogicItem;
import ru.shaplov.models.*;
import ru.shaplov.principal.CarUserPrincipal;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ItemsControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ILogicItem service;

    @Test
    public void whenGetItemsListThenJsonList() throws Exception {
        Item item = new Item(2);
        CarUser user = new CarUser(3);
        user.setTel("999");
        Brand brand = new Brand();
        brand.setTitle("brand");
        Model model = new Model();
        model.setTitle("model");
        BodyType body = new BodyType();
        body.setTitle("body");
        EngineType engine = new EngineType();
        engine.setTitle("engine");
        DriveType drive = new DriveType();
        drive.setTitle("drive");
        TransType trans = new TransType();
        trans.setTitle("trans");
        item.setTitle("testTitle");
        item.setUser(user);
        item.setBrand(brand);
        item.setModel(model);
        item.setBody(body);
        item.setEngine(engine);
        item.setDrive(drive);
        item.setTrans(trans);
        ObjectMapper mapper = new ObjectMapper();
        given(
                service.getItems()
        ).willReturn(
                List.of(item)
        );
        mvc.perform(
                get("/items").accept(MediaType.APPLICATION_JSON_UTF8)
                        .param("filter", "ALL")
        ).andExpect(
                status().isOk()
        ).andExpect(
                content().string(mapper.writeValueAsString(List.of(item)))
        );
    }

    @Test
    public void whenAddItemThenServiceAdded() throws Exception {
        Item item = new Item();
        item.setTitle("test-name");
        CarUser user = new CarUser(10);
        MockPart file = new MockPart("file", "".getBytes());
        item.setUser(user);
        mvc.perform(
                multipart("/add")
                        .part(file)
                        .with(user(new CarUserPrincipal(10, "user", "password", new ArrayList<>())))
                        .param("name", "test-name")
                        .accept(MediaType.TEXT_HTML)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
        ).andExpect(
                status().is3xxRedirection()
        ).andExpect(
                view().name("redirect:/index.html")
        );
        verify(
                service, times(1)
        ).save(item, file);
    }

    @Test
    public void whenDeleteItemThenServiceCall() throws Exception {
        Item item = new Item(1);
        CarUser user = new CarUser(1);
        item.setUser(user);
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("success", true);
        given(
                service.get(new Item(1))
        ).willReturn(item);
        mvc.perform(
                post("/items")
                        .with(user(new CarUserPrincipal(1, "user", "password", new ArrayList<>())))
                        .param("itemId", "1")

        ).andExpect(
                status().isOk()
        ).andExpect(
                content().string(mapper.writeValueAsString(node))
        );
        verify(
                service, times(1)
        ).delete(new Item(1));
    }

    @Test
    public void whenGetImageThenContentBytes() throws Exception {

        URL url = ItemsControllerTest.class.getClassLoader().getResource("static/images/system/No_image.png");
        Resource resource = new UrlResource(Objects.requireNonNull(url));
        PictureLob img = new PictureLob(1);
        img.setImg(Objects.requireNonNull(ItemsControllerTest.class.getClassLoader()
                .getResourceAsStream("static/images/system/No_image.png")).readAllBytes());
        byte[] file = Files.readAllBytes(resource.getFile().toPath());
        given(
                service.getImg(new PictureLob(1))
        ).willReturn(
                img
        );
        mvc.perform(
                get("/images/1")
        ).andExpect(
                status().isOk()
        ).andExpect(
                content().bytes(file)
        );
    }

    @Test
    public void whenUpdateThenStatusChanged() throws Exception {
        Item item = new Item(5);
        CarUser user = new CarUser(1);
        item.setUser(user);
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("success", true);
        node.put("sold", true);
        given(
                service.get(new Item(5))
        ).willReturn(item);
        mvc.perform(
                post("/update")
                        .with(user(new CarUserPrincipal(1, "user", "password", new ArrayList<>())))
                        .param("itemId", "5")
                        .param("sold", "true")
        ).andExpect(
                status().isOk()
        ).andExpect(
                content().string(mapper.writeValueAsString(node))
        );
    }
}