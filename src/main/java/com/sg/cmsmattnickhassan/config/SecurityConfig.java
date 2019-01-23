package com.sg.cmsmattnickhassan.config;

import javax.inject.Inject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Inject
    UserDetailsService userDetails;

//    @Autowired
//    public void configureGlobalInMemory(AuthenticationManagerBuilder auth) throws Exception {
//        auth
//                .inMemoryAuthentication()
//                .withUser("user").password("password").roles("USER")
//                .and()
//                .withUser("admin").password("password").roles("ADMIN", "USER");
//    }
    

    @Autowired
    public void configureGlobalInDB(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetails).passwordEncoder(bCryptPasswordEncoder());
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/admin").hasRole("ADMIN")
                .antMatchers("/displayPostApprovalPage").hasRole("ADMIN")
                .antMatchers("/", "/home").permitAll()
                .antMatchers("/css/**", "/js/**", "/fonts/**").permitAll()
                .antMatchers("/displayPostDetail").permitAll()
                .antMatchers("/GetPostByTag").permitAll()
                .antMatchers("/displayPostViewAll").permitAll()
                .antMatchers("/displayTagDetails").permitAll()
                .anyRequest().hasRole("USER")
                .and()
                .formLogin()
                .loginPage("/login")
                .failureUrl("/login?login_error=1")
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/")
                .permitAll();
    }
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
