package ru.shaplov;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

/**
 * @author shaplov
 * @since 16.10.2019
 */
@SpringBootApplication
@EnableJms
public class JmsSubApplication {

    public static void main(String[] args) {
        SpringApplication.run(JmsSubApplication.class, args);
    }
}
