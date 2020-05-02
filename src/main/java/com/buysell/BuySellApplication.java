package com.buysell;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EntityScan
@EnableJpaAuditing
@EnableCaching
@PropertySource({"classpath:application.properties", "classpath:application-real.properties"})
public class BuySellApplication {

	public static void main(String[] args) {
		SpringApplication.run(BuySellApplication.class, args);
	}

}
