package indiv.abko.taskflow.domain.user.dto;

import java.time.LocalDateTime;

public record MemberInfoResponse(Long id, String username, String email, String name, String role,
								 LocalDateTime createdAt) {
}
