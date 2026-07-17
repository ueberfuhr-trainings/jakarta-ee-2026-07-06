package de.schulung.spring.todos;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Persistenz-Konfiguration NUR für die Tests (Profil {@code test}): statt der
 * vom Liberty per JNDI bereitgestellten {@code EntityManagerFactory} wird hier
 * eine eigene In-Memory-H2 samt {@code EntityManagerFactory} (Hibernate als
 * Provider) und ein resource-lokaler {@code JpaTransactionManager} in den
 * Spring-Context gebaut. So laufen die Tests komplett in-JVM, ohne Liberty/JNDI.
 */
@Configuration
@Profile("test")
public class TestPersistenceConfig {

    @Bean
    public DataSource dataSource(
            @Value("${todos.db.url:jdbc:h2:mem:todos-test;DB_CLOSE_DELAY=-1}") String url) {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("org.h2.Driver");
        ds.setUrl(url);
        ds.setUsername("sa");
        ds.setPassword("sa");
        return ds;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        emf.setPackagesToScan("de.schulung.spring.todos.persistence");

        emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        Properties props = new Properties();
        props.setProperty("hibernate.hbm2ddl.auto", "update");
        props.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        emf.setJpaProperties(props);
        return emf;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

}
