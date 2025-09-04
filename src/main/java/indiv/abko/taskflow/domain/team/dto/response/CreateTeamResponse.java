package indiv.abko.taskflow.domain.team.dto.response;

import java.time.LocalDateTime;

public record CreateTeamResponse(
	Long id,
	String name,
	String description,
	LocalDateTime createdAt) {
}
