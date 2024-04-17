package com.woowa.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

@Configuration
@EnableMongoAuditing
public class MongoConfig {
    @Bean
    public MappingMongoConverter mappingMongoConverter(MongoDatabaseFactory mongoDatabaseFactory,
                                                       MongoMappingContext mongoMappingContext)  {

        DefaultDbRefResolver defaultDbRefResolver = new DefaultDbRefResolver(mongoDatabaseFactory);
        MappingMongoConverter mappingMongoConverter = new MappingMongoConverter(defaultDbRefResolver, mongoMappingContext);
        mappingMongoConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
        return mappingMongoConverter;

    }
}
