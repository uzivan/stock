package com.example.stock.config;


import com.example.stock.security.jwt.JwtConfigurer;
import com.example.stock.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;


@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/api/login").permitAll()
                .antMatchers(HttpMethod.GET,"/products", "/products/*", "/orders", "/orders/*").hasAnyRole("USER", "SALESMAN")
                .antMatchers(HttpMethod.POST, "/orders/new").hasAnyRole("USER")
                .antMatchers(HttpMethod.DELETE, "/products/*", "/orders/*").hasRole("SALESMAN")
                .antMatchers(HttpMethod.PUT, "/products/*", "/orders/*").hasRole("SALESMAN")
                .antMatchers(HttpMethod.POST, "/products/new").hasAnyRole("SALESMAN")
                .and()
                .apply(new JwtConfigurer(jwtTokenProvider));
    }
}
