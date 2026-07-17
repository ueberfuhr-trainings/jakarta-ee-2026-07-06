package de.schulung.spring.todos.persistence;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Persistenz-Konfiguration in Spring: DataSource (H2), JPA (Hibernate als
 * Provider) und Transaktionen. Was in der Jakarta-EE-App in server.xml
 * (dataSource) und persistence.xml (persistence-unit) steht, wird hier
 * programmatisch als Spring-Beans definiert. Spring Data JPA wird über
 * {@link EnableJpaRepositories} für das Repository-Paket aktiviert.
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "de.schulung.spring.todos.persistence")
public class PersistenceConfig {

    @Bean
    public DataSource dataSource(
            @Value("${todos.db.url:jdbc:h2:file:./.localdb/todos-spring;AUTO_SERVER=TRUE}") String url) {
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

        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        emf.setJpaVendorAdapter(adapter);

        Properties props = new Properties();
        props.setProperty("hibernate.hbm2ddl.auto", "update");
        props.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        props.setProperty("hibernate.show_sql", "false");
        emf.setJpaProperties(props);
        return emf;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

}
