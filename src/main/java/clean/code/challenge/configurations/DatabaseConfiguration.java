package clean.code.challenge.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories({"clean.code.challenge.repositories"})
@EnableTransactionManagement
public class DatabaseConfiguration {

}
