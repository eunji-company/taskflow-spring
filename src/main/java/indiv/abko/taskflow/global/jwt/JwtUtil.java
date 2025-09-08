package indiv.abko.taskflow.global.jwt;

import java.security.Key;
import java.time.Instant;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import indiv.abko.taskflow.domain.user.entity.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtUtil {

	public static final String BEARER_PREFIX = "Bearer ";
	private static final String AUTHORITIES_KEY = "auth";
	private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
	@Value("${jwt.secret}")
	private String secretKey;
	@Value("${jwt.access-token-validity-seconds}")
	private long accessTokenValiditySeconds;
	private Key key;

	@PostConstruct
	public void init() {
		this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
	}

	public String createAccessToken(Long memberId, UserRole userRole) {
		long now = (new Date()).getTime();
		Date validity = new Date(now + this.accessTokenValiditySeconds * 1000);

		return Jwts.builder()
			.setSubject(String.valueOf(memberId))
			.claim(AUTHORITIES_KEY, userRole.getKey())
			.setIssuedAt(new Date(now))
			.signWith(key, signatureAlgorithm)
			.setExpiration(validity)
			.compact();
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token);

			return true;
		} catch (SecurityException | MalformedJwtException e) {
			log.info("잘못된 JWT 서명입니다.");
		} catch (ExpiredJwtException e) {
			log.info("만료된 JWT 토큰입니다.");
		} catch (UnsupportedJwtException e) {
			log.info("지원되지 않는 JWT 토큰입니다.");
		} catch (IllegalArgumentException e) {
			log.info("JWT 토큰이 잘못되었습니다.");
		}

		return false;
	}

	public Jwt getJwt(String token) {
		Claims claims = parseClaims(token);
		Instant issuedAt = claims.getIssuedAt().toInstant();
		Instant expiresAt = claims.getExpiration().toInstant();

		return Jwt.withTokenValue(token)
			.header("alg", signatureAlgorithm.getValue())
			.subject(claims.getSubject())
			.claims(c -> c.putAll(claims))
			.issuedAt(issuedAt)
			.expiresAt(expiresAt)
			.build();
	}

	public Claims parseClaims(String token) {
		try {
			return Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody();
		} catch (ExpiredJwtException e) {
			// 만료된 토큰의 경우에도 호출 측에서 후속 처리할 수 있도록 Claims를 반환
			return e.getClaims();
		}
	}

}
