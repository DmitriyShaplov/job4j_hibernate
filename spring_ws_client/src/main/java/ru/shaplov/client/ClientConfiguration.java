package ru.shaplov.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

/**
 * @author shaplov
 * @since 24.09.2019
 */
@Configuration
public class ClientConfiguration {

    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("ru.shaplov.domain");
        return marshaller;
    }

    @Bean
    public ItemClient itemClient(Jaxb2Marshaller marshaller) {
        ItemClient client = new ItemClient();
        client.setDefaultUri("http://localhost:8080/car_sales/ws");
        client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);
        return client;
    }
}
