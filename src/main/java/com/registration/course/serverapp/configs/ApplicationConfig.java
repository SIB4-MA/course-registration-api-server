package com.registration.course.serverapp.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.registration.course.serverapp.api.authentication.AppUserDetailService;

import lombok.RequiredArgsConstructor;
 
@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {
    
    private final AppUserDetailService appUserDetailService;
    
  private final PasswordEncoder passwordEncoder;

    // @Bean
    // public UserDetailsService userDetailsService(){
    //     return username -> userRepository.findByUsername(username)
    //     .orElseThrow(() -> new UsernameNotFoundException("username atau email tidak ditemukan"));
    // }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(appUserDetailService); 
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    } 

}
