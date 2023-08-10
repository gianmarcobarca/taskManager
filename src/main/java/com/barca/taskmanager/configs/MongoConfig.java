package com.barca.taskmanager.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration("mongoConfig")
@EnableMongoAuditing // for timestamps
public class MongoConfig {
  @Bean
  MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
    return new MongoTransactionManager(dbFactory);
  }

  @Bean
  public ValidatingMongoEventListener validatingMongoEventListener(LocalValidatorFactoryBean validator) {
    return new ValidatingMongoEventListener(validator);
  }

  @Bean
  public LocalValidatorFactoryBean validator() {
    return new LocalValidatorFactoryBean();
  }
}
