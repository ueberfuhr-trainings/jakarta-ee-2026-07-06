package de.schulung.spring.todos;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import de.schulung.spring.todos.boundary.WebConfig;
import de.schulung.spring.todos.persistence.LibertyPersistenceConfig;
import de.schulung.spring.todos.persistence.PersistenceConfig;

/**
 * Programmatische Registrierung (Servlet 3.0+) der Spring
 * {@link DispatcherServlet} an {@code /}. Der Deployment-Descriptor web.xml
 * bleibt daneben nur für den {@code persistence-unit-ref} bestehen, über den
 * Liberty die JPA-EntityManagerFactory ins JNDI bindet.
 */
public class WebAppInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(AppConfig.class, WebConfig.class,
                PersistenceConfig.class, LibertyPersistenceConfig.class);

        DispatcherServlet dispatcherServlet = new DispatcherServlet(context);
        ServletRegistration.Dynamic registration =
                servletContext.addServlet("dispatcher", dispatcherServlet);
        registration.setLoadOnStartup(1);
        registration.addMapping("/");
    }

}
