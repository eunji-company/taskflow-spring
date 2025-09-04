package indiv.abko.taskflow.global.jwt;

import indiv.abko.taskflow.domain.user.entity.UserRole;
import indiv.abko.taskflow.global.auth.AuthMember;
import java.util.Collection;
import java.util.Collections;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationConverter implements Converter<Jwt, JwtAuthenticationToken> {

    private static final String AUTHORITIES_KEY = "auth";

    @Override
    public JwtAuthenticationToken convert(Jwt jwt) {
        long memberId = Long.parseLong(jwt.getSubject());
        UserRole userRole = UserRole.valueOf(jwt.getClaimAsString(AUTHORITIES_KEY));

        AuthMember authMember = new AuthMember(memberId, userRole);

        Collection<? extends GrantedAuthority> authorities =
                Collections.singletonList(new SimpleGrantedAuthority(userRole.getKey()));

        return new JwtAuthenticationToken(authMember, jwt, authorities);
    }
}