package ru.shaplov.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.activemq.broker.BrokerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.shaplov.jms.Publisher;
import ru.shaplov.logic.ILogicParts;
import ru.shaplov.models.BodyType;
import ru.shaplov.models.Brand;
import ru.shaplov.models.EngineType;
import ru.shaplov.models.Model;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
@TestPropertySource("/application-test.properties")
public class CarPartsControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ILogicParts logicParts;

    @MockBean
    private Publisher publisher;

    @MockBean
    private BrokerService broker;

    @Test
    public void whenGetBrandThenList() throws Exception {
        Brand brand = new Brand(1);
        brand.setTitle("brand");
        ObjectMapper mapper = new ObjectMapper();
        given(
                logicParts.findAllBrand()
        ).willReturn(List.of(brand));
        mvc.perform(
                get("/car_parts")
                        .param("type", "brand")
        )
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(List.of(brand))));

        verify(logicParts, times(1)).findAllBrand();
    }

    @Test
    public void whenGetModelThenList() throws Exception {
        Brand brand = new Brand(1);
        Model model = new Model(1);
        model.setTitle("model-test");
        brand.setModels(List.of(model));
        ObjectMapper mapper = new ObjectMapper();
        given(
                logicParts.getBrandById(1)
        ).willReturn(brand);
        mvc.perform(
                get("/car_parts")
                        .param("type", "model")
                        .param("brandId", "1")
        )
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(List.of(model))));

        verify(logicParts, times(1)).getBrandById(1);
    }

    @Test
    public void whenGetBodyThenList() throws Exception {
        BodyType body = new BodyType(1);
        body.setTitle("test-body");
        ObjectMapper mapper = new ObjectMapper();
        given(
                logicParts.getBodyTypes(1)
        ).willReturn(List.of(body));
        mvc.perform(
                get("/car_parts")
                        .param("type", "body")
                        .param("modelId", "1")
        )
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(List.of(body))));

        verify(logicParts, times(1)).getBodyTypes(1);
    }

    @Test
    public void whenGetEngineThenList() throws Exception {
        EngineType engine = new EngineType(1);
        engine.setTitle("test-engine");
        ObjectMapper mapper = new ObjectMapper();
        given(
                logicParts.getEngineTypes(1, 1)
        ).willReturn(List.of(engine));
        mvc.perform(
                get("/car_parts")
                        .param("type", "engine")
                        .param("modelId", "1")
                        .param("bodyId", "1")
        )
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(List.of(engine))));

        verify(logicParts, times(1)).getEngineTypes(1, 1);
    }
}