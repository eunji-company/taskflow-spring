package indiv.abko.taskflow.global.jwt;

import java.util.Collection;
import java.util.Collections;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import indiv.abko.taskflow.global.auth.AuthMember;

@Component
public class JwtAuthenticationConverter implements Converter<Jwt, JwtAuthenticationToken> {

	private static final String AUTHORITIES_KEY = "auth";

	@Override
	public JwtAuthenticationToken convert(Jwt jwt) {
		long memberId = Long.parseLong(jwt.getSubject());
		String userRole = jwt.getClaimAsString(AUTHORITIES_KEY);

		AuthMember authMember = new AuthMember(memberId);

		Collection<? extends GrantedAuthority> authorities =
			Collections.singletonList(new SimpleGrantedAuthority(userRole));

		return new JwtAuthenticationToken(authMember, jwt, authorities);
	}
}