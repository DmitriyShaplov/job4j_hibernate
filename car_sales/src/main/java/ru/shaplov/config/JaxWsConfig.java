package ru.shaplov.config;

import com.sun.xml.ws.transport.http.servlet.WSSpringServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * @author shaplov
 * @since 19.09.2019
 */
@Configuration
@ImportResource("classpath:/spring-ws.xml")
public class JaxWsConfig {

    @Bean
    public ServletRegistrationBean wsSpringServlet() {
        return new ServletRegistrationBean(new WSSpringServlet(), "/ws/*");
    }
}
