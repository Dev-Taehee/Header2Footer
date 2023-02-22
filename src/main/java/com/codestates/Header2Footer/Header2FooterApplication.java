package com.codestates.Header2Footer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class Header2FooterApplication {

	public static void main(String[] args) {
		SpringApplication.run(Header2FooterApplication.class, args);
	}

}
