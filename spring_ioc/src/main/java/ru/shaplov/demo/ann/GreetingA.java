package ru.shaplov.demo.ann;

import org.springframework.stereotype.Component;

/**
 * @author shaplov
 * @since 30.07.2019
 */
@Component
public class GreetingA {

    public String sayHello() {
        return "Hello from A";
    }
}
