package indiv.abko.taskflow.domain.team.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record CreateTeamResponse(
	Long id,
	String name,
	String description,
	LocalDateTime createdAt,
	List<Void> members) {
}
