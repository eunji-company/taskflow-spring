package indiv.abko.taskflow.domain.team.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import indiv.abko.taskflow.domain.team.dto.response.ReadTeamMembersResponse;
import indiv.abko.taskflow.domain.team.entity.Team;
import indiv.abko.taskflow.domain.team.exception.TeamErrorCode;
import indiv.abko.taskflow.domain.team.repository.TeamRepository;
import indiv.abko.taskflow.global.dto.DtoConstants;
import indiv.abko.taskflow.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReadTeamMembersUseCase {
	private final TeamRepository teamRepository;

	// 팀 멤버 목록 조회
	@Transactional(readOnly = true)
	public List<ReadTeamMembersResponse> execute(Long teamId) {
		Team team = teamRepository.findWithTeamMembersById(teamId).orElseThrow(
			() -> new BusinessException(TeamErrorCode.NOT_FOUND_TEAM)
		);

		List<ReadTeamMembersResponse> dtos = team.getTeamMembers()
			.stream()
			.map(teamMember -> teamMember.getId().getMember())
			.map(member -> new ReadTeamMembersResponse(
				member.getId(),
				member.getUsername(),
				member.getName(),
				member.getEmail(),
				member.getUserRole().name(),
				member.getCreatedAt().toInstant(DtoConstants.REAL_TIME_ZONE_OFFSET)
			))
			.collect(Collectors.toList());

		return dtos;
	}
}
