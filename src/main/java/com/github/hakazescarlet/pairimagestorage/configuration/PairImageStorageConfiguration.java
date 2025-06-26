package com.github.hakazescarlet.pairimagestorage.configuration;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.http.HttpClient;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Configuration
@ComponentScan
public class PairImageStorageConfiguration {

    @Bean
    public HttpClient createHttpClient() {
        return HttpClient.newHttpClient();
    }

    @Bean
    public MongoClient createMongoClient() {
        try {
            CodecRegistry pojoCodecRegistry = fromRegistries(
                    MongoClientSettings.getDefaultCodecRegistry(),
                    fromProviders(PojoCodecProvider.builder().automatic(true).build())
            );

            MongoCredential scramSha1Credential = MongoCredential.createScramSha1Credential("admin", "image_db", "pass".toCharArray());

            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(new ConnectionString("mongodb://localhost:27017"))
                    .credential(scramSha1Credential)
                    .codecRegistry(pojoCodecRegistry)
                    .build();

            return MongoClients.create(settings);
        } catch (Exception exception) {
            // TODO: handle custom exception
            throw new RuntimeException(exception);
        }
    }

    @PostConstruct
    @PreDestroy
    public void createDirectory() {
        Path path = Paths.get("temp");
        if (Files.notExists(path)) {
            try {
                Files.createDirectory(path);
            } catch (IOException e) {
                throw new DirectoryCreatingException("Failed to create directory /temp", e);
            }
        }
    }

    private static class DirectoryCreatingException extends RuntimeException {
        public DirectoryCreatingException(String message, Exception e) {
            super(message, e);
        }
    }
}
