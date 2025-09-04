package indiv.abko.taskflow.global.jwt;

import static org.assertj.core.api.Assertions.*;

import java.util.Collection;
import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import indiv.abko.taskflow.domain.user.entity.UserRole;
import indiv.abko.taskflow.global.auth.AuthMember;

class JwtAuthenticationTokenUnitTest {

	@Test
	@DisplayName("JwtAuthenticationToken 생성 시 principal, credentials, authorities가 올바르게 설정되고 인증 상태가 true가 된다")
	void createToken() {
		// given
		AuthMember principal = new AuthMember(1L, UserRole.USER);
		String credentials = "jwt-token-string";
		Collection<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(UserRole.USER.getKey()));

		// when
		JwtAuthenticationToken token = new JwtAuthenticationToken(principal, credentials, authorities);

		// then
		assertThat(token.getPrincipal()).isEqualTo(principal);
		assertThat(token.getCredentials()).isEqualTo(credentials);
		assertThat(token.getAuthorities()).isEqualTo(authorities);
		assertThat(token.isAuthenticated()).isTrue();
	}
}
