package indiv.abko.taskflow.global.jwt;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.util.ReflectionTestUtils;

import indiv.abko.taskflow.domain.user.entity.UserRole;
import indiv.abko.taskflow.global.auth.AuthMember;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@ExtendWith(MockitoExtension.class)
class JwtUtilUnitTest {

	private final String TEST_SECRET_KEY = "testtesttesttesttesttesttesttesttesttest";
	private final long TEST_ACCESS_TOKEN_VALIDITY_SECONDS = 3600;
	private JwtUtil jwtUtil;

	@BeforeEach
	void setUp() {
		jwtUtil = new JwtUtil();
		ReflectionTestUtils.setField(jwtUtil, "secretKey", TEST_SECRET_KEY);
		ReflectionTestUtils.setField(jwtUtil, "accessTokenValiditySeconds", TEST_ACCESS_TOKEN_VALIDITY_SECONDS);
		jwtUtil.init();
	}

	@Test
	@DisplayName("AuthMember 정보를 담은 액세스 토큰을 생성한다")
	void createAccessToken() {
		// given
		AuthMember authMember = new AuthMember(1L, UserRole.USER);

		// when
		String accessToken = jwtUtil.createAccessToken(authMember);

		// then
		assertThat(accessToken).isNotNull();
		assertThat(jwtUtil.validateToken(accessToken)).isTrue();
	}

	@Test
	@DisplayName("유효한 토큰을 검증하면 true를 반환한다")
	void validateToken_success() {
		// given
		AuthMember authMember = new AuthMember(1L, UserRole.USER);
		String accessToken = jwtUtil.createAccessToken(authMember);

		// when
		boolean isValid = jwtUtil.validateToken(accessToken);

		// then
		assertThat(isValid).isTrue();
	}

	@Test
	@DisplayName("만료된 토큰을 검증하면 false를 반환한다")
	void validateToken_expired() {
		// given
		AuthMember authMember = new AuthMember(1L, UserRole.USER);
		// 유효시간을 음수값으로 주어 만료된 토큰을 생성
		String accessToken = TestJwtUtil.createAccessToken(authMember, -1, TEST_SECRET_KEY);

		// when
		boolean isValid = jwtUtil.validateToken(accessToken);

		// then
		assertThat(isValid).isFalse();
	}

	@Test
	@DisplayName("잘못된 서명을 가진 토큰을 검증하면 false를 반환한다")
	void validateToken_invalidSignature() {
		// given
		AuthMember authMember = new AuthMember(1L, UserRole.USER);
		String validAccessToken = jwtUtil.createAccessToken(authMember);

		// when
		// 유효하지 않은 시크릿 키로 토큰을 다시 생성하여 서명을 조작
		String invalidSecretKey = "invalidinvalidinvalidinvalidinvalidinvalid";
		String tamperedToken = Jwts.builder()
			.setSubject(String.valueOf(authMember.memberId()))
			.claim("auth", authMember.userRole().name())
			.setIssuedAt(new Date())
			.signWith(Keys.hmacShaKeyFor(invalidSecretKey.getBytes()), SignatureAlgorithm.HS256)
			.setExpiration(Date.from(Instant.now().plusSeconds(TEST_ACCESS_TOKEN_VALIDITY_SECONDS)))
			.compact();

		boolean isValid = jwtUtil.validateToken(tamperedToken);

		// then
		assertThat(isValid).isFalse();
	}

	@Test
	@DisplayName("토큰 문자열을 Jwt 객체로 변환한다")
	void getJwt() {
		// given
		long memberId = 1L;
		UserRole userRole = UserRole.USER;
		AuthMember authMember = new AuthMember(memberId, userRole);
		String accessToken = jwtUtil.createAccessToken(authMember);

		// when
		Jwt jwt = jwtUtil.getJwt(accessToken);

		// then
		assertThat(jwt.getSubject()).isEqualTo(String.valueOf(memberId));
		assertThat(jwt.getClaimAsString("auth")).isEqualTo(userRole.name());
		assertDoesNotThrow(jwt::getIssuedAt);
		assertDoesNotThrow(jwt::getExpiresAt);
	}
}
