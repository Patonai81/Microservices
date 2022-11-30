package hu.webuni.flights;

import hu.webuni.security.config.SecurityConfig;
import hu.webuni.security.filter.JwtFilter;
import hu.webuni.security.service.JwtService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackageClasses = {SecurityConfig.class,FlightsSecurityConfig.class, JwtService.class, JwtFilter.class})
public class FlightsApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlightsApplication.class, args);
	}

}
