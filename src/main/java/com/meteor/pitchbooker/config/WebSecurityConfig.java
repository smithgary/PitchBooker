package com.meteor.pitchbooker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/", "/home").permitAll()
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .loginPage("/login")
                    .permitAll()
                    .and()
                .logout()
                    .permitAll();
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService(){
        UserDetails user =
                User.withDefaultPasswordEncoder()
                    .username("jean@jean.com")
                    .password("Testing123")
                    .roles("USER")
                    .build();
        UserDetails adminUser =
                User.withDefaultPasswordEncoder()
                        .username("joy@jean.com")
                        .password("easy")
                        .roles("USER", "ADMIN")
                        .build();
        List<UserDetails> users = new ArrayList<>();
        users.add(user);
        users.add(adminUser);
        return new InMemoryUserDetailsManager(users);
        //return new InMemoryUserDetailsManager(user);
    }
}
