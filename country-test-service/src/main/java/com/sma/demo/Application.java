package com.sma.demo;

import java.io.IOException;
import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.servlet.Filter;

import org.apache.catalina.filters.HttpHeaderSecurityFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;

import com.sma.config.AppConfig;
import com.sma.config.SwaggerConfig;
import com.sma.filter.BasicAuthenticationFilter;
import com.sma.filter.TracerRequestFilter;

import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

@SpringBootApplication
@Import(value = { AppConfig.class, SwaggerConfig.class })
@Slf4j
public class Application {

    @Autowired
    private Environment env;

    @PostConstruct
    void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    public static void main(String[] args) throws IOException {
        SpringApplication.run(Application.class, args);
    }

    @Bean()
    public FilterRegistrationBean tracerFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new TracerRequestFilter());
        registration.addUrlPatterns("/api/*");
        registration.setOrder(2);

        return registration;
    }

    @Bean()
    public FilterRegistrationBean basicAuthenticationFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        final Filter filter = new BasicAuthenticationFilter();
        registration.addInitParameter("username", env.getProperty("spring.security.username"));
        registration.addInitParameter("secret", env.getProperty("spring.security.password"));
        registration.setFilter(filter);
        registration.addUrlPatterns("/swagger-ui.html");
        registration.addUrlPatterns("/admin-actuator/*");
        registration.setOrder(4);

        return registration;
    }

    /**
     * security filter
     */
    @Bean()
    public FilterRegistrationBean httpHeaderSecurityFilter() {
        final HttpHeaderSecurityFilter f = new HttpHeaderSecurityFilter();
        f.setHstsEnabled(true);
        f.setHstsIncludeSubDomains(true);
        f.setHstsMaxAgeSeconds(31536000);

        final FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(f);
        registration.addUrlPatterns("/api/*");
        registration.setOrder(3);

        return registration;
    }

    @Bean
    public MapperFactory getMapper() {
        return new DefaultMapperFactory.Builder().build();
    }

}
