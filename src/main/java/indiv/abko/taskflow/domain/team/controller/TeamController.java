package indiv.abko.taskflow.domain.team.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import indiv.abko.taskflow.domain.team.dto.request.TeamCreateRequest;
import indiv.abko.taskflow.domain.team.dto.response.TeamCreateResponse;
import indiv.abko.taskflow.domain.team.service.CreateTeamUseCase;
import indiv.abko.taskflow.global.dto.CommonResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TeamController {
	private final CreateTeamUseCase createTeamUseCase;

	// 팀 생성
	@PostMapping("/api/teams")
	public CommonResponse<TeamCreateResponse> createTeam(
		@RequestBody TeamCreateRequest teamCreateRequest
	) {
		TeamCreateResponse teamCreateResponse = createTeamUseCase.execute(teamCreateRequest.getName(),
			teamCreateRequest.getDescription());
		return CommonResponse.success("팀이 성공적으로 생성되었습니다.", teamCreateResponse);
	}
}
