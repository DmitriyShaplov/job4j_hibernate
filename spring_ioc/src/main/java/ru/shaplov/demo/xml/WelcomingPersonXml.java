package ru.shaplov.demo.xml;

/**
 * @author shaplov
 * @since 30.07.2019
 */
public class WelcomingPersonXml {

    private final GreetingXml greeting;

    public WelcomingPersonXml(GreetingXml greeting) {
        this.greeting = greeting;
    }

    public String hello() {
        return greeting.sayHello();
    }
}
