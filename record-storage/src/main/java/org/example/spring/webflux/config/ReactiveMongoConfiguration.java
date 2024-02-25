package org.example.spring.webflux.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import static org.example.spring.webflux.ApplicationProperties.MONGO_DB_NAME;

@Configuration
@EnableMongoRepositories
public class ReactiveMongoConfiguration extends AbstractReactiveMongoConfiguration {
    @Override
    protected String getDatabaseName() {
        return MONGO_DB_NAME;
    }
}
