package indiv.abko.taskflow.support;

import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import indiv.abko.taskflow.global.auth.CustomAuthenticationEntryPoint;
import indiv.abko.taskflow.global.config.SecurityConfig;
import indiv.abko.taskflow.global.jwt.JwtAuthenticationConverter;
import indiv.abko.taskflow.global.jwt.JwtUtil;

@Import(SecurityConfig.class)
public abstract class ControllerTestSupport {

	@MockitoBean
	protected JwtUtil jwtUtil;

	@MockitoBean
	protected JwtAuthenticationConverter jwtAuthenticationConverter;

	@MockitoBean
	protected CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
}
