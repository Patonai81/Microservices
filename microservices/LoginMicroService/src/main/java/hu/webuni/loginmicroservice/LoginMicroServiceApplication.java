package hu.webuni.loginmicroservice;

import hu.webuni.security.config.SecurityConfig;
import hu.webuni.security.filter.JwtFilter;
import hu.webuni.security.service.JwtService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackageClasses = {LoginMicroServiceApplication.class, JwtService.class, SecurityConfig.class})
public class LoginMicroServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoginMicroServiceApplication.class, args);
    }

}
