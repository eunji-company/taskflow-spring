package indiv.abko.taskflow.global.auth;

import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import indiv.abko.taskflow.domain.user.entity.UserRole;

public class WithMockAuthMemberSecurityContextFactory implements WithSecurityContextFactory<WithMockAuthMember> {
	@Override
	public SecurityContext createSecurityContext(WithMockAuthMember withMockAuthMember) {
		SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
		AuthMember authMember = new AuthMember(withMockAuthMember.memberId());

		List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(UserRole.USER.getKey()));
		Authentication authentication = new UsernamePasswordAuthenticationToken(authMember, null, authorities);

		securityContext.setAuthentication(authentication);
		return securityContext;
	}
}
