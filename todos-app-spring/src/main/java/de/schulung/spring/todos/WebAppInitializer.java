package de.schulung.spring.todos;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import de.schulung.spring.todos.boundary.WebConfig;
import de.schulung.spring.todos.persistence.PersistenceConfig;

/**
 * Programmatischer Ersatz für web.xml (Servlet 3.0+): registriert die Spring
 * {@link DispatcherServlet} und bindet sie an {@code /}. Ersetzt damit den
 * Deployment-Descriptor, den man bei klassischem Servlet/JSP oder Jakarta EE
 * teilweise noch pflegt.
 */
public class WebAppInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(AppConfig.class, WebConfig.class, PersistenceConfig.class);

        DispatcherServlet dispatcherServlet = new DispatcherServlet(context);
        ServletRegistration.Dynamic registration =
                servletContext.addServlet("dispatcher", dispatcherServlet);
        registration.setLoadOnStartup(1);
        registration.addMapping("/");
    }

}
