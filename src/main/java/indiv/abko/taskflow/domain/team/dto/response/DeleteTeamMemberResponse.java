package indiv.abko.taskflow.domain.team.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record DeleteTeamMemberResponse(
	Long id,
	String name,
	String description,
	LocalDateTime createdAt,
	List<UserResp> members
) {
	public record UserResp(
		long id,
		String username,
		String name,
		String email,
		String role,
		LocalDateTime createdAt) {
	}
}