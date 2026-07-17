package de.schulung.spring.todos.persistence;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Basis-Persistenzkonfiguration: aktiviert Spring Data JPA (Repositories) und
 * Spring-Transaktionsmanagement. Die eigentliche {@code EntityManagerFactory}
 * und der {@code transactionManager} kommen aus einer profilspezifischen
 * Konfiguration:
 * <ul>
 *   <li>{@link LibertyPersistenceConfig} (Profil {@code !test}): holt die vom
 *       Open Liberty verwaltete {@code EntityManagerFactory} per JNDI und nutzt
 *       den JTA-TransactionManager des Servers – JPA und DataSource stellt also
 *       der Container bereit.</li>
 *   <li>{@code TestPersistenceConfig} (Profil {@code test}, in den Test-Sourcen):
 *       baut eine In-Memory-H2 samt eigener {@code EntityManagerFactory} in den
 *       Spring-Context, damit die Tests ohne Liberty/JNDI laufen.</li>
 * </ul>
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackageClasses = PersistenceConfig.class)
public class PersistenceConfig {

}
