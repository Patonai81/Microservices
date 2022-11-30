package hu.webuni.bonus;

import hu.webuni.security.config.SecurityConfig;
import hu.webuni.security.filter.JwtFilter;
import hu.webuni.security.service.JwtService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackageClasses = {
		JwtFilter.class,BonusApplication.class, JwtService.class, SecurityConfig.class
})
public class BonusApplication {

	public static void main(String[] args) {
		SpringApplication.run(BonusApplication.class, args);
	}

}
