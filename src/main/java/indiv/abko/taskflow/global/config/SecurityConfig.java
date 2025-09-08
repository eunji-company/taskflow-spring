package indiv.abko.taskflow.global.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import indiv.abko.taskflow.domain.auth.exception.AuthErrorCode;
import indiv.abko.taskflow.global.auth.CustomAuthenticationEntryPoint;
import indiv.abko.taskflow.global.exception.BusinessException;
import indiv.abko.taskflow.global.jwt.JwtAuthenticationConverter;
import indiv.abko.taskflow.global.jwt.JwtBlacklistFilter;
import indiv.abko.taskflow.global.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtUtil jwtUtil;
	private final JwtAuthenticationConverter jwtAuthenticationConverter;
	private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
	private final JwtBlacklistFilter jwtBlacklistFilter;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.csrf(AbstractHttpConfigurer::disable)
			.cors(Customizer.withDefaults())
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(auth -> auth
				// OPTIONS 요청은 CORS pre-flight를 위해 항상 허용
				.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
				.requestMatchers("/api/auth/**").permitAll()
				.anyRequest().authenticated()
			)
			.oauth2ResourceServer(oauth2 -> oauth2
				.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter))
			)
			.exceptionHandling(handler -> handler
				.authenticationEntryPoint(customAuthenticationEntryPoint) // 401
			)
			.addFilterBefore(jwtBlacklistFilter, BearerTokenAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public JwtDecoder jwtDecoder() {
		return token -> {
			if (!jwtUtil.validateToken(token)) {
				throw new BusinessException(AuthErrorCode.INVALID_TOKEN);
			}
			return jwtUtil.getJwt(token);
		};
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource(
		@Value("${cors.allowed-origins}") String origins,
		@Value("${cors.allowed-methods}") String methods,
		@Value("${cors.allowed-headers}") String headers,
		@Value("${cors.allow-credentials}") boolean allowCredentials
	) {
		CorsConfiguration cfg = new CorsConfiguration();
		cfg.setAllowedOrigins(Arrays.asList(origins.split(",")));
		cfg.setAllowedMethods(Arrays.asList(methods.split(",")));
		cfg.setAllowedHeaders(Arrays.asList(headers.split(",")));
		cfg.setAllowCredentials(allowCredentials);

		UrlBasedCorsConfigurationSource src = new UrlBasedCorsConfigurationSource();
		src.registerCorsConfiguration("/**", cfg);
		return src;
	}

}
