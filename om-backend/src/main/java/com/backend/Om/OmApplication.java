package com.backend.Om;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class OmApplication /*extends SpringBootServletInitializer*/ {

/*	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(OmApplication.class);
	}
*/
	public static void main(String[] args) {
		SpringApplication.run(OmApplication.class, args);
	}

}