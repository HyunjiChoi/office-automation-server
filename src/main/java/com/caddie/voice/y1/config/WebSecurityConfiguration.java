package com.caddie.voice.y1.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/**").permitAll()
                .anyRequest().permitAll();

        http.csrf().disable();
    }
    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers("/api-docs", "/swagger-ui.html", "/swagger-resources/**", "/webjars/**",
                        "/swagger/**");
    }



}