package indiv.abko.taskflow.domain.team.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import indiv.abko.taskflow.domain.auth.annotation.IsAdmin;
import indiv.abko.taskflow.domain.team.dto.request.AddTeamMemberRequest;
import indiv.abko.taskflow.domain.team.dto.request.CreateTeamRequest;
import indiv.abko.taskflow.domain.team.dto.request.UpdateTeamRequest;
import indiv.abko.taskflow.domain.team.dto.response.AddTeamMemberResponse;
import indiv.abko.taskflow.domain.team.dto.response.CreateTeamResponse;
import indiv.abko.taskflow.domain.team.dto.response.DeleteTeamMemberResponse;
import indiv.abko.taskflow.domain.team.dto.response.ReadSingleTeamResponse;
import indiv.abko.taskflow.domain.team.dto.response.ReadTeamResponse;
import indiv.abko.taskflow.domain.team.dto.response.UpdateTeamResponse;
import indiv.abko.taskflow.domain.team.service.AddTeamMemberUseCase;
import indiv.abko.taskflow.domain.team.service.CreateTeamUseCase;
import indiv.abko.taskflow.domain.team.service.DeleteTeamMemberUseCase;
import indiv.abko.taskflow.domain.team.service.DeleteTeamUseCase;
import indiv.abko.taskflow.domain.team.service.ReadSingleTeamUseCase;
import indiv.abko.taskflow.domain.team.service.ReadTeamUseCase;
import indiv.abko.taskflow.domain.team.service.UpdateTeamUseCase;
import indiv.abko.taskflow.global.dto.CommonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TeamController {
	private final CreateTeamUseCase createTeamUseCase;
	private final ReadTeamUseCase readTeamUseCase;
	private final ReadSingleTeamUseCase readSingleTeamUseCase;
	private final UpdateTeamUseCase updateTeamUseCase;
	private final DeleteTeamUseCase deleteTeamUseCase;

	private final AddTeamMemberUseCase addTeamMemberUseCase;
	private final DeleteTeamMemberUseCase deleteTeamMemberUseCase;

	// 팀 생성
	@IsAdmin
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

	// 팀 조회
	@GetMapping("/api/teams")
	public CommonResponse<List<ReadTeamResponse>> getAllTeams(
	) {
		List<ReadTeamResponse> readTeamResponses = readTeamUseCase.execute();

		return CommonResponse.success("팀 목록을 조회했습니다.", readTeamResponses);
	}

	// 특정 팀 조회
	@GetMapping("/api/teams/{teamId}")
	public CommonResponse<ReadSingleTeamResponse> getTeam(
		@PathVariable Long teamId
	) {
		ReadSingleTeamResponse readSingleTeamResponse = readSingleTeamUseCase.execute(teamId);

		return CommonResponse.success("팀 정보를 조회했습니다.", readSingleTeamResponse);
	}

	// 팀 수정
	@IsAdmin
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
	@IsAdmin
	@DeleteMapping("/api/teams/{teamId}")
	public CommonResponse<Void> deleteTeam(
		@PathVariable Long teamId
	) {
		deleteTeamUseCase.execute(teamId);

		return CommonResponse.success("팀이 성공적으로 삭제되었습니다.", null);
	}

	// 팀 멤버 추가
	@IsAdmin
	@PostMapping("/api/teams/{teamId}/members")
	public CommonResponse<AddTeamMemberResponse> createTeamMember(
		@Valid @RequestBody AddTeamMemberRequest addTeamMemberRequest,
		@PathVariable Long teamId
	) {
		AddTeamMemberResponse addTeamMemberResponse = addTeamMemberUseCase.execute(
			addTeamMemberRequest.userId(),
			teamId);

		return CommonResponse.success("멤버가 성공적으로 추가되었습니다.", addTeamMemberResponse);
	}

	// 팀 멤버 제거
	@IsAdmin
	@DeleteMapping("/api/teams/{teamId}/members/{userId}")
	public CommonResponse<DeleteTeamMemberResponse> deleteTeamMember(
		@PathVariable Long teamId,
		@PathVariable Long userId
	) {
		DeleteTeamMemberResponse deleteTeamMemberResponse = deleteTeamMemberUseCase.execute(teamId, userId);

		return CommonResponse.success("멤버가 성공적으로 제거되었습니다.", deleteTeamMemberResponse);
	}
}
