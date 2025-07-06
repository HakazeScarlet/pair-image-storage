package com.github.hakazescarlet.pairimagestorage.configuration;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Configuration
@ComponentScan
public class PairImageStorageConfiguration {

    public static final String IMAGE_KEY = "image";

    @Value("${spring.data.mongodb.database}")
    private String dbCollection;

    @Value("${mongo.datasource.url}")
    private String datasource;

    @Bean
    public HttpClient createHttpClient() {
        return HttpClient.newHttpClient();
    }

    @Bean
    public MongoClient createMongoClient() {
        CodecRegistry pojoCodecRegistry = fromRegistries(
            MongoClientSettings.getDefaultCodecRegistry(),
            fromProviders(PojoCodecProvider.builder().automatic(true).build())
        );

        MongoCredential scramSha1Credential = MongoCredential.createScramSha1Credential(
            System.getenv("DB_USERNAME"),
            dbCollection,
            System.getenv("DB_PASSWORD").toCharArray()
        );

        MongoClientSettings settings = MongoClientSettings.builder()
            .applyConnectionString(new ConnectionString(datasource))
            .credential(scramSha1Credential)
            .codecRegistry(pojoCodecRegistry)
            .build();

        return MongoClients.create(settings);
    }
}
