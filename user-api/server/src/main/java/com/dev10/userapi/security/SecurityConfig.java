package com.dev10.userapi.security;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http.cors();

        http.authorizeRequests()
                .antMatchers("/create_account").permitAll()
                .antMatchers("/authenticate").permitAll()
                .antMatchers("/refresh_token").permitAll()
                .antMatchers("/**").denyAll() // if we get to this point (i.e. nothing has matched) then deny all requests
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
