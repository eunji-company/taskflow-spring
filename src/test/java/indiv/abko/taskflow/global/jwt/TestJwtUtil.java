package indiv.abko.taskflow.global.jwt;

import java.security.Key;
import java.util.Date;

import indiv.abko.taskflow.domain.user.entity.UserRole;
import indiv.abko.taskflow.global.auth.AuthMember;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class TestJwtUtil {

	private static final String AUTHORITIES_KEY = "auth";
	private static final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

	public static String createAccessToken(AuthMember authMember, long validitySeconds, String secretKey) {
		long now = (new Date()).getTime();
		Date validity = new Date(now + validitySeconds * 1000);
		Key key = Keys.hmacShaKeyFor(secretKey.getBytes());

		return Jwts.builder()
			.setSubject(String.valueOf(authMember.memberId()))
			.claim(AUTHORITIES_KEY, UserRole.USER.getKey())
			.setIssuedAt(new Date(now))
			.signWith(key, signatureAlgorithm)
			.setExpiration(validity)
			.compact();
	}
}
