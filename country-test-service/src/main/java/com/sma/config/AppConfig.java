package com.sma.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceView;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Slf4j
public class AppConfig implements WebMvcConfigurer {

    @Autowired
    private Environment env;

    // -------------- Message Converters ----------------------
    @Override
    public void configureMessageConverters(final List<HttpMessageConverter<?>> converters) {

        final ObjectMapper skipNullMapper = new ObjectMapper();
        skipNullMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        skipNullMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        final MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(skipNullMapper);

        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.ENGLISH);
        skipNullMapper.setDateFormat(formatter);

        converters.add(converter);

    }

    // replace-holder properties using @value annotation
    @Bean
    public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Override
    public void configureDefaultServletHandling(final DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    // -------------- Controllers ----------------------

    private String getProperty(final String key) {
        return env.getProperty(key);
    }

    // -------------- View Stuff -----------------------

//    @Override
//    public void configureHandlerExceptionResolvers(final List<HandlerExceptionResolver> exceptionResolvers) {
//        exceptionResolvers.add(restJsonExceptionResolver());
//    }

//    @Bean
//    public RestJsonExceptionResolver restJsonExceptionResolver() {
//        final RestJsonExceptionResolver bean = new RestJsonExceptionResolver();
//        RestJsonExceptionResolver.registerExceptionWithHTTPCode(org.springframework.beans.TypeMismatchException.class, 400);
//        RestJsonExceptionResolver.registerExceptionWithHTTPCode(MissingServletRequestParameterException.class, 400);
//        RestJsonExceptionResolver.registerExceptionWithHTTPCode(MethodArgumentNotValidException.class, 400);
//        RestJsonExceptionResolver.registerExceptionWithHTTPCode(ServletRequestBindingException.class, 400);
//        RestJsonExceptionResolver.registerExceptionWithHTTPCode(AccessDeniedException.class, 403);
//
//        bean.setOrder(1);
//
//        bean.setDiagnosticsDisabled(Boolean.parseBoolean(getProperty("json.diagnosticsDisabled")));
//        // set general error message
//        RestJsonExceptionResolver.setCustomMessage(getProperty("json.errormsg"));
//
//        return bean;
//    }

    @Bean
    public ViewResolver viewResolver() {
        final InternalResourceViewResolver bean = new InternalResourceViewResolver();
        bean.setViewClass(InternalResourceView.class);
        bean.setOrder(999);
        bean.setPrefix("/internal/");
        bean.setContentType("text/html");
        bean.setSuffix("");
        return bean;
    }

    @Bean
    @Description("Spring Message Resolver")
    public ResourceBundleMessageSource messageSource() {
        final ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages/messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

}
