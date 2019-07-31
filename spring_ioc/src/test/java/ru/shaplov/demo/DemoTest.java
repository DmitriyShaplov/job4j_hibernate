package ru.shaplov.demo;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.shaplov.demo.ann.GreetingA;
import ru.shaplov.demo.ann.WelcomingPersonA;
import ru.shaplov.demo.xml.GreetingXml;
import ru.shaplov.demo.xml.WelcomingPersonXml;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * @author shaplov
 * @since 30.07.2019
 */
public class DemoTest {

    @Test
    public void whenGetGreetingXmlThenHelloFromXml() {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-context-demo.xml");
        GreetingXml greetingXml = context.getBean(GreetingXml.class);
        assertThat(greetingXml.sayHello(), is("Hello from Xml"));
    }

    @Test
    public void whenWelcomingPersonXmlSayHelloThenXml() {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-context-demo.xml");
        WelcomingPersonXml personXml = context.getBean(WelcomingPersonXml.class);
        assertThat(personXml.hello(), is("Hello from Xml"));
    }

    @Test
    public void whenGetGreetingAThenHelloFromA() {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-context-demo.xml");
        GreetingA greetingA = context.getBean(GreetingA.class);
        assertThat(greetingA.sayHello(), is("Hello from A"));
    }

    @Test
    public void whenWelcomingPersonASayHelloThenA() {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-context-demo.xml");
        WelcomingPersonA personA = context.getBean(WelcomingPersonA.class);
        assertThat(personA.hello(), is("Hello from A"));
    }
}
