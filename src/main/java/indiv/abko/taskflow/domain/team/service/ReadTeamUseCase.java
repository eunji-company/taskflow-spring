package indiv.abko.taskflow.domain.team.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import indiv.abko.taskflow.domain.team.dto.response.ReadTeamResponse;
import indiv.abko.taskflow.domain.team.entity.Team;
import indiv.abko.taskflow.domain.team.entity.TeamMember;
import indiv.abko.taskflow.domain.team.repository.TeamMemberRepository;
import indiv.abko.taskflow.domain.user.entity.Member;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReadTeamUseCase {
	private final TeamMemberRepository teamMemberRepository;

	// 팀 목록 조회
	@Transactional(readOnly = true)
	public List<ReadTeamResponse> execute() {
		Map<Long, ReadTeamResponse> map = new HashMap<>();

		List<TeamMember> teamMembers = teamMemberRepository.findAllWithFetchBy();

		for (TeamMember teamMember : teamMembers) {
			Team team = teamMember.getId().getTeam();
			Long teamId = team.getId();

			boolean existsKey = map.containsKey(teamId);

			Member member = teamMember.getId().getMember();

			ReadTeamResponse.UserResp resp = new ReadTeamResponse.UserResp(
				member.getId(),
				member.getUsername(),
				member.getName(),
				member.getEmail(),
				member.getUserRole().name(),
				member.getCreatedAt());

			if (!existsKey) {
				map.put(
					teamId,
					new ReadTeamResponse(teamId, team.getName(),
						team.getDescription(),
						team.getCreatedAt(),
						new ArrayList<>(List.of(resp))));
			} else {
				ReadTeamResponse response = map.get(teamId);

				response.members().add(resp);

			}
		}

		return new ArrayList<>(map.values());
	}
}
