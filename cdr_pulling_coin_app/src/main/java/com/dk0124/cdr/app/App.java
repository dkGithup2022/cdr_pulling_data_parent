package com.dk0124.cdr.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@ComponentScan(basePackages = {"com.dk0124.cdr"})
@EnableJpaRepositories(basePackages={"com.dk0124.cdr"})
@EntityScan(basePackages = {"com.dk0124.cdr"})
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
