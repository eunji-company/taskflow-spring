package indiv.abko.taskflow.global.jwt;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import indiv.abko.taskflow.domain.auth.exception.AuthErrorCode;
import indiv.abko.taskflow.global.auth.JwtBlacklist;
import indiv.abko.taskflow.global.dto.CommonResponse;
import indiv.abko.taskflow.global.exception.ErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtBlacklistFilter extends OncePerRequestFilter {

	private final JwtBlacklist jwtBlacklist;
	private final ObjectMapper objectMapper;

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws ServletException, IOException {

		String token = resolveToken(request);

		if (StringUtils.hasText(token) && jwtBlacklist.isBlacklisted(token)) {
			sendErrorResponse(response, AuthErrorCode.INVALID_TOKEN);
			return; // 필터 체인 중단
		}

		filterChain.doFilter(request, response);
	}

	private String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(JwtUtil.BEARER_PREFIX)) {
			return bearerToken.substring(JwtUtil.BEARER_PREFIX.length());
		}
		return null;
	}

	private void sendErrorResponse(
		HttpServletResponse response,
		ErrorCode errorCode
	) throws IOException {
		response.setStatus(errorCode.getHttpStatus().value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("UTF-8");

		CommonResponse<Void> errorResponse = CommonResponse.failure(errorCode.getMessage(), null);
		response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
	}
}
