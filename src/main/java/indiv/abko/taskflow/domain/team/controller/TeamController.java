package indiv.abko.taskflow.domain.team.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import indiv.abko.taskflow.domain.team.dto.request.TeamCreateRequest;
import indiv.abko.taskflow.domain.team.dto.response.TeamCreateResponse;
import indiv.abko.taskflow.domain.team.service.CreateTeamUseCase;
import indiv.abko.taskflow.domain.team.service.DeleteTeamUseCase;
import indiv.abko.taskflow.global.dto.CommonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TeamController {
	private final CreateTeamUseCase createTeamUseCase;
	private final DeleteTeamUseCase deleteTeamUseCase;

	// 팀 생성
	@PostMapping("/api/teams")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<TeamCreateResponse> createTeam(
		@Valid @RequestBody TeamCreateRequest teamCreateRequest
	) {
		TeamCreateResponse teamCreateResponse = createTeamUseCase.execute(teamCreateRequest.name(),
			teamCreateRequest.description());
		return CommonResponse.success("팀이 성공적으로 생성되었습니다.", teamCreateResponse);
	}

	// 팀 삭제
	@DeleteMapping("/api/teams/{teamId}")
	public CommonResponse<Void> deleteTeam(
		@PathVariable Long teamId
	) {
		deleteTeamUseCase.execute(teamId);
		return CommonResponse.success("팀이 성공적으로 삭제되었습니다.", null);
	}
}
