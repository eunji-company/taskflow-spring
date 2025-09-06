package indiv.abko.taskflow.global.auth;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtBlacklistScheduler {

	private final JwtBlacklist jwtBlacklist;

	/**
	 * 매시간 정각에 만료된 토큰을 블랙리스트에서 제거합니다.
	 */
	@Scheduled(cron = "0 0 * * * *")
	public void cleanupExpiredTokens() {
		jwtBlacklist.cleanupExpiredTokens();
	}

}
