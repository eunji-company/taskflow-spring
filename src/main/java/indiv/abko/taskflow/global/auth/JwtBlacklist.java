package indiv.abko.taskflow.global.auth;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import indiv.abko.taskflow.global.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtBlacklist {

	private final ConcurrentHashMap<String, Date> blacklist = new ConcurrentHashMap<>();
	private final JwtUtil jwtUtil;

	/**
	 * 만료되지 않은 토큰을 블랙리스트에 추가
	 *
	 * @param token 블랙리스트에 추가할 JWT
	 */
	public void addToBlacklist(String token) {
		Claims claims = jwtUtil.parseClaims(token);
		Date expiration = claims.getExpiration();

		if (expiration.after(new Date())) {
			blacklist.put(token, expiration);
		}
	}

	public boolean isBlacklisted(String token) {
		return blacklist.containsKey(token);
	}

	/**
	 * 블랙리스트에서 만료된 토큰들을 제거
	 */
	public void cleanupExpiredTokens() {
		Date now = new Date();
		blacklist.entrySet().removeIf(entry -> entry.getValue().before(now));
	}

}
