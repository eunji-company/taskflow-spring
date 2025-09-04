package indiv.abko.taskflow.domain.team.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateTeamRequest(
	@NotBlank(message = "이름은 필수로 입력해주셔야 합니다.") String name,
	@NotBlank(message = "설명은 필수로 입력해주셔야 합니다.") String description) {
}
