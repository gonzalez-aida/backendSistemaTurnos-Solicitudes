package mx.edu.uteq.backS.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {
    
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .authorizeHttpRequests((authHttp) -> authHttp
            .requestMatchers(HttpMethod.GET, "/api/solicitud/**").permitAll()
            // Vista solicitudes maestro
            .requestMatchers(HttpMethod.GET, "/api/solicitud/{idProfesor}/pendientes").hasRole("MAESTRO")
            .requestMatchers(HttpMethod.GET, "/api/solicitud/{idProfesor}/rev-fin").hasRole("MAESTRO")
            .requestMatchers(HttpMethod.PUT, "/api/solicitud/{id}/estado").hasRole("MAESTRO")

            // Vista solicitudes alumno
            .requestMatchers(HttpMethod.GET, "/api/solicitud/id/{matricula}/grupo").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/solicitud/profesores/{matricula}").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/solicitud/alumno/{matricula}/detalle").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/solicitud").permitAll()
            .anyRequest().authenticated())
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .oauth2ResourceServer(resourceServer -> 
                resourceServer.jwt(jwt -> 
                    jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));

        return http.build();
    }

    // Bean para la configuración de CORS
 @Bean
 CorsConfigurationSource corsConfigurationSource() {
 CorsConfiguration configuration = new CorsConfiguration();
 configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
 configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
 configuration.setAllowedHeaders(Arrays.asList("*"));
 configuration.setAllowCredentials(true);
 UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
 source.registerCorsConfiguration("/**", configuration);
return source;
 }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthoritiesClaimName("role");
        authoritiesConverter.setAuthorityPrefix("");
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
        return converter;
    }
}
