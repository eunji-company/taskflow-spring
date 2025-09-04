package indiv.abko.taskflow.domain.team.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import indiv.abko.taskflow.domain.team.dto.request.CreateTeamRequest;
import indiv.abko.taskflow.domain.team.dto.request.UpdateTeamRequest;
import indiv.abko.taskflow.domain.team.dto.response.CreateTeamResponse;
import indiv.abko.taskflow.domain.team.dto.response.UpdateTeamResponse;
import indiv.abko.taskflow.domain.team.service.CreateTeamUseCase;
import indiv.abko.taskflow.domain.team.service.DeleteTeamUseCase;
import indiv.abko.taskflow.domain.team.service.UpdateTeamUseCase;
import indiv.abko.taskflow.global.dto.CommonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TeamController {
	private final CreateTeamUseCase createTeamUseCase;
	private final UpdateTeamUseCase updateTeamUseCase;
	private final DeleteTeamUseCase deleteTeamUseCase;

	// 팀 생성
	@PostMapping("/api/teams")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<CreateTeamResponse> createTeam(
		@Valid @RequestBody CreateTeamRequest createTeamRequest
	) {
		CreateTeamResponse createTeamResponse = createTeamUseCase.execute(
			createTeamRequest.name(),
			createTeamRequest.description());

		return CommonResponse.success("팀이 성공적으로 생성되었습니다.", createTeamResponse);
	}

	// 팀 수정
	@PutMapping("/api/teams/{teamId}")
	public CommonResponse<UpdateTeamResponse> updateTeam(
		@Valid @RequestBody UpdateTeamRequest updateTeamRequest,
		@PathVariable Long teamId
	) {
		UpdateTeamResponse updateTeamResponse = updateTeamUseCase.execute(
			updateTeamRequest.name(),
			updateTeamRequest.description(),
			teamId);

		return CommonResponse.success("팀 정보가 성공적으로 업데이트되었습니다.", updateTeamResponse);
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
