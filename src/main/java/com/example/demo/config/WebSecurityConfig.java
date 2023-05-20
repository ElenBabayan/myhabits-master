package com.example.demo.config;

import com.example.demo.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().cors().and()
                .headers()
                .addHeaderWriter(new StaticHeadersWriter("Access-Control-Allow-Origin", "*"))
                .addHeaderWriter(new StaticHeadersWriter("Access-Control-Allow-Methods", "POST, GET"))
                .addHeaderWriter(new StaticHeadersWriter("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept"))
                .addHeaderWriter(new StaticHeadersWriter("Access-Control-Expose-Headers", "Authorization"))
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(authenticationManager()), BasicAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests()
                .antMatchers(HttpMethod.GET, "/users/3").permitAll()
                .antMatchers(HttpMethod.GET, "/user").hasAnyAuthority(Role.USER.name(), Role.ADMIN.name())
                .antMatchers(HttpMethod.POST, "/habits").permitAll()
                .antMatchers(HttpMethod.GET, "/habits").permitAll()
                .antMatchers(HttpMethod.POST, "/habits/3").permitAll()
                .antMatchers(HttpMethod.GET, "/habits/6").permitAll()
                .antMatchers(HttpMethod.GET, "/habits/user/3").permitAll()
                .antMatchers(HttpMethod.PUT, "/habits/6").permitAll()
                .antMatchers(HttpMethod.DELETE, "/habits/6").permitAll()
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                .antMatchers(HttpMethod.POST, "/signup").permitAll()
                .anyRequest().authenticated();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }
}
