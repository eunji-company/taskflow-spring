package indiv.abko.taskflow.global.jwt;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtBlacklistService {

	private final ConcurrentHashMap<String, Date> blacklist = new ConcurrentHashMap<>();
	private final JwtUtil jwtUtil;

	/**
	 * 토큰을 블랙리스트에 추가
	 *
	 * @param token 블랙리스트에 추가할 JWT
	 */
	public void addToBlacklist(String token) {
		Claims claims = jwtUtil.parseClaims(token);
		Date expiration = claims.getExpiration();
		blacklist.put(token, expiration);
	}

	/**
	 * 해당 토큰이 블랙리스트에 있는지 확인
	 *
	 * @param token 확인할 JWT
	 * @return 블랙리스트에 있으면 true, 없으면 false
	 */
	public boolean isBlacklisted(String token) {
		return blacklist.containsKey(token);
	}

	/**
	 * 매시간 정각에 만료된 토큰을 블랙리스트에서 제거합니다.
	 */
	@Scheduled(cron = "0 0 * * * *")
	public void cleanupExpiredTokens() {
		Date now = new Date();
		blacklist.entrySet().removeIf(entry -> entry.getValue().before(now));
	}
}
