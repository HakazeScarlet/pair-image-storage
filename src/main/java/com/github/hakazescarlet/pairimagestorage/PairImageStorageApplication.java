package com.github.hakazescarlet.pairimagestorage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class PairImageStorageApplication {

    public static void main(String[] args) {
        SpringApplication.run(PairImageStorageApplication.class, args);
    }

}
