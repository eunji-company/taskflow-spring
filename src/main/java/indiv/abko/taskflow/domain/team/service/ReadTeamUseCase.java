package indiv.abko.taskflow.domain.team.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import indiv.abko.taskflow.domain.team.dto.response.ReadTeamResponse;
import indiv.abko.taskflow.domain.team.entity.Team;
import indiv.abko.taskflow.domain.team.entity.TeamMember;
import indiv.abko.taskflow.domain.team.entity.TeamMemberId;
import indiv.abko.taskflow.domain.team.repository.TeamRepository;
import indiv.abko.taskflow.global.dto.DtoConstants;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReadTeamUseCase {
	private final TeamRepository teamRepository;

	// 팀 목록 조회
	@Transactional(readOnly = true)
	public List<ReadTeamResponse> execute() {
		List<Team> teams = teamRepository.findWithDetailBy();
		List<ReadTeamResponse> responses = new ArrayList<>();

		for (Team team : teams) {
			List<ReadTeamResponse.UserResp> members = team.getTeamMembers().stream()
				.map(TeamMember::getId)
				.map(TeamMemberId::getMember)
				.map(member -> {
					return new ReadTeamResponse.UserResp(
						member.getId(),
						member.getUsername(),
						member.getName(),
						member.getEmail(),
						member.getUserRole().name(),
						member.getCreatedAt().toInstant(DtoConstants.REAL_TIME_ZONE_OFFSET));
				})
				.toList();

			responses.add(new ReadTeamResponse(team.getId(), team.getName(),
				team.getDescription(),
				team.getCreatedAt().toInstant(DtoConstants.REAL_TIME_ZONE_OFFSET),
				members));
		}

		return responses;
	}
}
