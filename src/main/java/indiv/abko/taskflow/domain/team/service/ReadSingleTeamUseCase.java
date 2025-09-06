package indiv.abko.taskflow.domain.team.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import indiv.abko.taskflow.domain.team.dto.response.ReadSingleTeamResponse;
import indiv.abko.taskflow.domain.team.entity.Team;
import indiv.abko.taskflow.domain.team.exception.TeamErrorCode;
import indiv.abko.taskflow.domain.team.repository.TeamRepository;
import indiv.abko.taskflow.global.dto.DtoConstants;
import indiv.abko.taskflow.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReadSingleTeamUseCase {
	private final TeamRepository teamRepository;

	// 특정 팀 조회
	@Transactional(readOnly = true)
	public ReadSingleTeamResponse execute(Long teamId) {
		Team team = teamRepository.findWithTeamMembersById(teamId).orElseThrow(
			() -> new BusinessException(TeamErrorCode.NOT_FOUND_TEAM)
		);

		List<ReadSingleTeamResponse.UserResp> resps = team.getTeamMembers()
			.stream()
			.map(teamMember -> teamMember.getId().getMember())
			.map(member -> new ReadSingleTeamResponse.UserResp(
				member.getId(),
				member.getUsername(),
				member.getName(),
				member.getEmail(),
				member.getUserRole().name(),
				member.getCreatedAt().toInstant(DtoConstants.REAL_TIME_ZONE_OFFSET)
			))
			.collect(Collectors.toList());

		return new ReadSingleTeamResponse(
			team.getId(),
			team.getName(),
			team.getDescription(),
			team.getCreatedAt().toInstant(DtoConstants.REAL_TIME_ZONE_OFFSET),
			resps
		);
	}
}
