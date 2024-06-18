package com.moviedekho.MovieDekhoDiscoveryServer;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class MovieDekhoDiscoveryServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MovieDekhoDiscoveryServerApplication.class, args);
    }

}
