package de.schulung.spring.todos.persistence;

import javax.persistence.EntityManagerFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.transaction.jta.JtaTransactionManager;

/**
 * Persistenz: JPA und DataSource stellt der Open Liberty bereit. Die vom
 * Container verwaltete (JTA-)Persistence-Unit {@code todos} wird über einen
 * {@code persistence-unit-ref} (siehe web.xml) unter
 * {@code java:comp/env/persistence/todos} ins JNDI gebunden; Spring holt sich
 * die {@link EntityManagerFactory} lediglich per JNDI-Lookup und nutzt den
 * JTA-TransactionManager des Servers.
 *
 * <p>Diese Klasse ist frei von Test-Wissen. In den Tests wird sie zwar geladen,
 * ihre Beans {@code entityManagerFactory}/{@code transactionManager} aber von
 * {@code TestPersistenceConfig} überschrieben (Bean-Overriding) – der JNDI-Lookup
 * läuft dort also nie.</p>
 */
@Configuration
public class LibertyPersistenceConfig {

    @Bean
    public JndiObjectFactoryBean entityManagerFactory() {
        JndiObjectFactoryBean jndi = new JndiObjectFactoryBean();
        jndi.setJndiName("java:comp/env/persistence/todos");
        jndi.setExpectedType(EntityManagerFactory.class);
        // Beim Start ist die EMF vorhanden; kein Lazy-Lookup nötig.
        jndi.setLookupOnStartup(true);
        return jndi;
    }

    @Bean
    public JtaTransactionManager transactionManager() {
        // Findet UserTransaction/TransactionManager des Liberty automatisch.
        return new JtaTransactionManager();
    }

}
