package com.aramdev.delivery.configuration;

import com.aramdev.delivery.controller.GlobalExceptionHandler;
import com.aramdev.delivery.util.CustomUserDetails;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import tools.jackson.databind.json.JsonMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ProblemDetail;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class HttpSecurityConfiguration {

    private final JsonMapper jsonMapper;
    private final GlobalExceptionHandler globalExceptionHandler;

    @Bean
    public SecurityFilterChain configureHttp(HttpSecurity http) {
        http.csrf(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);
        http.formLogin(AbstractHttpConfigurer::disable);
        http.cors(cors -> cors.configurationSource(request -> corsConfiguration()));

        http.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeHttpRequests(auth -> {
            auth.requestMatchers("/api/auth/login").permitAll();
            auth.requestMatchers("/api-docs").permitAll();
            auth.requestMatchers("/api-docs/**").permitAll();
            auth.requestMatchers("/swagger-ui/**").permitAll();
            auth.requestMatchers("/swagger-ui.html").permitAll();
            auth.requestMatchers("/swagger-ui/index.html").permitAll();
            auth.requestMatchers("/contrato/**").permitAll();
            auth.anyRequest().authenticated();
        });

        http.oauth2ResourceServer(oauth2 ->
               oauth2.authenticationEntryPoint(authenticationEntryPoint())
                .jwt(jwt -> {
                    jwt.jwtAuthenticationConverter(jwtAuthenticationConverter());
                }
        ));

        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder(@Value("${auth.jwt.secret}") String secret) {
        SecretKey key = new SecretKeySpec(
                secret.getBytes(java.nio.charset.StandardCharsets.UTF_8),
                "HmacSHA256"
        );

        return NimbusJwtDecoder.withSecretKey(key).build();
    }

    @Bean
    public JwtEncoder jwtEncoder(@Value("${auth.jwt.secret}") String secret) {
        SecretKey key = new SecretKeySpec(
                secret.getBytes(StandardCharsets.UTF_8),
                "HmacSHA256"
        );

        return new NimbusJwtEncoder(new ImmutableSecret<>(key));
    }

    private CorsConfiguration corsConfiguration() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("*"));

        return configuration;
    }

    private Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationConverter() {

        JwtGrantedAuthoritiesConverter authoritiesConverter =
                new JwtGrantedAuthoritiesConverter();

        authoritiesConverter.setAuthoritiesClaimName("permissions");
        authoritiesConverter.setAuthorityPrefix("");

        return jwt -> {

            List<GrantedAuthority> authorities =
                    authoritiesConverter.convert(jwt).stream().toList();

            CustomUserDetails userDetails = new CustomUserDetails(
                    Long.valueOf(jwt.getSubject()),
                    jwt.getClaimAsString("email"),
                    null,
                    authorities
            );

            AbstractAuthenticationToken authentication =
                    new AbstractAuthenticationToken(authorities) {

                        @Override
                        public Object getCredentials() {
                            return jwt;
                        }

                        @Override
                        public Object getPrincipal() {
                            return userDetails;
                        }
                    };

            authentication.setAuthenticated(true);

            return authentication;
        };
    }
    
    private AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, exception) -> {
            ProblemDetail problemDetail = globalExceptionHandler.handle(exception);
            jsonMapper.writeValue(response.getOutputStream(), problemDetail);
        };
    }

}