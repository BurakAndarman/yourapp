package com.example.SpringVue;

import com.example.SpringVue.Config.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties({RsaKeyProperties.class})
@SpringBootApplication
public class SpringVueApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringVueApplication.class, args);
	}

}