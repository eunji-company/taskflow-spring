package indiv.abko.taskflow.global.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.Date;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import indiv.abko.taskflow.global.jwt.JwtUtil;
import io.jsonwebtoken.Claims;

@ExtendWith(MockitoExtension.class)
class JwtBlacklistUnitTest {

	@InjectMocks
	private JwtBlacklist jwtBlacklist;

	@Mock
	private JwtUtil jwtUtil;

	@Test
	@DisplayName("토큰을 블랙리스트에 추가하고 조회한다")
	void addToBlacklistAndCheck() {
		// given
		String token = "test-token";
		Claims claims = mock(Claims.class);
		Date expiration = new Date(System.currentTimeMillis() + 10000); // 10초 후 만료

		given(jwtUtil.parseClaims(token)).willReturn(claims);
		given(claims.getExpiration()).willReturn(expiration);

		// when
		jwtBlacklist.addToBlacklist(token);

		// then
		assertThat(jwtBlacklist.isBlacklisted(token)).isTrue();
		assertThat(jwtBlacklist.isBlacklisted("other-token")).isFalse();
	}

	@Test
	@DisplayName("만료된 토큰만 블랙리스트에서 제거한다")
	void cleanupOnlyExpiredTokens() {
		// given
		String expiredToken = "expired-token";
		String validToken = "valid-token";

		Claims expiredClaims = mock(Claims.class);
		Date pastDate = new Date(System.currentTimeMillis() - 10000); // 10초 전 만료
		given(jwtUtil.parseClaims(expiredToken)).willReturn(expiredClaims);
		given(expiredClaims.getExpiration()).willReturn(pastDate);

		Claims validClaims = mock(Claims.class);
		Date futureDate = new Date(System.currentTimeMillis() + 10000); // 10초 후 만료
		given(jwtUtil.parseClaims(validToken)).willReturn(validClaims);
		given(validClaims.getExpiration()).willReturn(futureDate);

		jwtBlacklist.addToBlacklist(expiredToken);
		jwtBlacklist.addToBlacklist(validToken);

		// when
		jwtBlacklist.cleanupExpiredTokens();

		// then
		assertThat(jwtBlacklist.isBlacklisted(expiredToken)).isFalse(); // 만료된 토큰은 제거됨
		assertThat(jwtBlacklist.isBlacklisted(validToken)).isTrue();   // 유효한 토큰은 남아있음
	}
}