package indiv.abko.taskflow.global.jwt;

import static org.assertj.core.api.Assertions.*;

import java.time.Instant;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import indiv.abko.taskflow.domain.user.entity.UserRole;
import indiv.abko.taskflow.global.auth.AuthMember;

class JwtAuthenticationConverterUnitTest {

	private final JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

	@Test
	@DisplayName("Jwt 객체를 JwtAuthenticationToken으로 변환한다")
	void convert() {
		// given
		long memberId = 1L;
		String userRole = UserRole.USER.getKey();
		String tokenValue = "test-token";

		Jwt jwt = Jwt.withTokenValue(tokenValue)
			.header("alg", "HS256")
			.subject(String.valueOf(memberId))
			.claim("auth", userRole)
			.issuedAt(Instant.now())
			.expiresAt(Instant.now().plusSeconds(60))
			.build();

		// when
		JwtAuthenticationToken authentication = converter.convert(jwt);

		// then
		assertThat(authentication).isNotNull();
		assertThat(authentication.getPrincipal()).isInstanceOf(AuthMember.class);

		AuthMember authMember = (AuthMember)authentication.getPrincipal();
		assertThat(authMember.memberId()).isEqualTo(memberId);

		assertThat(authentication.getAuthorities()).hasSize(1);
		GrantedAuthority authority = authentication.getAuthorities().iterator().next();
		assertThat(authority.getAuthority()).isEqualTo(userRole);
	}
}
