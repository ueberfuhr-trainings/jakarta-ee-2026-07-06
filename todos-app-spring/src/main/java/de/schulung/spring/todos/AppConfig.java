package de.schulung.spring.todos;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import de.schulung.spring.todos.boundary.WebConfig;
import de.schulung.spring.todos.persistence.PersistenceConfig;

/**
 * Wurzel-Konfiguration: scannt die Anwendungs-Beans (Controller, Services,
 * Mapper, Initializer) und lädt {@code application.properties}. Die
 * {@code @Configuration}-Klassen ({@link WebConfig}, {@link PersistenceConfig})
 * werden bewusst vom Scan ausgeschlossen und im {@code WebAppInitializer}
 * explizit registriert.
 */
@Configuration
@ComponentScan(
        basePackages = "de.schulung.spring.todos",
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ANNOTATION,
                classes = Configuration.class))
@PropertySource("classpath:application.properties")
public class AppConfig {

    @Bean
    static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

}
