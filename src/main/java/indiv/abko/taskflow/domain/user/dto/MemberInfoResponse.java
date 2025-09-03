package indiv.abko.taskflow.domain.user.dto;

import java.time.LocalDateTime;

public record MemberInfoResponse(Long id, String username, String email, String name, String role,
								 LocalDateTime createdAt) {

	public static MemberInfoResponse from(Long id, String username, String email, String name, String role,
		LocalDateTime createdAt) {
		return new MemberInfoResponse(id, username, email, name, role, createdAt);
	}
}
