package com.example.Personal_Travel_Concierge.security;


import com.example.Personal_Travel_Concierge.entity.user.RoleEntity;
import com.example.Personal_Travel_Concierge.repository.user.RoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final CustomUserDetailsService customUserDetailsService;
    private final JWTAuthEntryPoint jwtAuthEntryPoint;
    private final RoleRepository roleRepository;

    @Value("${PUBLIC_SECURITY_API}")
    private String publicSecurityApi;

    @Value("${PRIVATE_SECURITY_API}")
    private String privateSecurityApi;


    @Autowired
    public SecurityConfig(CustomUserDetailsService customUserDetailsService,
                          JWTAuthEntryPoint jwtAuthEntryPoint,
                          RoleRepository roleRepository){
        this.customUserDetailsService = customUserDetailsService;
        this.jwtAuthEntryPoint = jwtAuthEntryPoint;
        this.roleRepository = roleRepository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(configure-> configure.authenticationEntryPoint(jwtAuthEntryPoint))
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(publicSecurityApi).permitAll()
                        .requestMatchers(privateSecurityApi).hasAuthority("ADMIN")
                        .anyRequest().authenticated()
                )
                .httpBasic(withDefaults());
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JWTAuthenticationFilter jwtAuthenticationFilter() {
        return new JWTAuthenticationFilter();
    }

    @PostConstruct
    public void role(){
        RoleEntity userRole = new RoleEntity("USER");
        boolean existsUserRole = roleRepository.existsByRole(userRole.getRole());
        if(!existsUserRole){
            roleRepository.save(userRole);
        }

        RoleEntity adminRole = new RoleEntity("ADMIN");
        boolean existsAdminRole = roleRepository.existsByRole(adminRole.getRole());
        if(!existsAdminRole){
            roleRepository.save(adminRole);
        }
    }
}
