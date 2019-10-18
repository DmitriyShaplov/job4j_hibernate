package ru.shaplov.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import ru.shaplov.logic.CarUserDetailsService;
import ru.shaplov.principal.CarUserPrincipal;

import javax.servlet.http.HttpSession;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

/**
 * @author shaplov
 * @since 13.08.2019
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final Logger LOG = LogManager.getLogger(WebSecurityConfig.class);

    @Autowired
    private CarUserDetailsService userService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                    .antMatchers("/images/**", "/index.html", "/switch.css", "/ws/*", "/ws", "/js/**", "/css/**").permitAll()
                    .antMatchers(HttpMethod.GET, "/items").permitAll()
                    .antMatchers("/registry", "/login").permitAll()
                    .antMatchers("/additem.html").access("not hasRole('ADMIN') and authenticated")
                    .anyRequest().authenticated()
                .and()
                    .formLogin()
                    .usernameParameter("login")
                    .loginPage("/index.html")
                    .loginProcessingUrl("/login")
                    .successHandler(authenticationSuccessHandler())
                    .failureHandler(authenticationFailureHandler())
                .and()
                    .logout()
                    .permitAll()
                    .logoutSuccessHandler((httpServletRequest, httpServletResponse, authentication) -> {
                        httpServletResponse.setStatus(HttpStatus.OK.value());
                        LOG.info("Logged out");
                        httpServletResponse.getWriter().flush();
                    })
                    .invalidateHttpSession(true)
                .and()
                    .csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return (httpServletRequest, httpServletResponse, e) -> {
            LOG.info("Authentication failed");
            httpServletResponse.sendError(HttpStatus.UNAUTHORIZED.value());
        };
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return ((httpServletRequest, httpServletResponse, authentication) -> {
            SecurityContext sc = SecurityContextHolder.getContext();
            sc.setAuthentication(authentication);
            HttpSession session = httpServletRequest.getSession(true);
            session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, sc);
            httpServletResponse.setCharacterEncoding("UTF-8");
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode node = mapper.createObjectNode();
            node.put("username", authentication.getName());
            node.put("userId", ((CarUserPrincipal) authentication.getPrincipal()).getId());
            String jsonStr = mapper.writeValueAsString(node);
            LOG.info("Authentication success");
            httpServletResponse.getWriter().append(jsonStr).flush();
        });
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

//    @Autowired
//    private DataSource dataSource;
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.jdbcAuthentication().dataSource(dataSource)
//                .usersByUsernameQuery("select username, password from users where username=?")
//                .authoritiesByUsernameQuery("select username, role from user_roles where username=?");
//    }
}
