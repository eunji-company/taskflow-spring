package indiv.abko.taskflow.domain.team.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import indiv.abko.taskflow.domain.team.entity.Team;
import indiv.abko.taskflow.domain.team.entity.TeamMemberId;
import indiv.abko.taskflow.domain.team.exception.TeamErrorCode;
import indiv.abko.taskflow.domain.team.repository.TeamMemberRepository;
import indiv.abko.taskflow.domain.team.repository.TeamRepository;
import indiv.abko.taskflow.domain.user.entity.Member;
import indiv.abko.taskflow.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeamService implements TeamServiceApi {
	private final TeamRepository teamRepository;
	private final TeamMemberRepository teamMemberRepository;

	@Override
	@Transactional(readOnly = true)
	public Team getByIdOrThrow(Long teamId) {
		return teamRepository.findById(teamId).
			orElseThrow(() -> new BusinessException(TeamErrorCode.NOT_FOUND_TEAM));
	}

	@Override
	@Transactional(readOnly = true)
	public void assertMemberInTeamOrThrow(Team team, Member member) {
		if (!teamMemberRepository.existsById(new TeamMemberId(team, member))) {
			throw new BusinessException(TeamErrorCode.NOT_TEAM_MEMBER);
		}
	}
}
