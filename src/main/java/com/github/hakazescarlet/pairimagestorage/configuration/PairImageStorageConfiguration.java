package com.github.hakazescarlet.pairimagestorage.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;

@Configuration
@ComponentScan
public class PairImageStorageConfiguration {

    @Bean
    public HttpClient createHttpClient() {
        return HttpClient.newHttpClient();
    }
}
