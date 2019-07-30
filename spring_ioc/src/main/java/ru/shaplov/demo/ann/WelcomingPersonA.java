package ru.shaplov.demo.ann;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author shaplov
 * @since 30.07.2019
 */
@Component
public class WelcomingPersonA {

    private final GreetingA greeting;

    @Autowired
    public WelcomingPersonA(GreetingA greeting) {
        this.greeting = greeting;
    }

    public String hello() {
        return greeting.sayHello();
    }
}
