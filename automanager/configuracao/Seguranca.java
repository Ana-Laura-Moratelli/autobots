package com.autobots.automanager.configuracao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.autobots.automanager.adaptadores.UserDetailsServiceImpl;
import com.autobots.automanager.filtros.Autenticador;
import com.autobots.automanager.filtros.Autorizador;
import com.autobots.automanager.jwt.ProvedorJwt;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class Seguranca {

    private static final String[] rotasPublicas = { "/", "/login" };

    private final UserDetailsServiceImpl servico;
    private final ProvedorJwt provedorJwt;

    public Seguranca(UserDetailsServiceImpl servico, ProvedorJwt provedorJwt) {
        this.servico = servico;
        this.provedorJwt = provedorJwt;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable();

        http.authorizeHttpRequests(auth -> auth
                .antMatchers(rotasPublicas).permitAll()
                .anyRequest().authenticated());

        http.addFilter(new Autenticador(authenticationManager(http.getSharedObject(AuthenticationConfiguration.class)), provedorJwt));
        http.addFilter(new Autorizador(authenticationManager(http.getSharedObject(AuthenticationConfiguration.class)), provedorJwt, servico));

        http.sessionManagement().sessionCreationPolicy(org.springframework.security.config.http.SessionCreationPolicy.STATELESS);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(servico);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }
}
