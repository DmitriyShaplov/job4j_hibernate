package ru.shaplov;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.shaplov.client.ItemClient;
import ru.shaplov.domain.GetItemResponse;
import ru.shaplov.domain.GetItemsResponse;

/**
 * @author shaplov
 * @since 24.09.2019
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    CommandLineRunner lookup(ItemClient quoteClient) {
        return args -> {
            int id = 34;
            if (args.length > 0) {
                id = Integer.parseInt(args[0]);
            }
            GetItemResponse response = quoteClient.getItem(id);
            System.out.println("Item title: " + response.getItem().getTitle());
            System.out.println("Item date: " + response.getItem().getCreated());
            GetItemsResponse response1 = quoteClient.getItems();
            response1.getItem().forEach(item -> System.out.println("Item title: " + item.getTitle()));
        };
    }
}
