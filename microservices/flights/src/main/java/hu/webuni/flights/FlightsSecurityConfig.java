package hu.webuni.flights;

import hu.webuni.security.filter.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
@Configuration
public class FlightsSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    JwtFilter jwtFilter;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                 .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/api/flight/**").hasAnyAuthority("search")
                .anyRequest().permitAll();

         http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }



}
