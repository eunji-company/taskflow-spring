package indiv.abko.taskflow.domain.team.dto.request;

import jakarta.validation.constraints.NotNull;

public record AddTeamMemberRequest(
	@NotNull(message = "사용자 ID 입력은 필수입니다.") Long userId) {
}
