package indiv.abko.taskflow.domain.team.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import indiv.abko.taskflow.domain.team.dto.response.UpdateTeamResponse;
import indiv.abko.taskflow.domain.team.entity.Team;
import indiv.abko.taskflow.domain.team.exception.TeamErrorCode;
import indiv.abko.taskflow.domain.team.repository.TeamRepository;
import indiv.abko.taskflow.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UpdateTeamUseCase {
	private final TeamRepository teamRepository;

	// 팀 수정
	@Transactional
	public UpdateTeamResponse execute(String name, String description, Long teamId) {
		Team team = teamRepository.findWithTeamMembersById(teamId).orElseThrow(
			() -> new BusinessException(TeamErrorCode.NOT_FOUND_TEAM)
		);

		team.update(name, description);

		List<UpdateTeamResponse.UserResp> dtos = team.getTeamMembers()
			.stream()
			.map(teamMember -> teamMember.getId().getMember())
			.map(member -> new UpdateTeamResponse.UserResp(
				member.getId(),
				member.getUsername(),
				member.getName(),
				member.getEmail(),
				member.getUserRole().name(),
				member.getCreatedAt()))
			.collect(Collectors.toList());

		return new UpdateTeamResponse(
			team.getId(),
			team.getName(),
			team.getDescription(),
			team.getCreatedAt(),
			dtos
		);
	}
}
