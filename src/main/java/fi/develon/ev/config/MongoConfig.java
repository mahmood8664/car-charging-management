package fi.develon.ev.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * @author mahmood
 * @since 9/12/21
 */
@Configuration
@EnableMongoRepositories(basePackages = "fi.develon.ev.repository")
@EnableMongoAuditing
public class MongoConfig {
}
