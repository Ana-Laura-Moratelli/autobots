package com.comunicacao.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {
    "com.comunicacao.api.repositorios",
    "com.autobots.automanager.repositorios"
})
@EntityScan(basePackages = {
    "com.comunicacao.api.entidades",
    "com.autobots.automanager.entidades"
})
@ComponentScan(basePackages = {
    "com.autobots.automanager" 
})
public class ApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

}
