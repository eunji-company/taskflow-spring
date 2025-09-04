package indiv.abko.taskflow.global.auth;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.servlet.HandlerExceptionResolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
class CustomAuthenticationEntryPointTest {

	private CustomAuthenticationEntryPoint entryPoint;

	@Mock
	private HandlerExceptionResolver mockResolver;

	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpServletResponse response;

	@Mock
	private AuthenticationException authException;

	@BeforeEach
	void setUp() {
		// 실제 리졸버 대신 Mock 리졸버를 주입합니다.
		entryPoint = new CustomAuthenticationEntryPoint(mockResolver);
	}

	@Test
	@DisplayName("commence가 호출되면, HandlerExceptionResolver에게 예외 처리를 위임한다")
	void commence() {
		// when
		entryPoint.commence(request, response, authException);

		// then
		// resolver의 resolveException이 정확한 인자들로 호출되었는지 검증합니다.
		verify(mockResolver).resolveException(request, response, null, authException);
	}
}

