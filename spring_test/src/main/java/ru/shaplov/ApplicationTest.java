package ru.shaplov;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author shaplov
 * @since 29.08.2019
 */
@SpringBootApplication
@EnableJpaRepositories("ru.shaplov.repository")
public class ApplicationTest {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationTest.class, args);
    }
}