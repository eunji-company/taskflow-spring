package indiv.abko.taskflow.domain.team.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AddTeamMemberRequest(
	@NotBlank(message = "사용자 ID 입력은 필수입니다.") Long userId) {
}
