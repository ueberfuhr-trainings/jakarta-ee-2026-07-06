package de.schulung.spring.todos.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

/**
 * Web-/MVC-Konfiguration in Spring: JSP-View-Resolver (für die klassische UI),
 * Weiterreichen statischer Dateien (index.html) an den Default-Servlet des
 * Servers und JSON-Serialisierung inkl. {@code java.time} (Jackson).
 */
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    /** Löst View-Namen (z.B. "displayTodos") auf JSPs unter /WEB-INF/ auf. */
    @Bean
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/");
        resolver.setSuffix(".jsp");
        resolver.setViewClass(JstlView.class);
        return resolver;
    }

    /**
     * Bean-Validation-Validator als Spring-Bean. Wird u.a. vom
     * {@code TodosWebController} injiziert, um Formulareingaben zu prüfen; ist
     * zugleich der von Spring MVC für {@code @Valid} genutzte Validator.
     */
    @Bean
    public LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }

    /** Statische Ressourcen (index.html) an den Default-Servlet weiterreichen. */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    /** Jackson mit java.time-Unterstützung; Datumsangaben als ISO-String, nicht als Zahl. */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        Jackson2ObjectMapperBuilder builder = Jackson2ObjectMapperBuilder.json()
                .featuresToDisable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        converters.add(new MappingJackson2HttpMessageConverter(builder.build()));
    }

}
