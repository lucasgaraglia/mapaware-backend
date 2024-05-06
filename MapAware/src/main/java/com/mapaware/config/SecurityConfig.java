package com.mapaware.config;


import com.mapaware.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity

// con esta annotation se pueden configurar los accesos a los endpoints
//desde el controlador usando annotations
@EnableMethodSecurity
public class SecurityConfig {

//  1. SECURITY FILTER CHAIN

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())

                .httpBasic(Customizer.withDefaults())

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .build();
    }

    // COMPONENTE 2. AUTHENTICATION MANAGER. SE CONECTA CON LOS PROVIDERS
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    //    COMPONENTE 3. AUTHENTICATION PROVIDER, TIENE DOS subCOMPONENTES:
//    - PasswordEncoder (encripta y compara contrasenias)
//    - UserDetailsService (se conecta con la bdd y trae users)
    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsServiceImpl userDetailsService){
        // hay muchos tipos de providers, el DaoAuth se conecta a la bdd y trae los users
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
//        inyectamos el UserDetailsServiceImpl
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    //    COMPONENTE 4. PASSWORD ENCODER, DENTRO DE AUTH PROVIDER
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}